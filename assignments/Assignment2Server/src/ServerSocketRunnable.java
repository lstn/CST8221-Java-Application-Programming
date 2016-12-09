
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
public class ServerSocketRunnable implements Runnable {
    
    private Socket socket;
    private ObjectOutputStream sockOutput;
    private ObjectInputStream sockInput;
    boolean ssRunning = true;
    
    public ServerSocketRunnable(Socket socket){
        this.socket = socket;
        System.out.println("Connecting to a client " + socket.toString());
    }

    @Override
    public void run() {
        if(!openIOStreams()){
            return;
        }
        String inputCommand;
        String[] command;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        
        
        try {
            while(ssRunning){
                inputCommand = (String) sockInput.readObject();//sockInput.readLine();
                //System.err.println(inputCommand);
                command = getInputCommand(inputCommand);
                switch(command[0]){
                    case "quit":
                        System.out.println("Server Socket: Closing client connection...");
                        sockOutput.writeObject("Connection closed.");
                        ssRunning = false;
                        break;
                    case "echo":
                        sockOutput.writeObject("ECHO: " + command[1]);
                        break;
                    case "time":
                        sockOutput.writeObject("TIME: " + timeFormatter.format(LocalDateTime.now()));
                        break;
                    case "date":
                        sockOutput.writeObject("DATE: " + dateFormatter.format(LocalDateTime.now()));
                        break;
                    case "help":
                        sockOutput.writeObject("Available Services:\nquit\necho\ntime\ndate\nhelp\ncls\n\n");
                        break;
                    case "cls":
                        sockOutput.writeObject("cls");
                        break;
                    case "unknown":
                    default:
                        sockOutput.writeObject("ERROR: Unrecognized command.");
                        break;
                }
                if(ssRunning){
                    Thread.sleep(100);
                }
            }
        } catch (IOException e) {
            System.err.println("Socket unexpectedly died");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Socket interrupted during sleep wait");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find class");
            e.printStackTrace();
        }
 
        
        closeSocket(); // close socket when we're done
    }
    
    private boolean strStartsWithIC(String str, String pre){
        return str.regionMatches(true, 0, pre, 0, pre.length());
    }
    
    private String getCommandArgs(String command, int preLen){
        if(preLen >= command.length()){
            return "";
        }
        String commandArgs = command.substring(preLen);
        if(commandArgs.charAt(0) != '-' && commandArgs.charAt(0) != ' '){
            return "ERR";
        }
        if(!(commandArgs.length() > 1)){
            return "";
        }
        return commandArgs.substring(1);
    }
    
    private String[] getInputCommand(String inputCommand){
        String[] command = new String[2];
        int cmdOffset = 5;
        
        if(strStartsWithIC(inputCommand, "-quit")){
            command[0] = "quit";
        } else if(strStartsWithIC(inputCommand, "-echo")){
            command[0] = "echo";
        } else if(strStartsWithIC(inputCommand, "-time")){
            command[0] = "time";
        } else if(strStartsWithIC(inputCommand, "-date")){
            command[0] = "date";
        } else if(strStartsWithIC(inputCommand, "-help")){
            command[0] = "help";
        } else if(strStartsWithIC(inputCommand, "-cls")){
            command[0] = "cls";
            cmdOffset = 4;
        } else {
            command[0] = "unknown";
            command[1] = inputCommand;
            return command;
        }
        
        command[1] = getCommandArgs(inputCommand, cmdOffset);
        if(command[1].equals("ERR")){
            command[0] = "unknown";
            command[1] = inputCommand;
            return command;
        }
        return command;
    }
    
    private boolean openIOStreams(){
        if(sockOutput != null || sockInput != null){
            return true;
        }
        
        try{
            sockOutput = new ObjectOutputStream(socket.getOutputStream());
            sockInput = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch(IOException e){
            System.err.println("Error opening input or output streams for connection");
            e.printStackTrace();
            closeSocket();
        }
        return false;
    }
    
    private void tryCloseIOStreams(){
        if (sockOutput != null && sockInput != null){
            try{
                sockInput.close();
                sockOutput.close();
            } catch(IOException e) {
                System.err.println("IOExcept tryclose");
                e.printStackTrace();
            } finally {
                sockInput = null;
                sockOutput = null;
            }
        }
    }
    
    private void closeSocket(){
        ssRunning = false;
        try{
            socket.close();
        } catch( IOException e ){
            System.err.println("Error when trying to close socket connection");
            e.printStackTrace();
        }
    }
}

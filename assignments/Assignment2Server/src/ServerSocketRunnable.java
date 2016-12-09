/**
 * Filename: ServerSocketRunnable.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 2
 * Date: 12/8/2016
 * Professor: Svillen Ranev
 * Purpose: Implements the ServerSocketRunnable class to be able to communicate with the client.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* This class implements Runnable and is responsible for communicating with the client
* and responding to the command strings sent by the client.
* 
* @author Lucas Estienne
* @version 1.0
* @see Runnable
* @since 1.8.0_112
*/
public class ServerSocketRunnable implements Runnable {
    
    /**
     * the Socket object for this worker
     */
    private Socket socket;
    /**
     * the output stream for this worker
     */
    private ObjectOutputStream sockOutput;
    /**
     * the input stream for this worker
     */
    private ObjectInputStream sockInput;
    /**
     * boolean to represent whether or not this worker should be running its while loop
     */
    boolean ssRunning = true;
    
    /**
     * Default constructor for the ServerSocketRunnable class.
     * 
     * @param socket Socket the socket for this worker
    */
    public ServerSocketRunnable(Socket socket){
        this.socket = socket;
        System.out.println("Connecting to a client " + socket.toString());
    }

    /**
    * Overrides Runnable.run and begins running in a loop listening for & handling client messages
    * until we kill the loop by flipping ssRunning to false.
    */
    @Override
    public void run() {
        if(!openIOStreams()){ // failed to open input streams
            return;
        }
        String inputCommand;
        String[] command; // String[2] array to hold the two different parts of a command
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy"); // day number month name year number
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a"); // hour:minute:second am/pm
        
        
        try {
            while(ssRunning){
                inputCommand = (String) sockInput.readObject(); // attempt to read incoming message from client
                
                command = getInputCommand(inputCommand); // parse the command string we got
                switch(command[0]){
                    case "quit": // client wishes to quit, terminate the loop and the thread will die 
                        System.out.println("Server Socket: Closing client connection...");
                        sockOutput.writeObject("Connection closed.");
                        ssRunning = false;
                        break;
                    case "echo": // echo
                        sockOutput.writeObject("ECHO: " + command[1]);
                        break;
                    case "time": // displays server time
                        sockOutput.writeObject("TIME: " + timeFormatter.format(LocalDateTime.now()));
                        break;
                    case "date": // displays server date
                        sockOutput.writeObject("DATE: " + dateFormatter.format(LocalDateTime.now()));
                        break;
                    case "help": // displays a help string with the available commands/services
                        sockOutput.writeObject("Available Services:\nquit\necho\ntime\ndate\nhelp\ncls\n");
                        break;
                    case "cls": // clear the terminal text area
                        sockOutput.writeObject("cls");
                        break;
                    case "unknown": // handle unknown commands
                    default:
                        sockOutput.writeObject("ERROR: Unrecognized command.");
                        break;
                }
                if(ssRunning){
                    Thread.sleep(100); // sleep 100 ms inbetween commands
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
    
    /**
    * This method returns whether or not a string starts with a certain prefix,
    * and does so while ignoring case.
    * 
    * @param str String the string to be checked
    * @param pre String the prefix to check
    * @return true if the string contains the prefix regardless of case, false if not
    */
    private boolean strStartsWithIC(String str, String pre){
        return str.regionMatches(true, 0, pre, 0, pre.length());
    }
    
    /**
    * This method returns the arguments for a command.
    * 
    * @param command String the command to extract arguments from
    * @param preLen the length of the actual command string
    * @return String containing the arguments passed
    */
    private String getCommandArgs(String command, int preLen){
        if(preLen >= command.length()){ // no arguments
            return "";
        }
        String commandArgs = command.substring(preLen);
        if(commandArgs.charAt(0) != '-' && commandArgs.charAt(0) != ' '){
            return "ERR"; // return ERR string if the character does not match
        }
        if(!(commandArgs.length() > 1)){ // if length NOT over 1, no arguments
            return "";
        }
        return commandArgs.substring(1); // otherwise, substring by 1 more position and return
    }
    
    /**
    * This method extracts the two portions of a raw command, the command itself and the arguments if any.
    * 
    * @param inputCommand String the raw command to process
    * @return String[2] containing the command at index 0 and the arguments at index 1
    */
    private String[] getInputCommand(String inputCommand){
        String[] command = new String[2];
        int cmdOffset = 5; // offset for every command except cls
        
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
            cmdOffset = 4; // reduce offset to match the "cls" command length
        } else { // not a known command
            command[0] = "unknown"; // return unknown command string
            command[1] = inputCommand; // and return the full input command in the arguments field for potential debugging purposes.
            return command;
        }
        
        command[1] = getCommandArgs(inputCommand, cmdOffset); 
        if(command[1].equals("ERR")){ // check if arguments errored out
            command[0] = "unknown"; // return unknown command string
            command[1] = inputCommand; // and return the full input command in the arguments field for potential debugging purposes.
            return command;
        }
        return command; // success
    }
    
    
    /**
     * This method attempts to open an ObjectOutputStream & ObjectInputStream to act as
     * input/output for our worker.
     * 
     * @return boolean true if successful false if not
    */
    private boolean openIOStreams(){
        if(sockOutput != null || sockInput != null){
            return true; // if output and input aren't null then return success
        }
        
        try{
            // initialize input and output
            sockOutput = new ObjectOutputStream(socket.getOutputStream());
            sockInput = new ObjectInputStream(socket.getInputStream());
            return true; // success
        } catch(IOException e){
            System.err.println("Error opening input or output streams for connection");
            e.printStackTrace();
            closeSocket();
        }
        return false;
    }
    
    /**
     * This method tries to close and null the input and output streams if they are not null.
    */
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
    
    /**
     * This method attempts to close the worker's socket and stops the run's while loop so that this
     * thread can die.
    */
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

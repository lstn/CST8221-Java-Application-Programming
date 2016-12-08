
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
public class Server {
    private static final int DEFAULT_PORT = 65535;
    private static final int MAX_THREADS = 10;
    
    private static Server server;
    private ServerSocket serverSocket;
    private boolean serverRunning = true;
    private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS); 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int serverPort = DEFAULT_PORT;
        
        if (args.length > 0){
            try{
                serverPort = Integer.parseInt(args[0]);
            } catch(NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                
                System.err.println("Defaulting to " + DEFAULT_PORT + ".");
                serverPort = DEFAULT_PORT;
            }
        }
        
        
        server = new Server();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stopServer(); // on exit
        }));

        server.startServer(serverPort); // start listening 

        
    }
    
    private void startServer(int serverPort){ 
        try{
            System.out.println("Using" + ((serverPort == DEFAULT_PORT) ? " default " : " ") + "port: " + serverPort);
            serverSocket = new ServerSocket(serverPort);
            
            while(serverRunning){
                try{
                    if(serverRunning){
                        Socket s = serverSocket.accept();
                        executorService.submit(new ServerSocketRunnable(s));
                    }
                } catch (IOException e2) {
                    if(e2.getMessage().contains("socket closed")){
                        serverRunning = false;
                        serverSocket.close();
                        return;
                    }
                    System.err.println("Error accepting new connection");
                    e2.printStackTrace();
                }
                Thread.sleep(100);
            }
            
        } catch (IOException e) {
            System.err.println("Error starting server on port " + serverPort + ". Exiting.");
            e.printStackTrace();
        } catch(InterruptedException e){
            System.err.println("Connection listener interrupted");
        }
    }
    
    private void stopServer(){
        serverRunning = false;
        executorService.shutdownNow();
        try{
            serverSocket.close();
            System.out.println("Server shut down.");
        } catch( IOException e ){
            System.err.println("Error when trying to shut down server");
            e.printStackTrace();
        }
    }
    
}

/**
 * Filename: Server.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 2
 * Date: 12/8/2016
 * Professor: Svillen Ranev
 * Purpose: Responsible for spawning the worker threads for this server in our executor pool. 
 *          Contains main method for this program.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* This class handles the executor and its pool by making use of ExecutorService.
* Creates a serverSocket on either the passed command line argument port or DEFAULT_PORT and
* handles connections from clients.
* 
* @author Lucas Estienne
* @version 1.0
* @see java.net.ServerSocket java.util.concurrent.ExecutorService ServerSocketRunnable
* @since 1.8.0_112
*/
public class Server {
    /**
     * Default port our server to listen on. Value: {@value}
     */
    private static final int DEFAULT_PORT = 65535;
    /**
     * Max threads in our executor pool. Value: {@value}
     */
    private static final int MAX_THREADS = 10;
    
    /**
     * the Server object for our server.
     */
    private static Server server;
    /**
     * the ServerSocket object for our server.
     */
    private ServerSocket serverSocket;
    /**
     * whether or not the server should be running, to be used in order to kill the program loop/thread
     */
    private boolean serverRunning = true;
    /**
     * the ExecutorService instance for our server, initialized with a fixed thread pool of MAX_THREADS
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS); 
    /**
     * @param args the command line arguments, including the port for the server to be running on.
     */
    public static void main(String[] args) {
        int serverPort = DEFAULT_PORT;
        
        if (args.length > 0){ // if we have CLI arguments
            try{
                serverPort = Integer.parseInt(args[0]); // try to set port to the CLI passed port
            } catch(NumberFormatException e) { // invalid argument, default to DEFAULT_PORT
                System.err.println("Argument" + args[0] + " must be an integer.");
                
                System.err.println("Defaulting to " + DEFAULT_PORT + ".");
                serverPort = DEFAULT_PORT;
            }
        }
        
        // initialize our server
        server = new Server();
        
        // add a hook on shutdown to call the stopServer member method of our server on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stopServer(); // on exit
        }));

        server.startServer(serverPort); // start listening 
    }
    
    /**
     * This method attempts start out Server and begins spawning worker ServerSocketRunnables,
     * until we kill the loop by flipping serverRunning to false.
     * 
     * @param serverPort int the port the start the server on
    */
    private void startServer(int serverPort){ 
        try{
            System.out.println("Using" + ((serverPort == DEFAULT_PORT) ? " default " : " ") + "port: " + serverPort);
            serverSocket = new ServerSocket(serverPort); // initialize our ServerSocket
            
            while(serverRunning){ // 
                try{
                    if(serverRunning){
                        Socket s = serverSocket.accept();
                        executorService.submit(new ServerSocketRunnable(s));
                    }
                } catch (IOException e2) {
                    if(e2.getMessage().contains("socket closed")){ // check if our socket unexpectedly died
                        // if so, kill our server
                        serverRunning = false;
                        serverSocket.close();
                        return;
                    }
                    System.err.println("Error accepting new connection");
                    e2.printStackTrace();
                }
                if(serverRunning){
                    Thread.sleep(100); // sleep for 100 ms between attempts to connect
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error starting server on port " + serverPort + ". Exiting.");
            e.printStackTrace();
        } catch(InterruptedException e){
            System.err.println("Connection listener interrupted");
        }
    }
    
    /**
     * This method calls for our Executor pool to shut down, and attempts to close our ServerSocket.
    */
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

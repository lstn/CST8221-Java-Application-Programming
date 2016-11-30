/**
 * Filename: Client.java
 * Author: Lucas Estienne, 040 819 959
 * Course: CST8221 - JAP, Lab Section: 302
 * Assignment: 2
 * Date: 11/30/2016
 * Professor: Svillen Ranev
 * Purpose: Responsible for instanciating a ClientView and making it visible. Contains main method for this program.
 */

public class Client {
        /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientView().setVisible(true);
            }
        });
    }
}

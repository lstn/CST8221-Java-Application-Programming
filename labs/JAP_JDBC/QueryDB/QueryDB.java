/**
 * This is the launching class for the program that
 * demonstrates several complex database queries
 * and displays the result in a table format
 * @version 1.16.2 
 * @author Svillen Ranev 
 * @since Java 1.8_91
 */

import javax.swing.JFrame;
import java.awt.EventQueue;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UIManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryDB {
   public static void main(String[] args)   {
      //make the Table look nicer - row colored interchangeably
      try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            //uses default logging.properties file (jre/lib)- prints on Console
            Logger.getLogger(QueryDB.class.getName()).log(Level.SEVERE,
                    "Failed to apply Nimbus look and feel",ex );
        }
      
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new QueryDBFrame();
               frame.setVisible(true);
            }
         });
   }
}

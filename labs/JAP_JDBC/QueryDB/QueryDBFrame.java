/**
 * This program demonstrates several complex database queries
 * and displays the result in a table format
 * @version 1.16.2 
 * @author Svillen Ranev (based in parts on Cay Horstmann's program implementation)
 * @since Java 1.8_91
 */
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;
import javax.swing.*;
import javax.sql.rowset.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;




/**
 * This frame displays combo boxes for query parameters, a table for queries results,
 * a label status line to display command results, dialogs for run-time errors,
 * and buttons to launch a query and a price update.
 */
public class QueryDBFrame extends JFrame {
   /* Class constants */
   public static final int DEFAULT_WIDTH = 500;
   public static final int DEFAULT_HEIGHT = 430;
   public static final int FIRST_COLUMN_SIZE = 70;
   /* Class fields */
   private JComboBox <String> authors;
   private JComboBox <String> publishers;
   private JTextField priceChange;
   private JTable resultTable;
   private Connection connection;
   private boolean connected;  // keeps track of database connection status
   private JLabel status;
    
   private PreparedStatement authorQueryStmt;
   private PreparedStatement authorPublisherQueryStmt;
   private PreparedStatement publisherQueryStmt;
   private PreparedStatement allQueryStmt;
   private PreparedStatement priceUpdateStmt;
   
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   private String firstColumnName;
   private TableColumn firstTableColumn;
   private int numberOfRows;// number of row in the query

   private DatabaseTableModel tableModel;

   /* Predefined SQL statements */ 
   private static final String authorPublisherQuery = "SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors, Publishers"
         + " WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN"
         + " AND Books.Publisher_Id = Publishers.Publisher_Id AND Authors.Name = ?"
         + " AND Publishers.Name = ?";

   private static final String authorQuery = "SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors"
         + " WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN"
         + " AND Authors.Name = ?";

   private static final String publisherQuery = "SELECT Books.Price, Books.Title FROM Books, Publishers"
         + " WHERE Books.Publisher_Id = Publishers.Publisher_Id AND Publishers.Name = ?";

   private static final String allQuery = "SELECT Books.Price, Books.Title FROM Books";

   private static final String priceUpdate = "UPDATE Books " + "SET Price = Price + ? "
         + " WHERE Books.Publisher_Id = (SELECT Publisher_Id FROM Publishers WHERE Name = ?)";

   /** Default constructor */
   public QueryDBFrame(){
      setTitle("Query DB");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      JPanel topPanel = new JPanel(new BorderLayout());
      JPanel setQueryPanel = new JPanel(new GridLayout(1,2,5,5));
      JPanel executeQueryPanel = new JPanel(new GridLayout(1,3,5,5));
      topPanel.add(setQueryPanel, BorderLayout.NORTH);
      topPanel.add(executeQueryPanel, BorderLayout.SOUTH);
      add(topPanel,BorderLayout.NORTH);
      //create the authors' combobox
      authors = new JComboBox <String>();
      authors.setEditable(false);
      authors.addItem("Any Author");
      //create the publishers' combobox
      publishers = new JComboBox <String>();
      publishers.setEditable(false);
      publishers.addItem("Any Publisher");

      priceChange = new JTextField(8);
      priceChange.setText("-5.00");

      try {
        
         connection = getConnection();
         
         connected = true;
         Statement stmt = connection.createStatement();
/* Derby DB driver does not support scrollable results.
   The statement below will not have any effect.
*/     
//         Statement stmt = connection.createStatement( 
//         ResultSet.TYPE_SCROLL_INSENSITIVE,
//         ResultSet.CONCUR_READ_ONLY )
     //set the authors' combobox
         String query = "SELECT Name FROM Authors";
           resultSet = stmt.executeQuery(query);
         while (resultSet.next())
            authors.addItem(resultSet.getString(1));
         resultSet.close();
     //set the publishers' combobox
         query = "SELECT Name FROM Publishers";
         resultSet = stmt.executeQuery(query);
         while (resultSet.next())
            publishers.addItem(resultSet.getString(1));
         resultSet.close();
         stmt.close();
      // query the database 
         if (allQueryStmt == null) allQueryStmt = connection.prepareStatement(allQuery);
           resultSet = allQueryStmt.executeQuery();
      // create JTable based on the tableModel             
           tableModel = new DatabaseTableModel();    
           tableModel.setTableModel();
           resultTable = new JTable( tableModel );
           //set the cell spacinng
           resultTable.setIntercellSpacing(new Dimension(2,2));
           //needed for Nimbus look-and-feel: otherwise true by default
           resultTable.setShowGrid(true);  
           //set greed color
           resultTable.setGridColor(Color.BLUE);
      // add a generic sorter to sort the rows   
           final TableRowSorter< TableModel > sorter = 
            new TableRowSorter< TableModel >( tableModel );
           resultTable.setRowSorter( sorter );
//get the first column name and set the first column size           
           firstColumnName = metaData.getColumnName(1);
           firstTableColumn = resultTable.getColumn(firstColumnName);
           firstTableColumn.setMinWidth(FIRST_COLUMN_SIZE);
           firstTableColumn.setMaxWidth(FIRST_COLUMN_SIZE);
           
           
       
      }catch (SQLException e){
         String message ="";
         for (Throwable t : e)
            message += t.getMessage()+"\n";
       
         message += "The application will terminate! Start the database and try again!";
         JOptionPane.showMessageDialog(this,message ,"Database Server Error", JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }catch (IOException e){ 
          JOptionPane.showMessageDialog(this,e ,"Database Server Error", JOptionPane.ERROR_MESSAGE);
          System.exit(2);
      }
      
      
       setQueryPanel.add(authors);
       setQueryPanel.add(publishers);
       
       JButton queryButton = new JButton("Query");
       queryButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               executeQuery();
            }
         });

      JButton changeButton = new JButton("Change prices");
      changeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               changePrices();
            }
         });
      
        executeQueryPanel.add(queryButton);
        executeQueryPanel.add(changeButton);
        executeQueryPanel.add(priceChange); 

        add(new JScrollPane(resultTable),BorderLayout.CENTER);

        status = new JLabel(" ");
        add(status,BorderLayout.SOUTH);
// Window Listener must be used to grantee that the connection will be closed
// when the application is terminated     
        addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent event){
            
               try{             
                  if (connection != null) connection.close();
                  connected = false;
               }
               catch (SQLException e){
                   String message ="";
                   for (Throwable t : e)
                      message += t.getMessage()+"\n";
           
                  JOptionPane.showMessageDialog(null,message ,"Error: Connection not closed properly!", JOptionPane.ERROR_MESSAGE);
               }finally{
                 System.exit(0);
               }
            }
         });
   }//end of default connstructor

   /**
    * The method executes the selected query using a prepared statement.
    */
   private void executeQuery(){

      try{
         String author = (String) authors.getSelectedItem();
         String publisher = (String) publishers.getSelectedItem();
         if (!author.equals("Any Author") && !publisher.equals("Any Publisher")){
            if (authorPublisherQueryStmt == null) authorPublisherQueryStmt = connection
                  .prepareStatement(authorPublisherQuery);
            authorPublisherQueryStmt.setString(1, author);
            authorPublisherQueryStmt.setString(2, publisher);
            resultSet = authorPublisherQueryStmt.executeQuery();
         } else if (!author.equals("Any Author") && publisher.equals("Any Publisher")){
            if (authorQueryStmt == null) authorQueryStmt = connection.prepareStatement(authorQuery);
            authorQueryStmt.setString(1, author);
            resultSet = authorQueryStmt.executeQuery();
         } else if (author.equals("Any Author") && !publisher.equals("Any Publisher")){
            if (publisherQueryStmt == null) publisherQueryStmt = connection
                  .prepareStatement(publisherQuery);
            publisherQueryStmt.setString(1, publisher);
            resultSet = publisherQueryStmt.executeQuery();
         }else{
            if (allQueryStmt == null) allQueryStmt = connection.prepareStatement(allQuery);
            resultSet = allQueryStmt.executeQuery();
         }
//set table with the current query
         tableModel.setTableModel();
//set first column size         

           firstTableColumn = resultTable.getColumn(firstColumnName);
           firstTableColumn.setMinWidth(FIRST_COLUMN_SIZE);
           firstTableColumn.setMaxWidth(FIRST_COLUMN_SIZE);
        
//close the current result set         
         resultSet.close();
      }
      catch (SQLException e){
          String message ="";
          for (Throwable t : e)
             message += t.getMessage()+"\n";
        
          JOptionPane.showMessageDialog(this,message ,"Error", JOptionPane.ERROR_MESSAGE);        
      }
   }

   /**
    * The method executes an update statement to change prices.
    */
   public void changePrices(){
   
      String publisher = (String) publishers.getSelectedItem();
      if (publisher.equals("Any Publisher")){    
         status.setText("Sorry! This is an illegal database update operation!");
         return;
      }
      
      try{
         if (priceUpdateStmt == null) priceUpdateStmt = connection.prepareStatement(priceUpdate);
         priceUpdateStmt.setString(1, priceChange.getText());
         priceUpdateStmt.setString(2, publisher);
         int r = priceUpdateStmt.executeUpdate();
         status.setText(r + " records updated.");
      }
      catch (SQLException e){
         String message ="";
         for (Throwable t : e)
            message += t.getMessage()+"\n";
           
         JOptionPane.showMessageDialog(this,message ,"Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   /**
    * The method gets a connection from the properties specified in the file database.properties
    * @return the database connection
    */
   public static Connection getConnection() throws SQLException, IOException{
      Properties props = new Properties();
      FileInputStream in = new FileInputStream("database.properties");
      props.load(in);
      in.close();

      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      return DriverManager.getConnection(url, username, password);
   }


/** This class implements the table model necessary to display the query results.
 *  In Java ResultSet rows and columns are counted from 1 but JTable 
 *  rows and columns are counted from 0. Because of that when processing 
 *  ResultSet rows or columns for use in  a JTable model, it is 
 *  necessary to add 1 to the row or column number to manipulate
 *  the appropriate ResultSet column (i.e., JTable column 0 is 
 *  ResultSet column 1 and JTable row 0 is ResultSet row 1).
 */
 private class DatabaseTableModel extends AbstractTableModel {

/** Storage for the table values */
       private ArrayList <ArrayList<Object>>  tableValues;

   /** The method returns a class that represents column type
    *  @throw unchecked IllegalStateException
    */
@Override   
   public Class getColumnClass( int column ) throws IllegalStateException
   {
      // ensure database connection is available
      if ( !connected ) 
         throw new IllegalStateException( "Database not connected!" );

      // determine Java column class
      try {
         String className = metaData.getColumnClassName( column + 1 );
         
         // return Class object that represents className
         // System.out.println("Classname Object: "+ Class.forName( className ));
         return Class.forName( className );
      } catch ( Exception e ) {
      	JOptionPane.showMessageDialog(null,e.getMessage(),"Error: column class cannot be determined!", JOptionPane.ERROR_MESSAGE);
//         exception.printStackTrace();
      } // end catch
      
      return Object.class; // if problems occur above, assume type Object
   } // end method getColumnClass

   /** The method returns the number of columns in ResultSet */
@Override 
   public int getColumnCount() throws IllegalStateException  {   
      // ensure database connection is available
      if ( !connected ) 
         throw new IllegalStateException( "Database not connected!" );

      // determine number of columns
      try {
         return metaData.getColumnCount(); 
      } catch ( SQLException e ) {
         String message ="";
         for (Throwable t : e)
            message += t.getMessage()+"\n";
        
        JOptionPane.showMessageDialog(null,message,"Error: SQL error!", JOptionPane.ERROR_MESSAGE);
//         sqlException.printStackTrace();
      } // end try-catch
      
      return 0; // if problems occur above, return 0 for number of columns
   } // end method getColumnCount

   /** The method returns the name of a particular column in the ResultSet */
@Override 
   public String getColumnName( int column ) throws IllegalStateException  {    
      // ensure database connection is available
      if ( !connected ) 
         throw new IllegalStateException( "Database not connected!" );

      // determine column name
      try 
      {
         return metaData.getColumnName( column + 1 );  
      } catch ( SQLException e ) {
         String message ="";
         for (Throwable t : e)
            message += t.getMessage()+"\n";
        
        JOptionPane.showMessageDialog(null,message,"Error: SQL error!", JOptionPane.ERROR_MESSAGE);
//         sqlException.printStackTrace();
      } // end try-catch
      
      return ""; // if problems, return empty string for column name
   } // end method getColumnName

   /**The method returns number of rows in ResultSet */
@Override 
   public int getRowCount() {
      return numberOfRows;
   } // end method getRowCount

   /** The method returns thevalue in particular row and column of the table model */
@Override 
   public Object getValueAt( int row, int column ) 
      throws IllegalStateException {
      // ensure database connection is available
      if ( !connected ) 
         throw new IllegalStateException( "Database not connected!" );

      // obtain a value at specified ResultSet row and column
      
      return ((tableValues.get(column).get(row)!= null) ? tableValues.get(column).get(row): "") ;

    } // end method getValueAt
   
 /** This method sets the table model for the current query. 
  *  When the model is set it fires a fireTableStructureChanged event
  *  which causes update of the view of the table.
  */
   public void setTableModel( ) 
      throws SQLException , IllegalStateException  {
   
      // ensure database connection is available
      if ( !connected ) 
         throw new IllegalStateException("Database not connected!" );

      // obtain meta data for ResultSet

      metaData = resultSet.getMetaData();

      // determine the number of columns
      int alSize = metaData.getColumnCount();
      //create a value storage
      tableValues =  new ArrayList<ArrayList<Object>>(alSize);
      //initialize the value storage
      for(int i = 0; i < alSize;++i)
           tableValues.add(new ArrayList <Object>());  
     //populate the storage
     while (resultSet.next()){
        for(int i = 0; i < alSize;++i){
           if(getColumnClass(i) == BigDecimal.class)
             tableValues.get(i).add(resultSet.getBigDecimal(i+1));
           else
             tableValues.get(i).add(resultSet.getString(i+1));
        }
     }
    // determine number of rows in ResultSet
       numberOfRows = tableValues.get(0).size();
           
    // notify JTable controller that the model has changed
      //fireTableStructureChanged();
      fireTableDataChanged();
   } // end method setQuery
      
 } // end inner class DatabaseTableModel    
}//end class QueryDBFrame

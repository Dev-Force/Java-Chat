//package database;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//
//
///**
// *
// * @author Tolis
// */
//public class Database 
//{
//    private Connection dbCon = null;
//    private final String dbName = "chat.db";
//    
//    private final String createUsersTable =     "CREATE TABLE USERS " +
//                                                "(ID INT PRIMARY KEY            NOT NULL," +
//                                                " NAME           VARCHAR2(255)  NOT NULL, " +
//                                                " PASSWORD       VARCHAR2(255)  NOT NULL" +
//                                                ")";
//    
//    private final String createserMessagesTable =  "CREATE TABLE MESSAGES "
//                                                + "("
//                                                + " ID          INT PRIMARY KEY NOT NULL,"
//                                                + " MESSAGE     TEXT"
//                                                + " FROMUSER    INT             NOT NULL, "
//                                                + " TOUSER      INT             NOT NULL "
//                                                + " FOREIGN KEY (FROMUSER) REFERENCES USERS(ID),"
//                                                + " FOREIGN KEY (TOUSER) REFERENCES USERS(ID)"
//                                                + ")";
//    
//        
//    
//    public Database()
//    {
//        
//       // connect to database or create it if it does not exist
//       connect();
//       createTables();
// 
//    }
//    
//
//    
//    /**
//     * Returns -1 if none is found or an exception was raised
//     * 
//     * @param name the name of the user
//     * @return the id of the user
//     */
//    public int getUserID(String name) 
//    {
//        
//        try 
//        {
//            // create new PreparedStatement and execute sql query
//            String sql = "SELECT ID FROM USERS WHERE USER = ?";
//            PreparedStatement ps = dbCon.prepareStatement(sql);
//            ps.setString(1, name);
//            
//            // get user's id from database
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) 
//                return rs.getInt("ID");
//            
//        } 
//        catch (SQLException ex) 
//        {
//            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return -1;
//    }
//    
//    /**
//     * Use getUserID method to find the users ID
//     * 
//     * @param from the user the message is from
//     * @param message the actual message
//     * @param to the user the message is supposed to go to
//     */
//    public void insertMessage(int from, String message, int to) 
//    {
//        
//        try 
//        {
//            // create new PreparedStatement and execute sql query
//            String sql = "INSERT INTO MESSAGES (FROMUSER, MESSAGE, TOUSER) VALUES(?, ?, ?)";
//            PreparedStatement ps = dbCon.prepareStatement(sql);
//            ps.setInt(1, from);
//            ps.setString(2, message);
//            ps.setInt(3, to);
//            ps.executeUpdate();
//        } 
//        catch (SQLException ex) 
//        {
//            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
//    
//    /**
//     * Connects to and/or creates the sqlite db (includes the ".db" extention)
//     * 
//     * @param dbName the name of the sqlite file
//     */
//    private void connect() 
//    {
//        
//        try 
//        {
//            // connect to database
//            Class.forName("org.sqlite.JDBC");
//            dbCon = DriverManager.getConnection("jdbc:sqlite:" + dbName);
//        } 
//        catch ( Exception e ) 
//        {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        
//        System.out.println("Connected to  database successfully");
//        
//    }
//    
//    /**
//     * Creates the necessary tables in the database defined by connect
//     */
//    private void createTables() 
//    {
//        
//        try 
//        {
//            // check if "USERS" table exists and if it does, exit
//            DatabaseMetaData dbm = dbCon.getMetaData();
//            ResultSet tables = dbm.getTables(null, null, "USERS", null);
//            if (tables.next()) 
//              return;
//            
//            // create 'users' and 'messages' table
//            Statement stmt = dbCon.createStatement();
//            stmt.executeUpdate(createUsersTable);
//            stmt.executeUpdate(createserMessagesTable);
//            stmt.close();
//        } 
//        catch (SQLException ex) 
//        {
//            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//            
//            try 
//            {
//                // close database connection
//                dbCon.close();
//            } 
//            catch (SQLException ex1) 
//            {
//                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex1);
//            }
//            
//            
//            File file = new File(dbName);
//            System.out.println(file.getAbsolutePath());
//            file.delete();
//        }
//        
//    }
//    
//    
//    
//}

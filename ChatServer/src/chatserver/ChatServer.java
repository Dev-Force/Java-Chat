package chatserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.ConnectionMessage;
import messages.SimpleMessage;



public class ChatServer 
{
    private static List<ClientHandler> Clients;
    private static ServerSocket ServSock;


    
    public static void main(String[] args) 
    {
        //Database db;
        Clients = new ArrayList<ClientHandler>();
        int port;
        String ip;
        

        // check if given arguments are correct
        if(args.length != 2)
        {
            System.out.println("Usage: java -jar <jarfile> <server ip> <port>");
            System.exit(0);
        }
        
        try 
        {
            ip = args[0];
            port = Integer.parseInt(args[1]);
                
            // create new Database object
            // if database does not exist, create it
            //db = new Database();
        
            // setup connection listener
            setupListener(ip, port);
        } 
        catch (NumberFormatException e) 
        {
            System.err.println("Argument" + args[1] + " must be an integer.");
            System.exit(1);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected static List<ClientHandler> getClients() 
    {
        return Clients;
    }

    protected static void setClients(List<ClientHandler> Clients) 
    {
        ChatServer.Clients = Clients;
    }
    
    private synchronized static void setupListener(String ip, int port) throws IOException
    {  
        // setup socket to accept connections
        ServSock = new ServerSocket(port,50,InetAddress.getByName(ip));
        System.out.println(ServSock.getInetAddress());
        
        // accept connections and service clients
        while(true) 
        {
            // accept new connection
            Socket socket = ServSock.accept();
            System.out.println("client with ip " + socket.getInetAddress().getHostAddress());
            
            // create new ClientThread to take care of client
            ClientHandler client = new ClientHandler();
            client.setSocket(socket);
            
            // read ConnectionMessage from socket
            ConnectionMessage connectionmsg = readConnectionMessage(socket);
 
            System.out.println("Username: " + connectionmsg.getUsername());
            
            client.setUsername(connectionmsg.getUsername());
            Clients.add(client);
            
            // start client handler thread
            client.start();
        } 
    }
    
  
    
    
    
    
    private static ConnectionMessage readConnectionMessage(Socket socket) throws IOException
    {
        try {
            //        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//   
//
//        byte[] usernamelengthbuf = new byte[4];
//        inputStream.read(usernamelengthbuf,0,4);
//        int usernamelength = java.nio.ByteBuffer.wrap(usernamelengthbuf).getInt();
//                
//        byte[] publickeylengthbuf = new byte[4];
//        inputStream.read(publickeylengthbuf,0,4);
//        int publickeylength = java.nio.ByteBuffer.wrap(publickeylengthbuf).getInt();
//                
//        byte[] usernamebuf = new byte[usernamelength];
//        inputStream.read(usernamebuf,0,usernamelength);
//        String username = new String(usernamebuf, "US-ASCII");
//                
//        byte[] publickeybuf = new byte[publickeylength];
//        inputStream.read(publickeybuf,0,publickeylength);
//                
//        return new ConnectionMessage(username,publickeybuf);
            
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ConnectionMessage o = (ConnectionMessage) ois.readObject();
            System.out.println(o.getClass().getName());
            System.out.println(o.getPublicKey());
            System.out.println(o.getUsername());
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static void writeConnectionMessage(Socket socket,ConnectionMessage msg) throws IOException
    {
        // pack ConnectionMessage to byte array
        byte[] bytes = msg.toByteArray();

        // write bytes to socket
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }
      
    private static SimpleMessage readSimpleMessage(Socket socket) throws IOException
    {
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                
   
        byte[] messagelengthbuf = new byte[4];
        inputStream.read(messagelengthbuf,0,4);
        int messagelength = java.nio.ByteBuffer.wrap(messagelengthbuf).getInt();
  
        byte[] messagebuf = new byte[messagelength];
        inputStream.read(messagebuf,0,messagelength);
        String message = new String(messagebuf, "US-ASCII");

        return new SimpleMessage(message);
    }
    
    private static void writeSimpleMessage(Socket socket,SimpleMessage msg) throws IOException
    {
        // pack ConnectionMessage to byte array
        byte[] bytes = msg.toByteArray();

        // write bytes to socket
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }
    
    
    
}

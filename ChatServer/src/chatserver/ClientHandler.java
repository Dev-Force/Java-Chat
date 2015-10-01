package chatserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.ConnectionMessage;
import messages.SimpleMessage;



public class ClientHandler extends Thread
{
    private Socket socket;
    private String username;

    
    
    public ClientHandler() 
    {
        this.socket = null;
        this.username = null;
    }
    
    public ClientHandler(Socket socket, String username) 
    {
        this.socket = socket;
        this.username = username;
    }
    
    
    
    public Socket getSocket() 
    {
        return socket;
    }

    public void setSocket(Socket socket) 
    {
        this.socket = socket;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    
    
    
    
    
    @Override
    public void run() 
    {

 
        try 
        {
            // read input from socket and process it
            while(true) 
            {
                // read SimpleMessage from socket
                SimpleMessage msg = readSimpleMessage(socket);
                
                synchronized(chatserver.ChatServer.getClients())
                {
                    // send message to all clients except the one that send it
                    for(ClientHandler ct : chatserver.ChatServer.getClients())
                    {
                        // don't send message to the client that has sent the message
                        if(ct.getSocket() == socket)
                            continue;
                        
                        SimpleMessage outputmsg = new SimpleMessage(username + ": " + msg.getMessage());
                        
                        // send message to all clients
                        System.out.println(username + ": " + msg.getMessage());
                        writeSimpleMessage(ct.getSocket(),outputmsg);
                    }
                }                  
            }
        } 
        catch (IOException ex) 
        {
            //remove ClientThread from List
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            chatserver.ChatServer.getClients().remove(this);
        }
        
    }
    
    
    
    
    
        
    private static ConnectionMessage readConnectionMessage(Socket socket) throws IOException
    {
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                
   
        byte[] usernamelengthbuf = new byte[4];
        inputStream.read(usernamelengthbuf,0,4);
        int usernamelength = java.nio.ByteBuffer.wrap(usernamelengthbuf).getInt();
                
        byte[] publickeylengthbuf = new byte[4];
        inputStream.read(publickeylengthbuf,0,4);
        int publickeylength = java.nio.ByteBuffer.wrap(publickeylengthbuf).getInt();
                
        byte[] usernamebuf = new byte[usernamelength];
        inputStream.read(usernamebuf,0,usernamelength);
        String username = new String(usernamebuf, "US-ASCII");
                
        byte[] publickeybuf = new byte[publickeylength];
        inputStream.read(publickeybuf,0,publickeylength);
                
        return new ConnectionMessage(username,publickeybuf);
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

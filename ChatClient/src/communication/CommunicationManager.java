package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import messages.ConnectionMessage;
import messages.SimpleMessage;



public class CommunicationManager 
{
    Socket socket;
    
    
    
    public CommunicationManager(Socket socket)
    {
        this.socket = socket;
    }
    
    
            
    public ConnectionMessage readConnectionMessage() throws IOException
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
    
    public void writeConnectionMessage(ConnectionMessage msg) throws IOException
    {
        // pack ConnectionMessage to byte array
        byte[] bytes = msg.toByteArray();

        // write bytes to socket
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }
      
    public SimpleMessage readSimpleMessage() throws IOException
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
    
    public void writeSimpleMessage(SimpleMessage msg) throws IOException
    {
        // pack ConnectionMessage to byte array
        byte[] bytes = msg.toByteArray();

        // write bytes to socket
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }
    
    public Socket getSocket()
    {
        return socket;
    }
    
    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }
    
    
    
}

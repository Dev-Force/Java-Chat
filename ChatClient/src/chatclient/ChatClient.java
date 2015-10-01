package chatclient;

import encryption.EncryptionManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Scanner;
import messages.ConnectionMessage;
import messages.SimpleMessage;



public class ChatClient extends Thread
{
    private String username;
    private String host;
    private int port;
    private KeyPair keys;
    
    
    
    public ChatClient(String username, String host, int port) 
    {
        this.username = username;
        this.host = host;
        this.port = port;
        
        // generate public and private rsa keys
        EncryptionManager cryptmanager = new EncryptionManager();
        keys = cryptmanager.generateKeys();
    }
    
    
    
    @Override
    public void run() 
    {
        try
        {
            // create new Scanner to read input from keyboard
            Scanner sc = new Scanner(System.in);

            // create new socket
            Socket socket = new Socket(host, port);

            // create byte array from ConnectionMessage
            ConnectionMessage connectionmsg = new ConnectionMessage(username,keys.getPublic().getEncoded()); 
            writeConnectionMessage(socket,connectionmsg);

            // create new thread and take care of reading input from socket
            new Thread() 
            {
                @Override
                public void run() 
                {
                    try
                    {
                        while(true) 
                        {
                            SimpleMessage msg = readSimpleMessage(socket);
                            System.out.println(msg.getMessage());
                        }
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.getStackTrace());
                    }
                    System.exit(0);
                }
            }.start();

            // read input from keyboard and write to socket
            while(true) 
            {
                // read message from keyboard
                String string = sc.nextLine();
                
                // write SimpleMessage to socket
                SimpleMessage msg = new SimpleMessage(string);
                writeSimpleMessage(socket,msg);
            }
        }
        catch(Exception ex)
        {
        }
        
        
    }   

    
    
        
    private ConnectionMessage readConnectionMessage(Socket socket) throws IOException
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
    
    private void writeConnectionMessage(Socket socket,ConnectionMessage msg) throws IOException
    {
        // pack ConnectionMessage to byte array
        byte[] bytes = msg.toByteArray();

        // write bytes to socket
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }
      
    private SimpleMessage readSimpleMessage(Socket socket) throws IOException
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
    
    private void writeSimpleMessage(Socket socket,SimpleMessage msg) throws IOException
    {
        // pack ConnectionMessage to byte array
        byte[] bytes = msg.toByteArray();

        // write bytes to socket
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
    }
    
      
    
    
    
    
}

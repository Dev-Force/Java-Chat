package chatclient;

import communication.CommunicationManager;
import encryption.EncryptionManager;
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
    private CommunicationManager commanager;
    
    
    
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
            
            // create new CommunicationManager for this socket
            commanager = new CommunicationManager(socket);

            // create byte array from ConnectionMessage
            ConnectionMessage connectionmsg = new ConnectionMessage(username,keys.getPublic().getEncoded()); 
            commanager.writeConnectionMessage(connectionmsg);

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
                            SimpleMessage msg = commanager.readSimpleMessage();
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
                commanager.writeSimpleMessage(msg);
            }
        }
        catch(Exception ex)
        {
        }
        
        
    }   

    
}

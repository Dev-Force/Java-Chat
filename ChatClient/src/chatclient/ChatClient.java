package chatclient;

import communication.CommunicationManager;
import encryption.EncryptionManager;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import messages.ConnectionMessage;
import messages.SimpleMessage;



public class ChatClient extends Thread
{
    private String username;
    private String host;
    private int port;
    private KeyPair keys;
    private PublicKey serverpublickey;
    private CommunicationManager commanager;
    private EncryptionManager cryptmanager;
    
    
    public ChatClient(String username, String host, int port) 
    {
        this.username = username;
        this.host = host;
        this.port = port;
        
        // generate public and private rsa keys
        cryptmanager = new EncryptionManager();
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

            // create byte array from ConnectionMessage and write it to socket
            ConnectionMessage connectionmsg = new ConnectionMessage(username,keys.getPublic().getEncoded()); 
            commanager.writeConnectionMessage(connectionmsg);

            // read ConnectionMessage from socket and extract server's RSA public key
            // this key will be used to encrypt messages sent to server
            connectionmsg = commanager.readConnectionMessage();
            byte[] publickeybytes = connectionmsg.getPublicKey();
            serverpublickey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publickeybytes));
            
            
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
                            // read SimpleMessage from socket
                            SimpleMessage encryptedmsg = commanager.readSimpleMessage();
                
                            // decrypt EncryptedSimpleMessage using client's RSA private key
                            String msg = cryptmanager.decrypt(encryptedmsg.getMessage(), keys.getPrivate());
 
                            // write message to stdout
                            System.out.println(msg);
                        }
                    }
                    catch (IOException ex)
                    {
                        System.out.println("There is a problem with the Server.");
                        System.out.println("Please try again later.");
                    }
                    System.exit(0);
                }
            }.start();

            
            // read input from keyboard and write to socket
            while(true) 
            {
                // read message from keyboard
                String textmessage = sc.nextLine();
                
                // encrypt message using server's RSA public key
                byte[] msgbytes = cryptmanager.encrypt(textmessage, serverpublickey);
                
                // write SimpleMessage to socket
                SimpleMessage msg = new SimpleMessage(msgbytes);
                commanager.writeSimpleMessage(msg);
            }
  
        }
        catch(Exception ex)
        {
        }
        
        
    }   

    
}

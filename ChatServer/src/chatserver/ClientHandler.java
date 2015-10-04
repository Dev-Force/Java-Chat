package chatserver;

import communication.CommunicationManager;
import encryption.EncryptionManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.SimpleMessage;



public class ClientHandler extends Thread
{
    private final String[] COMMANDS = 
    {
        "!getOnline",
        "!listCommands",
    };
    
    private Socket socket;
    private String username;
    private CommunicationManager commanager;
    private EncryptionManager cryptmanager;
    private PrivateKey serverprivatekey;
    private PublicKey clientpublickey;

    
    
    public ClientHandler(Socket socket, String username, PublicKey clientpublickey, PrivateKey serverprivatekey) 
    {
        this.socket = socket;
        this.username = username;
        this.commanager = new CommunicationManager(socket);
        this.clientpublickey = clientpublickey;
        this.serverprivatekey = serverprivatekey;
        this.cryptmanager = new EncryptionManager();       
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
    
    public PublicKey getPublicKey()
    {
        return clientpublickey;
    }
    
    private void sendToAll(String message) throws IOException
    {
        synchronized(chatserver.ChatServer.getClients()) 
        {
            if (!chatserver.ChatServer.getClients().isEmpty()) 
            {
                // send message to all clients except the one that send it
                for(ClientHandler ct : chatserver.ChatServer.getClients())
                {
                    // don't send message to the client that has sent the message
                    if(ct.getSocket() == socket)
                        continue;

                    
                    // encrypt message using client's RSA public key
                    byte[] msgbytes = cryptmanager.encrypt(username + ": " + message, ct.getPublicKey());

                    // write SimpleMessage to socket
                    SimpleMessage msg = new SimpleMessage(msgbytes);
                    
                    // send message to client
                    System.out.println(username + ": " + message);
                    (new CommunicationManager(ct.getSocket())).writeSimpleMessage(msg);
                }
            }
        }
    }
    
//    private void logoutNotification() 
//    {
//        System.out.println("User \"" + this.username + "\" logged out" );
//        
//        if (!chatserver.ChatServer.getClients().isEmpty()) 
//        {
//            for (ClientHandler ct : chatserver.ChatServer.getClients())
//            {
//                try {
//                    (new CommunicationManager(ct.getSocket())).writeSimpleMessage(new SimpleMessage("User \"" + this.username + "\" logged out"));
//                } catch (IOException ex1) {
//                    // Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
//                    if(ct.getSocket() == socket)
//                        continue;
//                    System.out.println("Couldnt send notification to : " + ct.getUsername());
//                }
//            }
//        }
//    }
    
    private boolean isCommand(String message) 
    {
        try {
            // in this block we add the implementations of all the commands 
            // which are defined as final at the attributes sections of this class
            
            for (String c : COMMANDS)
            {
                if (c.equals(message))
                {
                    //removes the '!' from the start and calls the method dynamically
                    ClientHandler.class.getDeclaredMethod(c.substring(1)).invoke(this);
                    
                    return true;
                }
            }
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
//    private void getOnline() throws IOException 
//    {
//        String message = "\n";
//        message += "People Online: \n";
//        for(ClientHandler ct : chatserver.ChatServer.getClients()) 
//        {
//            message += ct.getUsername() + "\n";
//        }
//        message += "\n";
//
//        this.commanager.writeSimpleMessage(new SimpleMessage(message));
//        
//    }
//    
//    private void listCommands() throws IOException
//    {
//        String message = "\n";
//        message += "Available Commands: \n";
//        
//        for (String s : COMMANDS)
//        {
//            message += s + "\n";
//        }
//        message += "\n";
//        this.commanager.writeSimpleMessage(new SimpleMessage(message));
//    }
    
    @Override
    public void run() 
    {

 
        try 
        {
            boolean command;
            // read input from socket and process it
            while(true) 
            {
                // read SimpleMessage from socket
                SimpleMessage encryptedmsg = commanager.readSimpleMessage();
                
                // decrypt EncryptedSimpleMessage using server's RSA private key
                String msg = cryptmanager.decrypt(encryptedmsg.getMessage(), serverprivatekey);
                
                // command handling
                command = isCommand(msg);
                
                if(!command)
                {
                    this.sendToAll(msg);
                }
            }
        } 
        catch (IOException ex) 
        {
            //this.logoutNotification();
            
            // print the error (remove comment if you want to show it)
            //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            //remove ClientThread from List
            chatserver.ChatServer.getClients().remove(this);
        }
        
    }
    

    
}

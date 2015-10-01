package chatserver;

import communication.CommunicationManager;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.SimpleMessage;



public class ClientHandler extends Thread
{
    private final String PEOPLE_ONLINE = "!online";
    
    private Socket socket;
    private String username;
    private CommunicationManager commanager;

    
    
    public ClientHandler(Socket socket,String username) 
    {
        this.socket = socket;
        this.username = username;
        this.commanager = new CommunicationManager(socket);
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
    
    public void sendToAll(SimpleMessage msg) throws IOException
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

                    SimpleMessage outputmsg = new SimpleMessage(username + ": " + msg.getMessage());

                    // send message to all clients
                    System.out.println(username + ": " + msg.getMessage());
                    (new CommunicationManager(ct.getSocket())).writeSimpleMessage(outputmsg);
                }
            }
        }
    }
    
    public void logoutNotification() 
    {
        System.out.println("User \"" + this.username + "\" logged out" );
        
        if (!chatserver.ChatServer.getClients().isEmpty()) 
        {
            for (ClientHandler ct : chatserver.ChatServer.getClients())
            {
                try {
                    (new CommunicationManager(ct.getSocket())).writeSimpleMessage(new SimpleMessage("User \"" + this.username + "\" logged out"));
                } catch (IOException ex1) {
                    // Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    if(ct.getSocket() == socket)
                        continue;
                    System.out.println("Couldnt send notification to : " + ct.getUsername());
                }
            }
        }
    }
    
    public boolean isCommand(SimpleMessage msg) 
    {
        try {
            // in this block we add the implementations of all the commands 
            // which are defined as final at the attributes sections of this class
            
            String message;
            
            //People Online 
            if(msg.getMessage().equals(PEOPLE_ONLINE))
            {
                message = "People Online: \n";
                for(ClientHandler ct : chatserver.ChatServer.getClients()) 
                {
                    message += ct.getUsername() + "\n";
                }
                message += "\n";
                
                this.commanager.writeSimpleMessage(new SimpleMessage(message));
            }
            
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
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
                SimpleMessage msg = commanager.readSimpleMessage();
                
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
            this.logoutNotification();
            
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

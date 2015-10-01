package chatserver;

import communication.CommunicationManager;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.SimpleMessage;



public class ClientHandler extends Thread
{
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


    
    @Override
    public void run() 
    {

 
        try 
        {
            // read input from socket and process it
            while(true) 
            {
                // read SimpleMessage from socket
                SimpleMessage msg = commanager.readSimpleMessage();
                
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
                        (new CommunicationManager(ct.getSocket())).writeSimpleMessage(outputmsg);
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
    

    
}

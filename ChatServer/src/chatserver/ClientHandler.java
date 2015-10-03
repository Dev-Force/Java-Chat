package chatserver;

import communication.CommunicationManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Message;
import messages.MessageFactory;
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
    private MessageFactory mf;

    
    
    public ClientHandler(Socket socket,String username) 
    {
        this.socket = socket;
        this.username = username;
        this.commanager = new CommunicationManager(socket);
        this.mf = new MessageFactory();
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
    
    private void sendToAll(Message msg) throws IOException
    {
        if(msg == null)
        {
            return;
        }
        synchronized(chatserver.ChatServer.getClients()) 
        {
            if (!chatserver.ChatServer.getClients().isEmpty()) 
            {
                // send message to all clients except the one that send it
                for(ClientHandler ct : chatserver.ChatServer.getClients())
                {
                    // don't send message to the client that has sent the message
//                    if(ct.getSocket() == socket)
//                        continue;

//                    SimpleMessage outputmsg = new SimpleMessage(username + ": " + msg.getMessage());
                    Message outputmsg = mf.makeSimpleMesage(username + ": " + msg.getMessage());

                    // send message to all clients
                    System.out.println(username + ": " + msg.getMessage());
                    (new CommunicationManager(ct.getSocket())).writeMessage(outputmsg);
                }
            }
        }
    }
    
    private void logoutNotification() 
    {
        System.out.println("User \"" + this.username + "\" logged out" );
        
        if (!chatserver.ChatServer.getClients().isEmpty()) 
        {
            for (ClientHandler ct : chatserver.ChatServer.getClients())
            {
                if(ct.getSocket() == socket)
                        continue;
                
                try {
                    (new CommunicationManager(ct.getSocket())).writeMessage(new SimpleMessage("User \"" + this.username + "\" logged out"));
                } catch (IOException ex1) {
                    // Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    System.out.println("Couldnt send notification to : " + ct.getUsername());
                }
            }
        }
    }
    
    private boolean isCommand(Message msg) 
    {
        try {
            // in this block we add the implementations of all the commands 
            // which are defined as final at the attributes sections of this class
            
            String message;
            message = "\n";
            
            if(msg == null) 
            {
                return false;
            }
            
            for (String c : COMMANDS)
            {
                if (c.equals( msg.getMessage()) )
                {
                    //removes the '!' from the start and calls the method dynamically
                    Method method = ClientHandler.class.getDeclaredMethod(c.substring(1));
                    method.setAccessible(true);
                    method.invoke(this);
                    
                    return true;
                }
            }
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void getOnline() throws IOException 
    {
        String message = "\n";
        message += "People Online: \n";
        for(ClientHandler ct : chatserver.ChatServer.getClients()) 
        {
            message += ct.getUsername() + "\n";
        }
        message += "\n";

//        this.commanager.writeMessage(new SimpleMessage(message));
        Message msg = mf.makeSimpleMesage(message);
        this.commanager.writeMessage(msg);
        
    }
    
    private void listCommands() throws IOException
    {
        String message = "\n";
        message += "Available Commands: \n";
        
        for (String s : COMMANDS)
        {
            message += s + "\n";
        }
        message += "\n";
        this.commanager.writeMessage(new SimpleMessage(message));
    }
    
    @Override
    public void run() 
    {

 
        try 
        {
            boolean command;
            MessageFactory mf = new MessageFactory();
            // read input from socket and process it
            while(true) 
            {
                // read SimpleMessage from socket
                Message msg = commanager.readMessage("SimpleMessage");
                
//                System.out.println(msg.getMessage());
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

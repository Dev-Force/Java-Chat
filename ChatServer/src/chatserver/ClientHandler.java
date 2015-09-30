package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



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
        BufferedReader in = null;
        PrintWriter out = null;
        String s;
        
        try 
        {
            // create new BufferedReader and associate it with socket's input stream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // read input from socket and process it
            while(!(s = in.readLine()).equals("q!")) 
            {
                synchronized(chatserver.ChatServer.getClients()) 
                {
                    
                    // send message to all clients except the one that send it
                    for(ClientHandler ct : chatserver.ChatServer.getClients()) 
                    {
                        // don't send message to the client that has sent the message
                        if(ct.getSocket() == socket) 
                            continue;

                        // create new PrintWriter and associate it with socket's outpout stream
                        out = new PrintWriter(ct.getSocket().getOutputStream(), true);
                        
                        // send message to all clients
                        System.out.println(username + ": " + s);
                        out.println(username + ": " + s);
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
            try 
            {
                in.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

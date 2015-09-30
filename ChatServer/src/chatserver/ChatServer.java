package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ChatServer 
{
    private static List<ClientHandler> Clients;
    private static ServerSocket ServSock;


    
    public static void main(String[] args) 
    {
        //Database db;
        Clients = new ArrayList<ClientHandler>();
        int port;
        String ip;
        

        // check if given arguments are correct
        if(args.length != 2)
        {
            System.out.println("Usage: java -jar <jarfile> <server ip> <port>");
            System.exit(0);
        }
        
        try 
        {
            ip = args[0];
            port = Integer.parseInt(args[1]);
                
            // create new Database object
            // if database does not exist, create it
            //db = new Database();
        
            // setup connection listener
            setupListener(ip, port);
        } 
        catch (NumberFormatException e) 
        {
            System.err.println("Argument" + args[1] + " must be an integer.");
            System.exit(1);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected static List<ClientHandler> getClients() 
    {
        return Clients;
    }

    protected static void setClients(List<ClientHandler> Clients) 
    {
        ChatServer.Clients = Clients;
    }
    
    private synchronized static void setupListener(String ip, int port) throws IOException 
    {
        
        
        
        // setup socket to accept connections
        ServSock = new ServerSocket(port,50,InetAddress.getByName(ip));
        System.out.println(ServSock.getInetAddress());
        
        // accept connections and service clients
        while(true) 
        {
            // accept new connection
            Socket socket = ServSock.accept();
            System.out.println("client with ip " + socket.getInetAddress().getHostAddress());
            
            // create new ClientThread to take care of client
            ClientHandler client = new ClientHandler();
            client.setSocket(socket);
            
            // create new BufferedReader and associate it with socket's input stream
            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // read username from socket
            String username = bf.readLine();
            System.out.println("Username: " + username);
            
            // set client's username and
            client.setUsername(username);
            Clients.add(client);
            
            // start client thread
            client.start();
        }
        
        
        
    }
    
    
    
}

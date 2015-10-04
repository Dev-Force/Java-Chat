package chatserver;

import communication.CommunicationManager;
import encryption.EncryptionManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.ConnectionMessage;



public class ChatServer 
{
    private static List<ClientHandler> Clients;
    private static ServerSocket ServSock;
    private static EncryptionManager cryptmanager;
    private static KeyPair keys;


    
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
                
            try {
                // create new Database object
                // if database does not exist, create it
                //db = new Database();
                
                // setup connection listener
                setupListener(ip, port);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }

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
    
    private synchronized static void setupListener(String ip, int port) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {  
        // create new EncryptionManager and generate rsa keys
        cryptmanager = new EncryptionManager();
        keys = cryptmanager.generateKeys();
 
        // setup socket to accept connections
        ServSock = new ServerSocket(port,50,InetAddress.getByName(ip));
        System.out.println(ServSock.getInetAddress());
        
        // accept connections and service clients
        while(true) 
        {
            // accept new connection
            Socket socket = ServSock.accept();
            System.out.println("client with ip " + socket.getInetAddress().getHostAddress());
            
            // read ConnectionMessage from socket
            CommunicationManager commanager = new CommunicationManager(socket);
            ConnectionMessage connectionmsg = commanager.readConnectionMessage();
            String username = connectionmsg.getUsername();
            byte[] publickeybytes = connectionmsg.getPublicKey();
            PublicKey publickey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publickeybytes));

            
            // write ConnectionMessage to socket in order to send server's RSA public key
            connectionmsg = new ConnectionMessage("",keys.getPublic().getEncoded());
            commanager.writeConnectionMessage(connectionmsg);

            // create new ClientThread to handle client connection and start thread
            ClientHandler client = new ClientHandler(socket, username, publickey, keys.getPrivate());
            Clients.add(client);
            client.start();
        } 
    }
    

    
}
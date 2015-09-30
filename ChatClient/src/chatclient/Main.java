package chatclient;

import java.io.IOException;



public class Main 
{
    public static void main(String[] args) 
    {
        
        
        // check if given arguments are correct
        if(args.length != 3)
        {
            System.out.println("Usage: java -jar <jarfile> <username> <host> <port>");
            System.exit(0);
        }
            

        try 
        {
            String username = args[0];
            String host = args[1]; 
            int port = Integer.parseInt(args[2]);
                
            // create a new client thread
            ChatClient client = new ChatClient(username, host, port);
            client.start();
        } 
        catch(NumberFormatException e) 
        {
            System.err.println("Argument" + args[2] + " must be an integer.");
            System.exit(1);
        } 
        
        
        
    }
}

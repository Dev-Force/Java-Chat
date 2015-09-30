package chatclient;

import encryption.EncryptionManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Scanner;



public class ChatClient extends Thread
{
    private String username;
    private String host;
    private int port;
    private KeyPair keys;
    
    
    
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

            // create new BufferedReader and associate it with socket's input stream
            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // create new PrintWriter and associate it with socket's outpout stream
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            // give server the required username
            pw.println(username);

            // create new thread and take care of reading input from socket
            new Thread() 
            {
                @Override
                public void run() 
                {
                    System.out.println("To quit press \"!q\" ");
                    try 
                    {
                        String s;
                        do 
                        {
                            // while there is input from socket's input stream
                            // read string and print it to stdout
                            if((s = bf.readLine())!= null)
                                System.out.println(s);

                        } while(s != null);
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
                String string = sc.nextLine();
                pw.println(string);
            }
        }
        catch(Exception ex)
        {
        }
        
    }   

    
    
}

package messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;



public class ConnectionMessage
{
    private String username;
    private byte[] publickey;
    
    
    
    public ConnectionMessage(String username,byte[] publickey)
    {
        this.username = username;
        this.publickey = publickey;
    }
    
    
    private ConnectionMessage readConnectionMessage(Socket socket) throws IOException, ClassNotFoundException
    {
//        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//                
//   
//        byte[] usernamelengthbuf = new byte[4];
//        inputStream.read(usernamelengthbuf,0,4);
//        int usernamelength = java.nio.ByteBuffer.wrap(usernamelengthbuf).getInt();
//                
//        byte[] publickeylengthbuf = new byte[4];
//        inputStream.read(publickeylengthbuf,0,4);
//        int publickeylength = java.nio.ByteBuffer.wrap(publickeylengthbuf).getInt();
//                
//        byte[] usernamebuf = new byte[usernamelength];
//        inputStream.read(usernamebuf,0,usernamelength);
//        String username = new String(usernamebuf, "US-ASCII");
//                
//        byte[] publickeybuf = new byte[publickeylength];
//        inputStream.read(publickeybuf,0,publickeylength);
//                
//        return new ConnectionMessage(username,publickeybuf);
        
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object o = ois.readObject();
        System.out.println(o.getClass().getName());
        return null;
    }

    
    
    /**
     * 
     * Pack this ConnectionMessage to byte array.
     * 
     * @param msg
     * @return 
     */
    public byte[] toByteArray()
    {
        byte[] usernamebytes = this.getUsername().getBytes(Charset.forName("UTF-8"));
        byte[] publickeybytes = this.getPublicKey();
        byte[] usernamelengthbytes = ByteBuffer.allocate(4).putInt(usernamebytes.length).array(); 
        byte[] publickeylengthbytes = ByteBuffer.allocate(4).putInt(publickeybytes.length).array(); 
        byte[] bytes = new byte[usernamelengthbytes.length+publickeylengthbytes.length+usernamebytes.length+publickeybytes.length];

        System.arraycopy(usernamelengthbytes,0,bytes,0,usernamelengthbytes.length);
        System.arraycopy(publickeylengthbytes,0,bytes,usernamelengthbytes.length,publickeylengthbytes.length);
        System.arraycopy(usernamebytes,0,bytes,usernamelengthbytes.length+publickeylengthbytes.length,usernamebytes.length);
        System.arraycopy(publickeybytes,0,bytes,usernamelengthbytes.length+publickeylengthbytes.length+usernamebytes.length,publickeybytes.length);
 
        return bytes;
    }
    
    public String getUsername() 
    {
        return username;
    }

    public byte[] getPublicKey() 
    {
        return publickey;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public void setPublicKey(byte[] publickey) 
    {
        this.publickey = publickey;
    }


    
}
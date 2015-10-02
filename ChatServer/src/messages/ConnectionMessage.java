package messages;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;



public class ConnectionMessage implements Serializable
{
    private String username;
    private byte[] publickey;
    
    
    
    public ConnectionMessage(String username,byte[] publickey)
    {
        this.username = username;
        this.publickey = publickey;
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
        
        int offset = 0;
        System.arraycopy(usernamelengthbytes,0,bytes,offset,usernamelengthbytes.length);
        offset += usernamelengthbytes.length;
        System.arraycopy(publickeylengthbytes,0,bytes,offset,publickeylengthbytes.length);
        offset += publickeylengthbytes.length;
        System.arraycopy(usernamebytes,0,bytes,offset,usernamebytes.length);
        offset += usernamebytes.length;
        System.arraycopy(publickeybytes,0,bytes,offset,publickeybytes.length);
 
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
package messages;

import java.io.Serializable;


public class ConnectionMessage extends AbstractMessage implements Serializable, Message
{
    
    
    private String username;
 
    private byte[] publickey;
    
    
    public ConnectionMessage(String username,byte[] publickey)
    {
        this.username = username;
        this.publickey = publickey;
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

    @Override
    public String getMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
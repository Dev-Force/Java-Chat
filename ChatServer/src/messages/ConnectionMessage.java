package messages;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ConnectionMessage extends AbstractMessage implements Serializable
{
    private String username;
    private byte[] publickey;
    
    
    
    public ConnectionMessage(String username,byte[] publickey)
    {
        this.username = username;
        this.publickey = publickey;
    }

    
    

    @Override
    public byte[] toByteArray()
    {
        try {
            return toByteArray(username, publickey);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
            Logger.getLogger(SimpleMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
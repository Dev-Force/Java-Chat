package messages;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;



public class SimpleMessage extends AbstractMessage implements Serializable
{
    private byte[] message;
    
    
    
    public SimpleMessage(byte[] message)
    {
        this.message = message;
    }
    
    @Override
    public byte[] toByteArray()
    {
        try {
            return toByteArray(message);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
            Logger.getLogger(SimpleMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public byte[] getMessage()
    {
        return message;
    }
    
    public void setMessage(byte[] message)
    {
        this.message = message;
    }
    
}
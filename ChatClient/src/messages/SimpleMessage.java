package messages;

import java.io.Serializable;
import java.nio.ByteBuffer;



public class SimpleMessage implements Serializable 
{
    private byte[] message;
    
    
    
    public SimpleMessage(byte[] message)
    {
        this.message = message;
    }
    
    
    
    /**
     * 
     * Pack this EncryptedSimpleMessage to byte array.
     * 
     * @return 
     */
    public byte[] toByteArray()
    {
        byte[] messagebytes = this.getMessage();
        byte[] messagelengthbytes = ByteBuffer.allocate(4).putInt(messagebytes.length).array(); 
        byte[] bytes = new byte[messagelengthbytes.length+messagebytes.length];

        System.arraycopy(messagelengthbytes,0,bytes,0,messagelengthbytes.length);
        System.arraycopy(messagebytes,0,bytes,messagelengthbytes.length,messagebytes.length);
        
        return bytes;
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
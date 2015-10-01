package messages;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;



public class SimpleMessage implements Serializable 
{
    private String message;
    
    
    
    public SimpleMessage(String message)
    {
        this.message = message;
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
        byte[] usernamebytes = this.getMessage().getBytes(Charset.forName("UTF-8"));
        byte[] usernamelengthbytes = ByteBuffer.allocate(4).putInt(usernamebytes.length).array(); 
        byte[] bytes = new byte[usernamelengthbytes.length+usernamebytes.length];

        System.arraycopy(usernamelengthbytes,0,bytes,0,usernamelengthbytes.length);
        System.arraycopy(usernamebytes,0,bytes,usernamelengthbytes.length,usernamebytes.length);
        
        return bytes;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
}

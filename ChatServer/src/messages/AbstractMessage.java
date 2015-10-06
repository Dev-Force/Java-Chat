/*
 */
package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author Tolis
 */
public abstract class AbstractMessage {
    
    final Class[] AVAILABLE_TYPES = 
    {
//        int.class,
        String.class,
        byte[].class,
    };
    
    byte[] length;
    
    byte[] bytes;
    
    private final ByteArrayOutputStream baus_l = new ByteArrayOutputStream();
    
    private final ByteArrayOutputStream baus_b = new ByteArrayOutputStream();
    
    
    public abstract byte[] toByteArray();
    
    private void toByteArray(String s) throws IOException 
    {
        // get s bytes and s bytes length
        byte[] sBytes = s.getBytes(Charset.forName("UTF-8"));
        byte[] sLengthBytes = ByteBuffer.allocate(4).putInt(s.length()).array();
        
        //add s length bytes to stream
        baus_l.write(sLengthBytes);
        
        //add s bytes to stream
        baus_b.write(sBytes);
    }
    
    private void toByteArray(byte[] b) throws IOException
    {
        byte[] l = ByteBuffer.allocate(4).putInt(b.length).array();
        baus_l.write(l);
        baus_b.write(b);
    }
    
    protected byte[] toByteArray(Object... o) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        byte[] length;
        byte[] bytes;
        byte[] finalBytes;
        
        for (Object ob : o) // for every object in the parameter list
        {
            for (Class c : AVAILABLE_TYPES) //for every defined available type
            {
                // if classes are the same
                if (ob.getClass().equals(c)) 
                {
                    // find the method and then invoke it (casted the object as any type we want)
                    Method method = AbstractMessage.class.getDeclaredMethod("toByteArray", new Class[] {c});
                    method.setAccessible(true);
                    method.invoke(this, new Object[] {ob});
                    
                    break;
                }
            }
        }
        
        length = baus_l.toByteArray();
        bytes = baus_b.toByteArray();
        
        finalBytes = new byte[length.length + bytes.length];
        
        //combine arrays to one!
        System.arraycopy(length, 0, finalBytes, 0, length.length);
        System.arraycopy(bytes, 0, finalBytes, length.length, bytes.length);
        
        //close streams
        baus_l.close();
        baus_b.close();
        
        return finalBytes;

    }
}

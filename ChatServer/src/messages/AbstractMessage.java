
package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private final ByteArrayOutputStream baus_l = new ByteArrayOutputStream();
    
    private final ByteArrayOutputStream baus_b = new ByteArrayOutputStream();
    
    private void toByteArray(String s) throws IOException 
    {
        // get s bytes and s bytes length
        byte[] sBytes = s.getBytes(Charset.forName("UTF-8"));
        byte[] sLengthBytes = ByteBuffer.allocate(4).putInt(s.length()).array();
        
        //add s length bytes to stream
        baus_l.write(sLengthBytes);
        
        //add s bytes to stream
        baus_b.write(sBytes);
        
        System.out.println(sBytes.length);
    }
    
    private void toByteArray(byte[] b) throws IOException
    {
        baus_l.write(b.length);
        baus_b.write(b);
    }
    
    public byte[] toByteArray(Object... objects)
    {
        byte[] length;
        byte[] bytes;
        byte[] finalBytes;
        
        for (Object ob : objects) // for every object in the parameter list
        {
            for (Class c : AVAILABLE_TYPES) //for every defined available type
            {
                // if classes are the same
                if (ob.getClass().equals(c)) 
                {
                    try {
                        // find the method and then invoke it (casted the object as any type we want)
                        Method method = this.getClass().getDeclaredMethod("toByteArray", new Class[] {c});
                        method.setAccessible(true);
                        method.invoke(this, new Object[] {ob});
                        
                        baus_l.flush();
                        baus_b.flush();
                        break;
                    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
                        Logger.getLogger(AbstractMessage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        length = baus_l.toByteArray();
        bytes = baus_b.toByteArray();
        
        System.out.println(length.length);
        
        finalBytes = new byte[length.length + bytes.length];
        
        //combine arrays to one!
        System.arraycopy(length, 0, finalBytes, 0, length.length);
        System.arraycopy(bytes, 0, finalBytes, length.length, bytes.length);
        
        //close streams
        try {
            baus_l.close();
            baus_b.close();
        } catch (IOException ex) {
            Logger.getLogger(AbstractMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return finalBytes;

    }
}


package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
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
    
    private final ByteArrayOutputStream baos_l = new ByteArrayOutputStream();
    
    private final ByteArrayOutputStream baos_b = new ByteArrayOutputStream();
    
    private void toByteArray(String s)
    {
        
        try {
            // get s bytes and s bytes length
            byte[] sBytes = s.getBytes(Charset.forName("UTF-8"));
            byte[] sLengthBytes = ByteBuffer.allocate(4).putInt(s.length()).array();
            
            //add s length bytes to stream
            baos_l.write(sLengthBytes);
            
            //add s bytes to stream
            baos_b.write(sBytes);
        } catch (IOException ex) {
            Logger.getLogger(AbstractMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void toByteArray(byte[] b)
    {
        try {
            byte[] l = ByteBuffer.allocate(4).putInt(b.length).array();
            baos_l.write(l);
            baos_b.write(b);
        } catch (IOException ex) {
            Logger.getLogger(AbstractMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                        
//                        Method[] m = this.getClass().getSuperclass().getDeclaredMethods();
//                        for (int i = 0; i < m.length; i++)
//                        System.out.println(m[i].toString());
                        
                        // find the method and then invoke it (casted the object as any type we want)
                        Method method = AbstractMessage.class.getDeclaredMethod("toByteArray", new Class[] {c});
                        method.setAccessible(true);
                        method.invoke(this, new Object[] {ob});
                        
                        baos_l.flush();
                        baos_b.flush();
                        break;
                    } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException | NoSuchMethodException ex) {
                        Logger.getLogger(AbstractMessage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        length = baos_l.toByteArray();
        bytes = baos_b.toByteArray();
        
        finalBytes = new byte[length.length + bytes.length];
        
        //combine arrays to one!
        System.arraycopy(length, 0, finalBytes, 0, length.length);
        System.arraycopy(bytes, 0, finalBytes, length.length, bytes.length);
        
        //close streams
        try {
            baos_l.close();
            baos_b.close();
        } catch (IOException ex) {
            Logger.getLogger(AbstractMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return finalBytes;

    }
}

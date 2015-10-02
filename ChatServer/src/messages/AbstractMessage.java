/*
 */
package messages;

import java.lang.reflect.Method;

/**
 *
 * @author Tolis
 */
public abstract class AbstractMessage {
    
    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }
    
    public byte[] toByteArray(Object... objects) throws ClassNotFoundException, Exception 
    {
        final Class[] AVAILABLE_TYPES = {
            Integer.class,
            String.class,
            Double.class,
        };
        
        for (Object ob : objects) {
//            System.out.println(ob.getClass().getSimpleName());
            
            for (Class c : AVAILABLE_TYPES) 
            {
                if (ob.getClass().getSimpleName().equals(c.getSimpleName())) {
//                    System.out.println(c.getClass());
                    System.out.println(convertInstanceOfObject(ob, c).getClass());
//                    test(convertInstanceOfObject(ob, c) + "ffg");
                    Method method = this.getClass().getMethod("toByteArray", new Class[] {c});
                    System.out.println(method.invoke(this, new Object[] {convertInstanceOfObject(ob, c)}));
                    
                }
            }
        } 
        
                
        return null;
    }
    
    public byte[] toByteArray(String s) 
    {
       
        return null;
    }
    
    public byte[] toByteArray(int i)
    {
        
        return null;
    }
    
    public byte[] toByteArray(byte[] b)
    {
        return null;
    }
}

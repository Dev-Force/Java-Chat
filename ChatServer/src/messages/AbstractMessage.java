/*
 */
package messages;

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
        
        Class<?> cls;
        Object temp;
        for (Object o : objects) {
//            System.out.println(ob.getClass().getSimpleName());
            
            for (Class c : AVAILABLE_TYPES) 
            {
                if (o.getClass().getSimpleName().equals(c.getSimpleName())) {
                    System.out.println(convertInstanceOfObject(o, c.getName().getClass()));
                    toByteArray(convertInstanceOfObject(o, c.getName().getClass()) + "\n");
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

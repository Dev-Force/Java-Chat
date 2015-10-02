/*
 */
package messages;

/**
 *
 * @author Tolis
 */
public abstract class AbstractMessage {
    
    public byte[] toByteArray(Object... objects) throws ClassNotFoundException, Exception 
    {
        final String[] types = { 
            "String",
            "Integer",
            "byte[]",
        };
        
        int offset = 0;
        int size = 0;
        byte[] bytes;
        Class c;
        
        for (Object o : objects)
        {
            for (String s : types) 
            {
                
                if(!s.equals(o.getClass().getSimpleName()))
                {
                    throw new Exception("Value " + o.getClass().getSimpleName() + " not accepted");
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

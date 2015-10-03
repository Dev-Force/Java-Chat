
package messages;


public interface Message {
    

    public byte[] toByteArray(Object... objects);
    
    public String getUsername();

    public byte[] getPublicKey();

    public void setUsername(String username);

    public void setPublicKey(byte[] publickey); 
    
    public String getMessage();
    
    public void setMessage(String message);
    
    
}

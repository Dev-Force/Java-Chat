
package messages;

public class MessageFactory 
{
    
    public Message makeSimpleMesage(String message) 
    {
        return new SimpleMessage(message);
    }
    
    public Message makeConnectionMessage(String username, byte[] publickey) 
    {
        return new ConnectionMessage(username, publickey);
    }
}

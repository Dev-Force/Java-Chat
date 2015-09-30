package encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;



public class EncryptionManager 
{

 
    
    /**
     * 
     * Generate RSA public and private keys.
     * 
     * @return KeyPair
     */
    public KeyPair generateKeys()
    {
        KeyPair keypair = null;
        
        
        
        try 
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            keypair = keyGen.generateKeyPair();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return keypair;
    }

    /**
     * 
     * Encrypt plain text using the public key.
     * 
     * @param text
     * @param publicKey
     * @return 
     */
    public byte[] encrypt(String text,PublicKey publicKey) 
    {
        byte[] ciphertext = null;
      
      
      
        try 
        {
          Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.ENCRYPT_MODE,publicKey);
          ciphertext = cipher.doFinal(text.getBytes());
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }

        return ciphertext;
    }
  
    /**
     * 
     * Decrypt cipher text using the private key.
     * 
     * @param text
     * @param privateKey
     * @return 
     */
    public String decrypt(byte[] text,PrivateKey privateKey) 
    {
      byte[] plaintext = null;
      
      
      
      try 
      {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        plaintext = cipher.doFinal(text);
      } 
      catch (Exception ex) 
      {
        ex.printStackTrace();
      }

      return new String(plaintext);
    }

  
 
}

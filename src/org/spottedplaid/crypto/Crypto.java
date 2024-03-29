package org.spottedplaid.crypto;

/**
 * This software has NO WARRANTY.  It is available �S-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * Crypto.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/* ***************************************************************
Class:    Crypto
Purpose:  Methods used for encryption/decryption of strings
***************************************************************  */
public class Crypto {

	
/// Class variables	
private static SecretKey lKey;
private Cipher ecipher;
private Cipher dcipher;

private String sKeyfile = "";
	
    /// Constructor
    public Crypto()
    {
    }
       
    /** 
     *  buildAlgorithm - Creates a working instance of encrypt/decrypt objects
     *   @param - String algorithm type
     *   @param - SecretKey  value
     * 
     */
	public void buildAlgorithm(String strAlg, SecretKey key) {
		try {
			  ecipher = Cipher.getInstance(strAlg);
			  dcipher = Cipher.getInstance(strAlg);
			  ecipher.init(Cipher.ENCRYPT_MODE, key);
			  dcipher.init(Cipher.DECRYPT_MODE, key);
			} catch (javax.crypto.NoSuchPaddingException e) {
			    System.out.println("EXCEPTION->DesEncrypter(1)-> " + e.getMessage());
			} catch (java.security.NoSuchAlgorithmException e) {
				System.out.println("EXCEPTION->DesEncrypter(2)-> " + e.getMessage());
			} catch (java.security.InvalidKeyException e) {
				System.out.println("EXCEPTION->DesEncrypter(3)-> " + e.getMessage());
			}
		}
    
	/** 
	 *  encrypt - Encrypts a string using the defined key
	 *   @param - String plain text
	 *   @return - Encrypted string
	 * 
	 */
	public String encrypt(String str) {
		try {
				// Encode the string into bytes using utf-8
			  byte[] utf8 = str.getBytes("UTF8");
    
				// Encrypt
			  byte[] enc = ecipher.doFinal(utf8);
                
			  return javax.xml.bind.DatatypeConverter.printBase64Binary(enc);
			     
			} catch (javax.crypto.BadPaddingException e) {
				System.out.println("EXCEPTION->encrypt Exception(1)-> " + e.getMessage());
			} catch (IllegalBlockSizeException e) {
				System.out.println("EXCEPTION->encrypt Exception(2)-> " + e.getMessage());
			} catch (UnsupportedEncodingException e) {
				System.out.println("EXCEPTION->encrypt Exception(3)-> " + e.getMessage());
			} 
		     return null;
		}
    
	/** 
	 *  decrypt - Decrypts a string using the defined key
	 *   @param - String encrypted value
	 *   @return - Plain text string
	 * 
	 */
	public String decrypt(String str) {
		try {
				// Decode base64 to get bytes
			  byte[] dec = javax.xml.bind.DatatypeConverter.parseBase64Binary(str);
    
				// Decrypt
			  if (dcipher==null)
			  {
				  System.out.println("Failed,decrypt, dcipher is null...wtf?!?");
				   return null;
			  }
			  byte[] utf8 = dcipher.doFinal(dec);
					
				// Decode using utf-8
				  return new String(utf8, "UTF8");
			} catch (javax.crypto.BadPaddingException e) {
				System.out.println("EXCEPTION->decrypt Exception(1)-> " + e.getMessage());
			} catch (IllegalBlockSizeException e) {
				System.out.println("EXCEPTION->decrypt Exception(2)-> " + e.getMessage());
			} catch (UnsupportedEncodingException e) {
				System.out.println("EXCEPTION->decrypt Exception(3)-> " + e.getMessage());
			} catch (Exception e) {
				  System.out.println("EXCEPTION->decrypt Exception(5)->Algorithms may not match-> " + e.getMessage());
		}
		return null;
	}

	/** 
	 *  verifyKey - Validates existence of key, creates if not found.  Returns a non-success message if keystore cannot be opened
	 *   @param - String filename
	 *   @param - String password/phrase
	 *   @param - String algorithm method
	 *   @return - String
	 *               "Success" or failure message
	 * 
	 */
	public String verifyKey(String _sFilename, String _sPass, String _sMethod)
	{
	   File fileName = new File(_sFilename);
	   if (fileName.exists())
	   {
	   if ((lKey=Keyutil.getKeyFromFile(_sFilename, _sPass))==null)
	   {
		   return "Keystore is corrupt, or wrong passcode provided";
	   }
	   else
	   {
		   buildAlgorithm(_sMethod,lKey);
		   return "Success";
	   }
	   }
	   else
	   {
		   SecretKey key = null;
		try {
			key = KeyGenerator.getInstance(_sMethod).generateKey();
			Keyutil.createKeyFile(_sFilename, key, _sPass);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("EXCEPTION->verifyKey, Exception [" + e.getMessage() + "]");
			 return "Failed to create key [" + e.getMessage() + "]";
		}
	       lKey = key;
		   if (key==null)
		   {
			   return "Failed to create key";
		   }
		   buildAlgorithm(_sMethod,lKey);
	   }
	   return "Success";
	   
	}
	
	/** 
	 *  setlKey - Set local class variable to the key value
	 *   @param - SecretKey value
	 * 
	 */
	public void setlKey(SecretKey _sKey)
	{
		lKey = _sKey;
	}
	
	/** 
	 *  getlKey - Return local variable containing the key value
	 *   @return - private SecretKey value lKey
	 * 
	 */
	public SecretKey getlKey()
	{
		return Crypto.lKey;
	}
			
	public void setKeyfile(String _sFile)
	{
		sKeyfile = _sFile;
	}
	
	public String getKeyfile()
	{
		return this.sKeyfile;
	}
}

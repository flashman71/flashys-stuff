package org.spottedplaid.crypto;

/**
 * This software has NO WARRANTY.  It is available �S-IS, use at your own risk.
 * 
 * @author gary
 * @version 1.0
 * 
 * Keyutil.java
 * (c) 2013 - Spotted Plaid Productions.
 * 
 * License - Can be copied, modified, and distributed with no fees and/or royalties.  If this is used it would be appreciated if
 *           credit were given, but it is not necessary.
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;

/* ***************************************************************
Class:    Keyutil
Purpose:  Utility functions for Keystore management
***************************************************************  */
public class Keyutil {

	/// Class variable for key type
	private final static String S_KEY_ALIAS = "KEY";
	
	/**
	 * getKeyFromFile - Gets key value from file, to be used for encryption/decryption 
	 * @param _sFilename - Filename of keystore
	 * @param _sPassword - Password/passphrase to open keystore
	 * @return - SecreKey object
	 */
	public static SecretKey getKeyFromFile(String _sFilename,String _sPassword)
	{
		SecretKey sk = null;
		File fKeyFile = new File(_sFilename);			
		
		 if (fKeyFile.exists())
		 {
			 try {
				KeyStore ks = KeyStore.getInstance("JCEKS");
			    ks.load(new FileInputStream(_sFilename), _sPassword.toCharArray());	
				sk = (SecretKey) ks.getKey(S_KEY_ALIAS, _sPassword.toCharArray());

			} catch (IOException e) {
				System.out.println("Keyutil, IOException [" + e.getMessage() + "]");
				 return null;
			} catch (KeyStoreException e) {
				System.out.println("Keyutil, KeyStoreException [" + e.getMessage() + "]");
				  return null;
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Keyutil, NoSuchAlgorithmException [" + e.getMessage() + "]");
				  return null;
			} catch (CertificateException e) {
				System.out.println("Keyutil, CertificateException [" + e.getMessage() + "]");
				  return null;
			} catch (UnrecoverableKeyException e) {
				System.out.println("Keyutil, UnrecoverableException [" + e.getMessage() + "]");
				  return null;
			}
			 
		 }
		 else
		 {
			 System.out.println("File [" + _sFilename + "] does not exist");
		 }
		 
		return sk;
	}
	
	/**
	 * createKeyFile - Creates a new keystore file
	 * @param _sFilename - Keystore filename
	 * @param _sKey - Key value
	 * @param _sPassword - Password/passphrase
	 * @return - 0 for success, < 0 for failures
	 */
	public static int createKeyFile(String _sFilename, SecretKey _sKey, String _sPassword)
	{
		KeyStore ks = null;
		FileOutputStream  fos = null;
		try {
	
			ks = KeyStore.getInstance("JCEKS");
			ks.load(null,_sPassword.toCharArray());
			File fin = new File(_sFilename);
			fin.createNewFile();
			fos = new java.io.FileOutputStream(_sFilename);      
              // Close the key store

		      // Store the secret key
			ks.setKeyEntry(S_KEY_ALIAS,_sKey,_sPassword.toCharArray(),null);
			ks.store(fos,_sPassword.toCharArray());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		  catch (KeyStoreException e) {
			System.out.println("createKeyFile KeyStoreException [" + e.getMessage() + "]");
			 return -1;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("createKeyFile NoSuchAlgorithmException [" + e.getMessage() + "]");
			 return -2;
		} catch (CertificateException e) {
			System.out.println("createKeyFile CertificateException [" + e.getMessage() + "]");
			 return -3;
		} catch (IOException e) {
			System.out.println("EXCEPTION->IOException [" + e.getMessage() + "]");
			 return -4;
		}
 finally {
	        if (fos != null) {
	            try {
					fos.close();
				} catch (IOException e) {
					System.out.println("createKeyFile IOException closing file [" + e.getMessage() + "]");
					 return -5;
				}
	        }
	    }
		
		return 0;
	}
	 
}
/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/3/2021

###################################

Functional Description: 
Encrypts a key.

###################################

Pseudocode:

Class Encrypt Key
	Private Members
		cipher
	Constructor
		Initialize cipher’s 
		algorithm
		EncryptFile(key)		
	EncryptFile()
		put cipher in encrypt mode
		writeToFile(encrypted key)
	WriteToFile()
		write encrypted key to file
	getFileInBytes()
		read file into byte array


###################################

Sources:
Marilena Panagiotidou

###################################

*/

// package
package encrypt;

// library imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

// class definition
public class EncryptKey 
{
	// private member cipher
	private Cipher cipher;
	
	// constructor
	public EncryptKey(PublicKey key, File originalKey, File encryptedKey, String cipherAlgorithm) throws IOException, GeneralSecurityException
	{
		// initialize cipher with algorithm
		this.cipher = Cipher.getInstance(cipherAlgorithm);
		
		// encrypt the file
		encryptFile(getFileInBytes(originalKey), encryptedKey, key);
	}
	
	// encrypt a file
	public void encryptFile(byte[] input, File output, PublicKey key) throws IOException, GeneralSecurityException 
	{
		// put cipher in encrypt mode
		this.cipher.init(Cipher.ENCRYPT_MODE, key);

		// create the encrypted file
		writeToFile(output, this.cipher.doFinal(input));
    }
	
	// create an encrypted file
	private void writeToFile(File output, byte[] toFile) throws IllegalBlockSizeException, BadPaddingException, IOException
	{
		output.getParentFile().mkdirs();

		// open file output stream
		FileOutputStream fos = new FileOutputStream(output);

		// write byte array to file
		fos.write(toFile);

		// clear the buffer
		fos.flush();

		// close the output stream
		fos.close();

		// confirmation
		System.out.println("The key was successfully encrypted and stored in: " + output.getPath());
	}
	
	// convert from file to bytes
	public byte[] getFileInBytes(File file) throws IOException
	{
		// open file input stream
		FileInputStream fileInput = new FileInputStream(file);

		// initialize byte array
		byte[] bytes = new byte[(int) file.length()];

		// read file into byte array
		fileInput.read(bytes);

		// close input stream
		fileInput.close();

		// return byte array
		return bytes;
	}
}

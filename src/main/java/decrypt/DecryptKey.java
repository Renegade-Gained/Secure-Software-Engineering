/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/3/2021

###################################

Functional Description: 
Decrypts a key that was used for
encryption.

###################################

Pseudocode:

Class DecryptKey
	Private Members
		cipher
	Constructor
		initialize cipher’s 
		algorithm
		decryptFile(encrypted key)
	DecryptFile()
		put cipher in decrypt mode
		writetofile(decrypted key)
	WriteToFile()
		write decrypted key to file
	GetFileInBytes()
		read a file into a byte 
		array


##################################

Sources:
Marilena Panagiotidou

###################################

*/

// package
package decrypt;

// library imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class DecryptKey 
{
	// private member cipher
	private Cipher cipher;

	// decrypt a key
	public DecryptKey(PrivateKey privateKey, File encryptedKeyReceived, File decryptedKeyFile, String algorithm) throws IOException, GeneralSecurityException 
	{
		// initialize cipher with algorithm
		this.cipher = Cipher.getInstance(algorithm);

		// decrypt key
		decryptFile(getFileInBytes(encryptedKeyReceived), decryptedKeyFile, privateKey);
	}
	
	// decrypt and write to file
	public void decryptFile(byte[] input, File output, PrivateKey key) throws IOException, GeneralSecurityException 
	{
		// put cipher in decrypt mode with key
		this.cipher.init(Cipher.DECRYPT_MODE, key);

		// write to file
		writeToFile(output, this.cipher.doFinal(input));
    }
	
	// write to a file
	private void writeToFile(File output, byte[] toWrite) throws IllegalBlockSizeException, BadPaddingException, IOException
	{
		output.getParentFile().mkdirs();

		// open file output stream with proided file
		FileOutputStream fos = new FileOutputStream(output);

		// write bytes to file
		fos.write(toWrite);

		// empty buffer
		fos.flush();

		// close output stream
		fos.close();
	}
	
	// get file as byte array
	public byte[] getFileInBytes(File f) throws IOException
	{
		// open file input stream
		FileInputStream fis = new FileInputStream(f);

		// initialize byte array
		byte[] fbytes = new byte[(int) f.length()];

		// read in file into byte array
		fis.read(fbytes);

		// close input stream
		fis.close();

		// return byte array
		return fbytes;
	}
}

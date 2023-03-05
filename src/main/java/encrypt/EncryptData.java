/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/3/2021

###################################

Functional Description: 
Encrypts a file.

###################################

Pseudocode:

Class EncryptData
	Private Members
		cipher
	Constructor
		initialize cipher’s algorithm
		encryptFile(file)
	EncryptFile()
		encrypt the specified file
	WriteToFile()
		write the file with encrypted 
		contents
	getFileInBytes()
		open a file and read into byte 
		array


##################################

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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptData 
{
	// private member cipher
	private Cipher cipher;
	
	// encrypt a file
	public EncryptData(File originalFile, File encrypted, SecretKeySpec secretKey, String cipherAlgorithm) throws IOException, GeneralSecurityException
	{
		// initialize cipher with an algorithm
		this.cipher = Cipher.getInstance(cipherAlgorithm);	
		
		// encrypt the file
		encryptFile(getFileInBytes(originalFile), encrypted, secretKey);
	}
	
	// encrypts a file
	public void encryptFile(byte[] input, File output, SecretKeySpec key) throws IOException, GeneralSecurityException 
	{
		// put the cipher in encrypt mode using provided key
		this.cipher.init(Cipher.ENCRYPT_MODE, key);

		// create the encrypted file
		writeToFile(output, this.cipher.doFinal(input));
    }
	
	// creates an encrypted file
	private void writeToFile(File output, byte[] toWrite) throws IllegalBlockSizeException, BadPaddingException, IOException
	{
		output.getParentFile().mkdirs();

		// open file output stream
		FileOutputStream fos = new FileOutputStream(output);

		// write byte array to output stream
		fos.write(toWrite);

		// clear the buffer
		fos.flush();

		// close the output stream
		fos.close();

		// confirmation
		System.out.println("The file was successfully encrypted and stored in: " + output.getPath());
	}
	
	// convert from file to byte array
	public byte[] getFileInBytes(File f) throws IOException
	{
		// open inputstream
		FileInputStream fis = new FileInputStream(f);

		// initialize byte array
		byte[] fbytes = new byte[(int) f.length()];

		// read file into byte array
		fis.read(fbytes);

		// close input stream
		fis.close();

		// return byte array
		return fbytes;
	}
}

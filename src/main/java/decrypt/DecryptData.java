/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/4/2021

###################################

Functional Description:
Decrypts files using saved keys.

###################################

Pseudocode:

Class DecryptData
	Private Members
		cipher
	Constructor
		initialize cipher’s 
		algorithm
		decryptFile(encrypted key)
	DecryptFile()
		put cipher in decrypt mode
		writetofile(decrypted data)
	WriteToFile()
write decrypted data to file
	GetFileInBytes()
read a file into a byte array


###################################

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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

// class definition
public class DecryptData 
{
	// private member cipher
	private Cipher cipher;

	// decrypt data
	public DecryptData(File encryptedFileReceived, File decryptedFile, SecretKeySpec secretKey, String algorithm) throws IOException, GeneralSecurityException 
	{
		// initialize the cipher
		this.cipher = Cipher.getInstance(algorithm);

		// decrypt file
		decryptFile(getFileInBytes(encryptedFileReceived), decryptedFile, secretKey);
	}
	
	// decrypt file
	public void decryptFile(byte[] input, File output, SecretKeySpec key) throws IOException, GeneralSecurityException 
	{
		// set cipher to decrypt mode using provided key
		this.cipher.init(Cipher.DECRYPT_MODE, key);

		// create decrypted file
		writeToFile(output, this.cipher.doFinal(input));
    }
	
	// create decrypted file
	private void writeToFile(File output, byte[] toWrite) throws IllegalBlockSizeException, BadPaddingException, IOException
	{
		output.getParentFile().mkdirs();

		// create file using provided name
		FileOutputStream fos = new FileOutputStream(output);

		// write data to file
		fos.write(toWrite);

		// clear stream
		fos.flush();

		// close stream
		fos.close();
		System.out.println("The file was successfully decrypted. You can view it in: " + output.getPath());
	}
	
	// convert from file to bytes
	public byte[] getFileInBytes(File f) throws IOException
	{
		// open file input stream with provided file
		FileInputStream fis = new FileInputStream(f);

		// create byte array
		byte[] fbytes = new byte[(int) f.length()];

		// read file into byte array
		fis.read(fbytes);

		// close file stream
		fis.close();

		// return byte array
		return fbytes;
	}

}

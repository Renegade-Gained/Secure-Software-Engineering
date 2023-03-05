/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/3/2021

###################################

Functional Description: 
Generates symmetric keys for
encryption and decryption.

###################################

Pseudocode:

Class Generate Symmetric Key
	Private Members
		secretKey
	Get Secret Key()
		gets secretKey
	Constructor
		initialize key algorithm and padding
	WriteToFile()
		save key in a file
		
Main()
	Create key with padding of 16 and AES algorithm.

###################################

Sources:
Marilena Panagiotidou

###################################

*/

// package
package key;

// library imports
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

// class definition
public class GenerateSymmetricKey 
{
	// private member secretKey
	private SecretKeySpec secretKey;

	public GenerateSymmetricKey(int length, String algorithm) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException 
	{
		// use an extra layer of randomness
		SecureRandom random = new SecureRandom();

		// initialize byte array
		byte [] key = new byte [length];
		random.nextBytes(key);

		// initialize secretKey
		this.secretKey = new SecretKeySpec(key, algorithm);
	}
	
	// get secret key
	public SecretKeySpec getKey()
	{
		// return secretKey
		return this.secretKey;
	}

	// create file
	public void writeToFile(String path, byte[] key) throws IOException 
	{
		// create file at provided path
		File f = new File(path);
		f.getParentFile().mkdirs();

		// open file output stream
		FileOutputStream fos = new FileOutputStream(f);

		// save key to file
		fos.write(key);

		// clear buffer
		fos.flush();

		// close output stream
		fos.close();
	}
	
	// main
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException 
	{
		// create symmetric keys using AES algorithm
		GenerateSymmetricKey genSK = new GenerateSymmetricKey(16, "AES");

		// save key to file
		genSK.writeToFile("OneKey/secretKey", genSK.getKey().getEncoded());
	}
}

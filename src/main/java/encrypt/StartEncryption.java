/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/4/2021

###################################

Functional Description: 

Encrypts a file and key using
EncryptData.java and 
EncryptKey.java.

###################################

Pseudocode:

getPrivate(file, algorithm)
	Open the file and read into 
	a byte array
	Set encoding to PKCS8
	set algorithm of key to 
	algorithm
	return key

getPublic(file, algorithm)
	Open the file and read into a 
	byte array
	Set encoding to X509
	set algorithm to algorithm
	return key

getSecretKey(file, algorithm)
	Open file and read into byte 
	array
	return key

Main()
	Set key file path
	Set path for file of encrypted 
	key
	Encrypt key with Bob’s public 
	key using RSA algorithm
	Set path for unencrypted file
	Set file path for the encrypted 
	data file
	Encrypt the file and store it 
	in the project

###################################

Sources:
Marilena Panagiotidou

###################################

*/

// package
package encrypt;

// library imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.spec.SecretKeySpec;

// class definition
public class StartEncryption 
{
	
	// get a private key
	public PrivateKey getPrivateKey(String file, String algorithm) throws Exception 
	{
		// fill byte array with file contents
		byte[] keyBytes = Files.readAllBytes(new File(file).toPath());

		// encoding
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

		// get key's algorithm type
		KeyFactory kf = KeyFactory.getInstance(algorithm);

		// return key
		return kf.generatePrivate(spec);
	}

	// get public key
	public PublicKey getPublicKey(String file, String algorithm) throws Exception 
	{
		// fill byte array with file contents
		byte[] keyBytes = Files.readAllBytes(new File(file).toPath());

		// encoding
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

		// get key's algorithm type
		KeyFactory kf = KeyFactory.getInstance(algorithm);

		// return key
		return kf.generatePublic(spec);
	}
	
	// get secret key
	public SecretKeySpec getSecretKey(String file, String algorithm) throws IOException
	{
		// fill byte array with file contents
		byte[] keyBytes = Files.readAllBytes(new File(file).toPath());

		// return secret key
		return new SecretKeySpec(keyBytes, algorithm);
	}
	
	// main
	public static void main(String filePath) throws IOException, GeneralSecurityException, Exception
	{
		// initialize
		StartEncryption startEnc = new StartEncryption();
		
		// file of key
		File originalKey = new File("OneKey/secretKey");

		// file of encrypted key
		File encryptedKey = new File("EncryptedFiles/encryptedSecretKey");

		// encrypt the key
		new EncryptKey(startEnc.getPublicKey("KeyPair/publicKey_Bob", "RSA"), originalKey, encryptedKey, "RSA");
		
		// create file
		File original = new File(filePath);

		// encrypted file
		File encrypted = new File("EncryptedFiles/encryptedFile");

		// encrypt data using AES algorithm
		new EncryptData(original, encrypted, startEnc.getSecretKey("OneKey/secretKey", "AES"), "AES");
	}
}

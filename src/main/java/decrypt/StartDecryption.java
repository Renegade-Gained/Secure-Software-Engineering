/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/3/2021

###################################

Functional Description: 
Decrypt a key and file.

###################################

Pseudocode:

getPrivateKey(file, algorithm)
	Open the file and read 
	into a byte array
	Set encoding to PKCS8
	set algorithm of key to 
	algorithm
	return key
getPublicKey(file, algorithm)
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
	Set encrypted key file path
	Set path for file of decrypted 
	data file
	decrypt key using Bob’s private 
	key with RSA algorithm
	Set path for encrypted file
	Set file path for the decrypted 
	data file
	Decrypt the file and store it 
	in the specified path

###################################

Sources:
Marilena Panagiotidou

###################################

*/

// Package
package decrypt;

// Library Imports
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

public class StartDecryption 
{
	
	// gets the private key
	public PrivateKey getPrivateKey(String file, String algorithm) throws Exception 
	{
		// byte array for holding file bytes
		byte[] keyBytes = Files.readAllBytes(new File(file).toPath());

		// encoding
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

		// get key's algorithm type
		KeyFactory kf = KeyFactory.getInstance(algorithm);

		// return private key
		return kf.generatePrivate(spec);
	}

	// gets the public key
	public PublicKey getPublicKey(String file, String algorithm) throws Exception 
	{
		// bye array for holding file bytes
		byte[] keyBytes = Files.readAllBytes(new File(file).toPath());

		// encoding
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

		// get key's algorithm type
		KeyFactory kf = KeyFactory.getInstance(algorithm);

		// return public key
		return kf.generatePublic(spec);
	}
	
	// gets secret key
	public SecretKeySpec getSecretKey(String file, String algorithm) throws IOException
	{
		// byte array for holding file bytes
		byte[] keyBytes = Files.readAllBytes(new File(file).toPath());

		// return secret key
		return new SecretKeySpec(keyBytes, algorithm);
	}

	// main
	public static void main(String destFilePath2) throws IOException, GeneralSecurityException, Exception
	{
		// initialize
		StartDecryption startEnc = new StartDecryption();
		
		// file of encrypted key
		File encryptedKey= new File("EncryptedFiles/encryptedSecretKey");

		// file of decrypted key
		File decryptedKey= new File("DecryptedFiles/SecretKey");

		// decrypt key using RSA algorithm
		new DecryptKey(startEnc.getPrivateKey("KeyPair/privateKey_Bob", "RSA"), encryptedKey, decryptedKey, "RSA");
		
		// encrypted file
		File encryptedFileReceived = new File("EncryptedFiles/encryptedFile");

		// decrypted file
		File decryptedFile = new File(destFilePath2);

		// decrypt file
		new DecryptData(encryptedFileReceived, decryptedFile, startEnc.getSecretKey("DecryptedFiles/SecretKey", "AES"), "AES");
	}
}

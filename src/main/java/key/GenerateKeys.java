/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 5/2/2021
Last Modified: 5/3/2021

###################################

Functional Description: 
Creates and stores keys for
encryption and decryption.

###################################

Pseudocode:

Class Generate Keys
	Private Members
		keygen
		pair
		privateKey
		publicKey
	Get Private Key()
		gets private key
	Get Public Key()
		gets public key
	Constructor
		initialize keys algorithms 
		and padding
	CreateKeys()
		Generate a key pair
		set private key members to 
		appropriate key type
	WriteToFile()
		save keys in a file
		

Main()
	Create keys for “Alice” with 
	1024 bytes of padding. Save in
	file.
	Create keys for “Bob” with 
	1024 bytes of padding. Save in
	file.
	
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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

// class definition
public class GenerateKeys 
{
	// private member keygen
	private KeyPairGenerator keyGen;

	// private member pair
	private KeyPair pair;

	// private member privateKey
	private PrivateKey privateKey;

	// private memeber publicKey
	private PublicKey publicKey;

	// set parameters for generating keys
	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException 
	{
		// use RSA algorithm
		this.keyGen = KeyPairGenerator.getInstance("RSA");

		// set length
		this.keyGen.initialize(keylength);
	}

	// create keys
	public void createKeys() 
	{
		// initialize keys
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	// get private key
	public PrivateKey getPrivateKey() 
	{
		// return private key
		return this.privateKey;
	}

	// get public key
	public PublicKey getPublicKey() 
	{
		// return public key
		return this.publicKey;
	}

	// create file
	public void writeToFile(String path, byte[] key) throws IOException 
	{
		// create file at provided path
		File f = new File(path);
		f.getParentFile().mkdirs();

		// open file output stream
		FileOutputStream fos = new FileOutputStream(f);

		// write key to file
		fos.write(key);

		// clear buffer
		fos.flush();

		// close output stream
		fos.close();
	}

	// main
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException 
	{
		// initialize variables
		GenerateKeys gk_Alice;
		GenerateKeys gk_Bob;
		
		// generate keys
		gk_Alice = new GenerateKeys(1024);
		gk_Alice.createKeys();

		// save the keys
		gk_Alice.writeToFile("KeyPair/publicKey_Alice", gk_Alice.getPublicKey().getEncoded());
		gk_Alice.writeToFile("KeyPair/privateKey_Alice", gk_Alice.getPrivateKey().getEncoded());
			
		// generate keys
		gk_Bob = new GenerateKeys(1024);
		gk_Bob.createKeys();

		// save the keys
		gk_Bob.writeToFile("KeyPair/publicKey_Bob", gk_Bob.getPublicKey().getEncoded());
		gk_Bob.writeToFile("KeyPair/privateKey_Bob", gk_Bob.getPrivateKey().getEncoded());
	}
}

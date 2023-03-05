/*

###################################

Programmer   : Samuel Sommerschield
Date Created : 3/16/2021
Last Modified: 5/2/2021

###################################

Functional Description: Main Driver

Provides the menu for selection 
choices and calls other functions.

###################################

Pseudocode:

Main()
Authenticate()
if(authenticated)
	print Menu
	if(choice 1)
		List()
	if(choice 2)
		Upload()
	if(choice 3)
		Download()
	if(choice 4)
		Exit
else
    Exit

###################################

Sources:
Google Cloud Platform Documentation

###################################

*/

// Package
package main;

// Package Imports
import encrypt.StartEncryption;
import decrypt.StartDecryption;

// Library Imports
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class App 
{
    // Main
    public static void main( String[] args ) throws GeneralSecurityException, Exception
    {
        // Proof of authentication
        Boolean auth = false;

        // Main loop exit condition
        Boolean done = false;

        // Input String
        String ipt = "";
        
        // Create a Scanner object
        Scanner input = new Scanner(System.in);

        // Authenticate user
        auth = authenticate(input);

        // If User is authenticated, let them pass!
        if(auth == true)
        {
            // Main loop
            while(done != true)
            {
                // Main Menu
                System.out.println("Main Menu");
                System.out.println("1. List Available Objects for Download");
                System.out.println("2. Upload an Object to Cloud");
                System.out.println("3. Download Object from Cloud");
                System.out.println("4. Exit");
                System.out.println("Enter a choice 1 to 4: ");
                // Choice
                ipt = input.nextLine();

                switch(ipt)
                {
                    case "1":
                        listObjects();
                        break;
                    case "2":
                        UploadObject(input);
                        break;
                    case "3":
                        downloadObject(input);
                        break;
                    case "4":
                        done = true;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Please enter a valid input of numbers 1 to 4.");
                        break;
                }

            }
        }
        
        // If User isn't authenticated, kick them out!
        else
        {
            System.out.println("Maximum Attempts Exceeded. Exiting...");
        }

        // Close scanner object
        input.close();
    }


    /* 
    
    ###################################

    Function Name: authenticate
    Last Modified: 4/14/2021

    ###################################

    Functional Description: 
    Authenticate user by querying 
    firestore database in cloud.

    ###################################

    Pseudocode:

    Authenticate()
	    Connect to Firestore Database in Cloud
	    Get Username and Password
	    Query for matching data
	        if(data exists)
		        return true
	        else
		        try again
	        if(attempts> 3)
		        return false

    ###################################

    */
    private static Boolean authenticate(Scanner input) throws InterruptedException, ExecutionException, IOException
    {
        // Initialize return value
        Boolean result = false;

        // Get firestore instance
        FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId("lofty-defender-307302")
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build();
        Firestore db = firestoreOptions.getService();

        // Initialize variables
        String user = "";
        String pass = "";
        int counter = 0;

        // Create collection reference to data in firestore database
        CollectionReference users = db.collection("users");

        // 3 attempts
        while(counter < 3 && result == false)
        {
            // Get username
            System.out.println("Enter Username: ");
            user = input.nextLine();
        
            // Get password
            System.out.println("Enter Password: ");
            pass = input.nextLine();

            // Create a query against the collection.
            Query query = users.whereEqualTo("username", user).whereEqualTo("password", pass);
            // retrieve  query results asynchronously using query.get()
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) 
            {
                if(document.exists())
                {
                    result = true;
                }
                // testing
                // System.out.println(document.getId());
            }

            // increment counter, amount of attempts
            counter++;

            // catch if wrong information was input
            if(result == false && counter < 3)
            {
                System.out.println("Incorrect information entered. Please try again.");
            }
        }

        // return true / false
        return result;
    }

    /* 
    
    ###################################

    Function Name: UploadObject
    Last Modified: 5/5/2021

    ###################################

    Functional Description:
    Uploads a file to cloud storage by
    taking a filepath and name for the
    file.

    ###################################

    Pseudocode:
    
    Upload()
	    Get file path
	    Get desired cloud file name

        Encrypt file

	    upload to cloud

    ###################################

    */
    private static void UploadObject(Scanner input) throws GeneralSecurityException, Exception
    {
        // Initialize variables
        String filePath = "";
        String encryptedFilePath = "";
        String objectName = "";

        // Get the filepath
        System.out.println("Enter the filepath for your file, including filetype (example: C:/Users/Documents/example.txt): ");
        filePath = input.nextLine();

        // Get the object name for the cloud
        System.out.println("Enter the name for your file: ");
        objectName = input.nextLine();

        // call another function's main
        StartEncryption.main(filePath);

        // Encrypted file will be stored here
        encryptedFilePath = "EncryptedFiles/encryptedFile";

        // The ID of your GCP project
        String projectId = "lofty-defender-307302";
  
        // The ID of your GCS bucket
        String bucketName = "totally_secure_bucket";
  
        // Connect
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        // Upload
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(encryptedFilePath)));
  
        // Confirmation
        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    /* 
    
    ###################################

    Function Name: listObjects
    Last Modified: 4/12/2021

    ###################################

    Functional Description:
    Lists objects available in cloud
    storage bucket.

    ###################################

    Pseudocode:

    List()
	    connect to bucket
	    print out each file’s name

    ###################################

    */
    public static void listObjects() 
    {
        // The ID of your GCP project
        String projectId = "lofty-defender-307302";
      
        // The ID of your GCS bucket
        String bucketName = "totally_secure_bucket";
      
        // Connect
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Page<com.google.cloud.storage.Blob> blobs = storage.list(bucketName);
      
        // Print out each item
        for (com.google.cloud.storage.Blob blob : blobs.iterateAll()) 
        {
            System.out.println(blob.getName());
        }
    }

    /* 
    
    ###################################

    Function Name: downloadObject
    Last Modified: 5/5/2021

    ###################################

    Functional Description:
    Downloads an object of the user's
    choosing to the file path the user
    provides.

    ###################################

    Pseudocode:

    Download()
	    Get desired file for download’s name
	    Get desired download location path
	    Connect to bucket
	    Download file

        Decrypt file
    
    ###################################

    */
    public static void downloadObject(Scanner input) throws IOException, GeneralSecurityException, Exception 
    {
    // The ID of your GCP project
    String projectId = "lofty-defender-307302";

    // The ID of your GCS bucket
    String bucketName = "totally_secure_bucket";

    // The ID of your GCS object
    System.out.println("Enter the name of the file you wish to download: ");
    String objectName = input.nextLine();

    // The path to which the file should be downloaded
    System.out.println("Enter the path the file should be downloaded to (Example: C:/Users/saint/Downloads/example.txt): ");
    String destFilePath2 = input.nextLine();
    
    // put in encrypted file folders for decryption
    String destFilePath = "EncryptedFiles/encryptedFile";

    // Connect
    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    // Download
    Blob blob = storage.get(BlobId.of(bucketName, objectName));
    blob.downloadTo(Paths.get(destFilePath));

    // decrypt file
    StartDecryption.main(destFilePath2);

    // Confirmation
    System.out.println(
        "Downloaded object "
            + objectName
            + " from bucket name "
            + bucketName
            + " to "
            + destFilePath);
  }
}
# Secure-Software-Engineering

Secure File Storage on Cloud using Hybrid Cryptography

This project was developed on Windows, programmed in Java, using Visual Studio Code, Maven, and Google Cloud Platform services.

# Steps to Recreate Project
1. Install Visual Studio Code
2. Ensure the following items are installed in the Visual Studio Code Extension Explorer:
  * Cloud Code
  * Debugger for Java
  * Dependency Analytics
  * Java Extension Pack
  * Java Test Runner
  * Language Support for Java
  * Maven for Java
  * Project Manager for Java
  * Red Hat Commons
  * Visual Studio IntelliCode
  * XML
3. If Maven is not properly setup, you may have to install it outside of Visual Studio Code
  * Do not forget to set the environment or system variables so Visual Studio Code can find Maven
4. Create a GCP project with a Firestore database containing usernames (column 'username') and passwords (column 'password') and create a bucket
5. Acquire a service key to connect to your GCP project
6. Run command in Visual Studio Code's powershell:
    $env:GOOGLE_APPLICATION_CREDENTIALS="Disk:\path\to\json\key.json"
7. If keys have not been generated yet, or cleared from project before running, make sure to run the code to generate them.

# Requirements
1. Store files in the cloud
2. Use hybrid cryptography

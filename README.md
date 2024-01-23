# Chat-Server
This project was developed for a college assignment. The module taught Networking Technologies. 

## About the Assignment
This assignment required students to design and implement a network-based chat application in Java using the Java Socket API.

## Design
The design of Chat App 1.0 is based on NeuralNine (2021), Barhoumi (2021), French (2022), and Minh (2019). Additionally, the design took insight from A Guide to Java Sockets (2023), Jade (2006), JavaDocs Java.net package (2022) and Java Socket Programming (n.d.).

I modelled the design of the app closely to NeuralNine (2021) because the author deployed a thread pool to handle multiple clients. The approach of using thread pools was supported by French (2022). Additionally, NeuralNine deployed a simple structure which was common to other chat applications that I researched (Barhoumi 2021; Minh 2019). 
The common elements included:
- Server and ServerSocket classes to handle TCP connections.
- BufferedReader class to read data from an input stream.
- PrintWriter class to print data to an output stream.
- ArrayList to store multiple clients.

By recognising these common elements and how they are used with the Java Socket API (A Guide to Java Sockets 2023; JavaDocs Java.net package 2022; Java Socket Programming n.d.), I was able to develop features that addressed the issues outlined in the assignment brief.

## Features
This section outlines the main features of the app:
- The user can specify the port number for the server and the host address and server port number for the client (French 2022; Minh 2019).
- The client returns feedback and closes if there is an issue connecting to the server (Minh 2019).
- The server creates a thread pool to handle multiple clients connections concurrently (French 2022; NeuralNine 2021).
- The client can create a user name (Minh 2019; NeuralNine 2021).
- The client can leave the chat by entering \q (Minh 2019; NeuralNine 2021).
- The server admin can write messages to the clients (Barhoumi 2021).
- The server admin can shut down the server by entering \close.
- If the server closes with active client connections the server broadcasts a message that closes all the active clients (Jade 2006).
- There are numerous feedback messages that print to the console of the server and the client (French 2022).
- The code from the server and the client is separated into individual classes to help with readability (Minh 2019).

## Demo Instructions
This section outlines the instructions for the demo.
Notes:
- The Server class takes in any valid port number as an argument.
- The Client class takes in any valid hostname and port number as arguments.
- The demo instructions are written for Mac OS.

Step 1: Open a terminal window from the folder titled src<br>

Step 2: Enter javac ie/atu/server/*.java to compile the code<br>

Step 3: Enter java ie.atu.server.Server 14 to launch the server<br>

Step 4: Open another terminal window from the folder titled src<br>

Step 5: Enter javac ie/atu/client/*.java to compile the code<br>

Step 6: Enter java ie.atu.client.Client localhost 14 to connect the client to the server<br>

Step 7: Enter your user name at the client window<br>

Step 8: Repeat step 4, step 6 and step 7 to connect additional clients (skip step 5)<br>

Step 9: Enter typed text at each terminal window to stream messages<br>

Step 10: Enter \q at each client window to disconnect each client from the server<br>

Step 11: Enter \close at the server window to shut down the server<br>

Error Tests:
- Enter java ie.atu.client.Client localhost 14 while the server is not running.
- Enter \close at the server window while multiple clients are still connected to the server.

## Known Bugs and Issues
Chat App 1.0 has the following known issues:
- The app throws a java.net.SocketException when closing the client streams in most cases(sometimes it does not).
- The above issue also throws an exception in the ConnectionHandler class in the server package.
- Another java.net.SocketException is thrown when the server shuts down.
  - This causes two shutdown messages as the catch block calls the shutdown method.
- I had to make some of the instance variables protected which is a breach of encapsulation.
- I added System.exit(0) to the Client shutDown() method to close the client fully when the server requests the connected clients to close. Without the call to System.exit(0) the client did not close fully.

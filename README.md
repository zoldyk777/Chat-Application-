# Chat-Application- in java
A simple LAN-based chat application built in Java using TCP sockets and multithreading.
It allows multiple clients on the same network to connect to a central server and exchange messages in real-time.
# instructions to compile and run
# 1. Compile
javac ChatServer.java ChatClient.java

# 2. Launch server in one terminal
java ChatServer

# 3. Launch each client in separate terminals or different PCs on the same LAN
java ChatClient            # default localhost
java ChatClient 192.168.1.5    # change HOST constant or pass param

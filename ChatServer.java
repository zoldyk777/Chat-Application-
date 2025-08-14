import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 5000;
    // thread‑safe set of all client writers for broadcast
    private final Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();

    public void start() {
        System.out.println("Server listening on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getRemoteSocketAddress());
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private String name = "Anonymous";

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                clientWriters.add(out);
                out.println("Welcome! Enter your name:");
                name = in.readLine();

                broadcast("*** " + name + " joined ***");

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("/quit")) break;
                    broadcast(name + ": " + msg);
                }
            } catch (IOException e) {
                System.err.println("Connection error: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                broadcast("*** " + name + " left ***");
            }
        }

        private void broadcast(String message) {
            System.out.println(message);
            clientWriters.forEach(w -> w.println(message));
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}

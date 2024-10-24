package LTM_07;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chat_Server3 {
    private static final int PortNumber = 9999;
    private static final String LOG_FILE = "chat_log.txt";

    public static void main(String[] args) {
        try {
            // Get Local IP Address
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Server is running on IP: " + localAddress.getHostAddress());

            ServerSocket serverSocket = new ServerSocket(PortNumber);
            System.out.println("Server is listening...");

            ArrayList<ClientHandler3> clientHandlers = new ArrayList<>();
            ExecutorService executor = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new thread to handle the client
                ClientHandler3 clientHandler = new ClientHandler3(clientSocket, clientHandlers);
                clientHandlers.add(clientHandler);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logMessage(String senderName, String message) {
        try (PrintWriter logWriter = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true)))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            logWriter.println(timestamp + " - " + senderName + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler3 implements Runnable {
    private Socket clientSocket;
    private String clientName;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler3> clientHandlers;

    public ClientHandler3(Socket clientSocket, ArrayList<ClientHandler3> clientHandlers) {
        this.clientSocket = clientSocket;
        this.clientHandlers = clientHandlers;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get client name from the first message
            clientName = in.readLine();
            System.out.println("Client name: " + clientName);

            // Read data from the client and broadcast to other clients
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break; // Client disconnected
                }
                if (message.equalsIgnoreCase("exit")) {
                    broadcastMessage(clientName + " has exited the chat.");
                    System.out.println(clientName + " has exited the chat.");
                    break;
                }
                Chat_Server3.logMessage(clientName, message);
                broadcastMessage(clientName + ": " + message);
            }

            // Remove client handler from the list
            clientHandlers.remove(this);

            // Close the client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler3 clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}


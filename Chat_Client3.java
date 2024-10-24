package LTM_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Chat_Client3 {
    public static void main(String[] args) {
        try {
            final int PortNumber = 9999;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

            // Get server IP address from user input
            System.out.print("Enter Server IP Address: ");
            String serverIP = inputReader.readLine();

            // Create a client socket
            Socket clientSocket = new Socket(serverIP, PortNumber);

            // Create input/output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get client name from user input
            System.out.print("Enter your name: ");
            String clientName = inputReader.readLine();

            // Send client name to the server
            out.println(clientName);

            // Start a new thread to read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String response = in.readLine();
                        if (response == null) break; // Server closed
                        System.out.println(response);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readThread.start();

            // Send data to the server
            while (true) {
                String message = inputReader.readLine();
                out.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            // Close the client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


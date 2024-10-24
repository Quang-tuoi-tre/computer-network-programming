package MulticastUDP;

import java.io.IOException;
import java.net.*;

public class MulticastSender {
    public static void main(String[] args) {
        try {
            InetAddress multicastGroup = InetAddress.getByName("230.1.1.1"); // Multicast IP address, 224.0.0.1, 239.255.255.250, 239.0.0.1
            int port = 7777; // Multicast port number

            MulticastSocket socket = new MulticastSocket();

            String message = "Hello, multicast receivers!";
            byte[] buffer = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, multicastGroup, port);

            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


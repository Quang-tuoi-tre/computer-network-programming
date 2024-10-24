package Input_Output_Stream;

import java.io.IOException;
import java.net.*;

public class MulticastReceiver {
    public static void main(String[] args) {
        try {
            InetAddress multicastGroup = InetAddress.getByName("230.1.1.1");
            // Multicast IP address, 224.0.0.1, 239.255.255.250, 239.0.0.1
            int port = 12345; // Multicast port number

            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(multicastGroup);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

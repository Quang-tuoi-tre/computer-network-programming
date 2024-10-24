package LTM_06;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

public class TempServer2_N {
    private static final int PORT = 9999;
    private static HashMap<String, Integer> temperatureData = new HashMap<>();

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            byte[] receiveData = new byte[1024];

            // Hiển thị địa chỉ IP của máy chủ
            displayServerIPAddress();

            System.out.println("UDP Server đang lắng nghe...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
                String message = bufferedReader.readLine().trim();

                String[] data = message.split(":");
                if (data.length == 3 && data[0].equals("Temperature")) {
                    String location = data[1];
                    int temperature = Integer.parseInt(data[2]);
                    String clientAddress = receivePacket.getAddress().getHostAddress();
                    temperatureData.put(location + "(" + clientAddress + ")", temperature);

                    // Kiểm tra nhiệt độ vượt ngưỡng cảnh báo
                    checkTemperatureThreshold(location, clientAddress, temperature);
                }

                // Hiển thị dữ liệu nhiệt độ từ tất cả các thiết bị IoT đã kết nối
                displayTemperatureData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkTemperatureThreshold(String location, String clientAddress, int temperature) {
        if (temperature < 0 || temperature > 35) {
            String alertMessage = "Cảnh báo nhiệt độ vượt ngưỡng tại Địa điểm " + location + " (" + clientAddress + "): "
                    + temperature + " độ C vào lúc " + getCurrentTime();
            System.out.println(alertMessage);
            saveAlertToLogFile(alertMessage);
        }
    }

    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private static void displayServerIPAddress() {
        try {
            // Lấy địa chỉ Local IP của máy chủ
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // Lọc và tìm địa chỉ IP của giao diện Local (không phải loopback)
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        String serverIP = inetAddress.getHostAddress();
                        System.out.println("Địa chỉ IP của Server: " + serverIP);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static void displayTemperatureData() {
        System.out.println("Dữ liệu nhiệt độ:");
        for (String location : temperatureData.keySet()) {
            int temperature = temperatureData.get(location);
            System.out.println("Địa điểm " + location + ": " + temperature + " độ C");
        }
    }

    private static void saveAlertToLogFile(String alertMessage) {
        try (FileWriter fileWriter = new FileWriter("alerts_log2.txt", true)) {
            fileWriter.write(alertMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package LTM_06;


import java.net.*;
import java.util.Scanner;

public class TempSensor2 {
    private static final int PORT = 9999;
    private static String SERVER_IP; // Địa chỉ IP của máy chủ
    private static String location; // Địa điểm đo nhiệt độ

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập địa chỉ IP của máy chủ: ");
        SERVER_IP = scanner.nextLine();

        System.out.print("Nhập địa điểm đo nhiệt độ: ");
        location = scanner.nextLine();
        //Bến Tre
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            while (true) {
                int temperature = readTemperatureFromSensor(); // Hàm này giả lập đọc nhiệt độ từ cảm biến
                String message = "Temperature:" + location + ":" + temperature;
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        InetAddress.getByName(SERVER_IP), PORT);
                clientSocket.send(sendPacket);

                Thread.sleep(5000); // Gửi dữ liệu mỗi 10 giây
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int readTemperatureFromSensor() {
        // Giả lập đọc nhiệt độ từ cảm biến
        return (int) (Math.random() * 55) - 10;
    }


}



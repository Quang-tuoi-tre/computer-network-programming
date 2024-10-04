package Input_Output_Stream;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class OanTuTi2_Client {
    public static void main(String[] args) {
        try {
            // Khởi tạo địa chỉ mặc định của máy chủ
            String serverAddress = "localhost";
            int serverPort = 1234;

            // Khởi tạo Socket
            Socket socket = null;

            // Vòng lặp để kiểm tra kết nối đến máy chủ localhost
            boolean isConnected = false;
            while (!isConnected) {
                try {
                    // Thử kết nối đến máy chủ
                    socket = new Socket(serverAddress, serverPort);
                    isConnected = true; // Kết nối thành công
                } catch (IOException e) {
                    // Nếu không kết nối được, yêu cầu người dùng nhập địa chỉ IP của máy chủ
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.print("Không thể kết nối đến localhost. Nhập địa chỉ IP của máy chủ: ");
                    try {
                        serverAddress = reader.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // Tạo luồng vào/ra cho client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Tạo giao diện người dùng
            Scanner scanner = new Scanner(System.in);
            // Đọc thông báo từ server rằng nhập tên của ngừoi chơi
            String serverMessage = in.readLine();
            System.out.println(serverMessage);
            String playerName = scanner.nextLine();
            out.println(playerName);
            // Đọc thông báo từ server tên của đối thủ
            serverMessage = in.readLine();
            System.out.println(serverMessage);

            // Vòng lặp chính của trò chơi
            while (true) {
                // Đọc thông báo từ server rằng trò chơi đã bắt đầu
                serverMessage = in.readLine();
                System.out.println(serverMessage);

                // Người chơi nhập lựa chọn
                System.out.print("Nhập lựa chọn (BÚA, BAO, KÉO): ");
                String choice = scanner.nextLine();

                // Gửi lựa chọn tới server
                out.println(choice);

                // Nhận kết quả từ server
                String result = in.readLine();
                System.out.println("Kết quả: " + result);

                // Kiểm tra xem có chơi tiếp hay không
                String playAgain;
                while (true) {
                    System.out.print("Chơi tiếp? (Y/N): ");
                    playAgain = scanner.nextLine().trim();
                    if (playAgain.equalsIgnoreCase("Y") || playAgain.equalsIgnoreCase("N")) {
                        break;
                    }
                    System.out.println("Vui lòng chỉ nhập Y hoặc N.");
                }

                // Gửi thông báo chơi tiếp hay không tới server
                out.println(playAgain);

                if (playAgain.equalsIgnoreCase("N")) {
                    break;
                }
            }

            // Đóng kết nối
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



package Input_Output_Stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;

public class OanTuTi2_Server {
    public static void main(String[] args) {
        try {
            // Tạo socket server và lắng nghe cổng 1234
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server đã khởi động và đang lắng nghe cổng 1234...");

            // Lấy địa chỉ Local IP của Server

            try {
                // Lấy danh sách tất cả các địa chỉ IP của máy tính
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        // Lọc và tìm địa chỉ IP của giao diện Local (không phải loopback)
                        if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                            System.out.println("Địa chỉ IP của Server: " + inetAddress.getHostAddress());
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

            // Chấp nhận kết nối từ ba client
            Socket player1Socket = serverSocket.accept();
            System.out.println("Người chơi 1 đã kết nối!");
            Socket player2Socket = serverSocket.accept();
            System.out.println("Người chơi 2 đã kết nối!");
            Socket player3Socket = serverSocket.accept(); // New player 3
            System.out.println("Người chơi 3 đã kết nối!");

            // Tạo luồng vào/ra cho người chơi 1, 2, 3
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            PrintWriter player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            PrintWriter player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
            BufferedReader player3In = new BufferedReader(new InputStreamReader(player3Socket.getInputStream())); // New input for player 3
            PrintWriter player3Out = new PrintWriter(player3Socket.getOutputStream(), true); // New output for player 3

            // Nhập tên người chơi 1, 2, 3
            player1Out.println("Nhập tên người chơi 1: ");
            player2Out.println("Nhập tên người chơi 2: ");
            player3Out.println("Nhập tên người chơi 3: "); // New prompt for player 3
            String player1Name = player1In.readLine();
            System.out.println("Người chơi 1 đã nhập tên: " + player1Name);

            String player2Name = player2In.readLine();
            System.out.println("Người chơi 2 đã nhập tên: " + player2Name);

            String player3Name = player3In.readLine(); // New input for player 3
            System.out.println("Người chơi 3 đã nhập tên: " + player3Name);

            player1Out.println("Bạn đang chơi với " + player2Name + " và " + player3Name);
            player2Out.println("Bạn đang chơi với " + player1Name + " và " + player3Name);
            player3Out.println("Bạn đang chơi với " + player1Name + " và " + player2Name); // New output for player 3

            // Khởi tạo điểm ban đầu cho cả ba người chơi
            double player1Score = 0;
            double player2Score = 0;
            double player3Score = 0; // New score for player 3

            // Vòng lặp chính của trò chơi
            while (true) {
                // Gửi thông báo cho cả ba người chơi rằng trò chơi đã bắt đầu
                player1Out.println("BẮT ĐẦU");
                player2Out.println("BẮT ĐẦU");
                player3Out.println("BẮT ĐẦU"); // New start message for player 3

                // Người chơi 1, 2, 3 chọn lựa chọn
                String player1Choice = player1In.readLine();
                System.out.println("Người chơi 1 đã chọn: " + player1Choice);

                String player2Choice = player2In.readLine();
                System.out.println("Người chơi 2 đã chọn: " + player2Choice);

                String player3Choice = player3In.readLine(); // New choice for player 3
                System.out.println("Người chơi 3 đã chọn: " + player3Choice);

                // Xác định kết quả
                String result = determineResult(player1Choice, player2Choice, player3Choice); // Updated method call

                // In ra màn hình kết quả ai thắng
                System.out.println("Kết quả lượt chơi: " + result);

                // Cập nhật điểm của người chơi
                // Logic to update scores for three players
                if (result.equals("NGƯỜI CHƠI 1 THẮNG")) {
                    player1Score += 1;
                } else if (result.equals("NGƯỜI CHƠI 2 THẮNG")) {
                    player2Score += 1;
                } else if (result.equals("NGƯỜI CHƠI 3 THẮNG")) { // New condition for player 3
                    player3Score += 1;
                } else if (result.equals("HÒA")) {
                    player1Score += 0.5;
                    player2Score += 0.5;
                    player3Score += 0.5; // New score update for player 3
                }
                String tySo = player1Name + " " + player1Score + " | " + player2Name + " " + player2Score + " | " + player3Name + " " + player3Score; // Updated score display
                System.out.println(tySo);
                // Gửi điểm tỷ số cho cả ba người chơi
                player1Out.println(tySo);
                player1Out.flush();
                player2Out.println(tySo);
                player2Out.flush();
                player3Out.println(tySo); // New output for player 3
                player3Out.flush();

                // Kiểm tra xem có chơi tiếp hay không
                String playAgain1 = player1In.readLine();
                String playAgain2 = player2In.readLine();
                String playAgain3 = player3In.readLine(); // New input for player 3
                if (!playAgain1.equalsIgnoreCase("Y") || !playAgain2.equalsIgnoreCase("Y") || !playAgain3.equalsIgnoreCase("Y")) { // Updated condition
                    break;
                }
            }

            // Đóng kết nối
            player1In.close();
            player1Out.close();
            player1Socket.close();
            player2In.close();
            player2Out.close();
            player2Socket.close();
            player3In.close(); // New close for player 3
            player3Out.close(); // New close for player 3
            player3Socket.close(); // New close for player 3
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Xác định kết quả dựa trên lựa chọn của ba người chơi
    private static String determineResult(String player1Choice, String player2Choice, String player3Choice) {
        player1Choice = player1Choice.toUpperCase();
        player2Choice = player2Choice.toUpperCase();
        player3Choice = player3Choice.toUpperCase(); // New choice handling for player 3

        // Logic to determine the winner among three players
        if (player1Choice.equals(player2Choice) && player1Choice.equals(player3Choice)) {
            return "HÒA";
        } else if (player1Choice.equals(player2Choice)) {
            return (player1Choice.equals("BÚA") && player3Choice.equals("KÉO")) ||
                    (player1Choice.equals("KÉO") && player3Choice.equals("BAO")) ||
                    (player1Choice.equals("BAO") && player3Choice.equals("BÚA")) ? "NGƯỜI CHƠI 1 THẮNG" : "NGƯỜI CHƠI 3 THẮNG";
        } else if (player1Choice.equals(player3Choice)) {
            return (player1Choice.equals("BÚA") && player2Choice.equals("KÉO")) ||
                    (player1Choice.equals("KÉO") && player2Choice.equals("BAO")) ||
                    (player1Choice.equals("BAO") && player2Choice.equals("BÚA")) ? "NGƯỜI CHƠI 1 THẮNG" : "NGƯỜI CHƠI 2 THẮNG";
        } else if (player2Choice.equals(player3Choice)) {
            return (player2Choice.equals("BÚA") && player1Choice.equals("KÉO")) ||
                    (player2Choice.equals("KÉO") && player1Choice.equals("BAO")) ||
                    (player2Choice.equals("BAO") && player1Choice.equals("BÚA")) ? "NGƯỜI CHƠI 2 THẮNG" : "NGƯỜI CHƠI 3 THẮNG";
        } else {
            return "NGƯỜI CHƠI 2 THẮNG"; // Default case
        }
    }
}

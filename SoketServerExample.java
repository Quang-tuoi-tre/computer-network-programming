package Input_Output_Stream;

import java.io.*;
import java.net.*;
public class SoketServerExample {

    public static void main(String[] args) {
        try {
// Tạo SocketServer và lắng nghe cổng 1234
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server đã khởi động và đang lắng nghe cổng 1234...");
// Chấp nhận kết nối từ Client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client đã kết nối!");
// Lấy luồng vào và ra để giao tiếp với Client
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream, true);
// Đọc dữ liệu từ Client và gửi phản hồi
            String clientMessage = in.readLine();
            System.out.println("Client: " + clientMessage);
            out.println("Xin chào, Client!");
// Đóng kết nối
clientSocket.close();
serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
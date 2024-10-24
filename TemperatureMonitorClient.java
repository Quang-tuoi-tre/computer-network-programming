package Input_Output_Stream;


// Package RMICallbacks: Gói (package) chứa các lớp và giao diện liên quan đến việc triển khai RMI với callbacks.

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.util.Scanner;

public class TemperatureMonitorClient {
    private Input_Output_Stream.TemperatureListener listener;
    private double currentTemperature;
    private boolean isRegistered;

    public TemperatureMonitorClient() {
        // Tạo một đối tượng listener để lắng nghe thông báo về thay đổi nhiệt độ từ server
        listener = new Input_Output_Stream.TemperatureListener() {
            @Override
            public void temperatureChanged(double newTemperature) {
                // Kiểm tra xem client có đang đăng ký lắng nghe không trước khi in thông báo thay đổi nhiệt độ
                if (isRegistered) {
                    currentTemperature = newTemperature;
                    // Sử dụng DecimalFormat để chỉ hiển thị 1 số thập phân sau dấu phẩy
                    DecimalFormat df = new DecimalFormat("#.#");
                    System.out.println("Nhiệt độ đã thay đổi: " + df.format(currentTemperature));

                }
            }
        };

        try {
            // Kết nối tới RMI Registry trên localhost, port 1099
            Registry registry = LocateRegistry.getRegistry("10.100.2.226", 1099);
            // Lấy đối tượng TemperatureSensorService từ RMI Registry
            Input_Output_Stream.TemperatureSensorService temperatureSensorService = (Input_Output_Stream.TemperatureSensorService) registry.lookup("TemperatureSensorService");
            // Tạo một đối tượng RemoteTemperatureListener để gửi lời gọi callback tới server
            Input_Output_Stream.RemoteTemperatureListener remoteListener = new Input_Output_Stream.RemoteTemperatureListener(listener);

            // Đăng ký đối tượng listener với dịch vụ TemperatureSensorService trên server
            temperatureSensorService.registerListener(remoteListener);
            isRegistered = true;
            System.out.println("Temperature Monitor Client đã được đăng ký như một listener.");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Nhập 'out' để hủy đăng ký, 'in' để đăng ký lại, hoặc 'exit' để thoát:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("out")) {
                    if (isRegistered) {
                        // Hủy đăng ký listener nếu đã đăng ký trước đó
                        temperatureSensorService.unregisterListener(remoteListener);
                        isRegistered = false;
                        System.out.println("Temperature Monitor Client đã bị hủy đăng ký.");
                    } else {
                        System.out.println("Bạn chưa đăng ký làm listener.");
                    }
                } else if (input.equalsIgnoreCase("in")) {
                    if (!isRegistered) {
                        // Đăng ký lại listener nếu chưa đăng ký hoặc đã hủy đăng ký trước đó
                        temperatureSensorService.registerListener(remoteListener);
                        isRegistered = true;
                        System.out.println("Temperature Monitor Client đã được đăng ký lại.");
                    } else {
                        System.out.println("Bạn đã đăng ký làm listener rồi.");
                    }
                } else if (input.equalsIgnoreCase("exit")) {
                    if (isRegistered) {
                        // Hủy đăng ký listener trước khi thoát khỏi chương trình
                        temperatureSensorService.unregisterListener(remoteListener);
                        isRegistered = false;
                        System.out.println("Temperature Monitor Client đã bị hủy đăng ký.");
                    }
                    break; // Thoát khỏi vòng lặp khi nhập "exit"
                } else {
                    System.out.println("Đầu vào không hợp lệ. Vui lòng thử lại.");
                }
            }

            System.out.println("Temperature Monitor Client đang tắt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TemperatureMonitorClient();
    }
}

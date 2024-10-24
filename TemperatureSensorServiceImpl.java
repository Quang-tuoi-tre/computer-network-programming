package Input_Output_Stream;
// Package RMICallbacks: Gói (package) chứa các lớp liên quan đến việc triển khai RMI với callbacks.

// TemperatureSensorServiceImpl.java (RMI Server)
// Lớp TemperatureSensorServiceImpl là một máy chủ RMI thực hiện giao diện TemperatureSensorService.
// Nó kế thừa từ UnicastRemoteObject để cho phép các phương thức của nó có thể được gọi từ xa
// thông qua RMI, và cũng triển khai giao diện TemperatureSensorService để cung cấp dịch vụ giám sát nhiệt độ.

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

public class TemperatureSensorServiceImpl extends UnicastRemoteObject implements TemperatureSensorService {
    private List<TemperatureListener> listeners;
    private double currentTemperature;

    // Constructor của lớp, được gọi khi tạo mới một đối tượng TemperatureSensorServiceImpl.
    // Trong hàm này, chúng ta khởi tạo danh sách listeners và giả định máy chủ cung cấp thông tin nhiệt độ một cách ngẫu nhiên.
    public TemperatureSensorServiceImpl() throws RemoteException {
        listeners = new ArrayList<>();
        // Giả định máy chủ cung cấp thông tin nhiệt độ một cách ngẫu nhiên
        simulateTemperatureChanges();
    }

    // Phương thức private simulateTemperatureChanges dùng để giả định nhiệt độ thay đổi theo thời gian.
    // Đối tượng TemperatureSensorServiceImpl sẽ duyệt qua danh sách listeners và thông báo về sự thay đổi nhiệt độ cho mỗi listener.
    private void simulateTemperatureChanges() {
        Thread thread = new Thread(() -> {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(5000); // Giả định nhiệt độ thay đổi mỗi 5 giây
                    double newTemperature = random.nextDouble() * 50; // Nhiệt độ từ 0 đến 50
                    currentTemperature = newTemperature;
                    notifyListeners(newTemperature);
                } catch (InterruptedException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    // Phương thức private notifyListeners dùng để thông báo sự thay đổi nhiệt độ cho các listeners đã đăng ký.
    // Phương thức sẽ gọi lại phương thức temperatureChanged của từng listener và truyền vào giá trị nhiệt độ mới đã thay đổi.
    private void notifyListeners(double newTemperature) throws RemoteException {
        for (TemperatureListener listener : listeners) {
            listener.temperatureChanged(newTemperature);
        }
    }

    // Override phương thức registerListener từ giao diện TemperatureSensorService để đăng ký một đối tượng làm người nghe (listener).
    // Phương thức sẽ thêm listener vào danh sách listeners để theo dõi sự thay đổi nhiệt độ.
    @Override
    public void registerListener(TemperatureListener listener) throws RemoteException {
        listeners.add(listener);
    }

    // Override phương thức unregisterListener từ giao diện TemperatureSensorService để hủy đăng ký một đối tượng làm người nghe (listener).
    // Phương thức sẽ loại bỏ listener khỏi danh sách listeners và không theo dõi sự thay đổi nhiệt độ nữa.
    @Override
    public void unregisterListener(TemperatureListener listener) throws RemoteException {
        listeners.remove(listener);
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
    // Phương thức main để chạy máy chủ RMI.
    // Trong phương thức này, chúng ta tạo một đối tượng TemperatureSensorServiceImpl,
    // export đối tượng này thông qua RMI registry và đăng ký tên "TemperatureSensorService".
    public static void main(String[] args) {
        try {
            TemperatureSensorService temperatureSensorService = new TemperatureSensorServiceImpl();
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.createRegistry(1099);
            registry.rebind("TemperatureSensorService", temperatureSensorService);
            System.out.println("Temperature Sensor Service is running.");
            // Hiển thị địa chỉ IP của máy chủ
            displayServerIPAddress();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

package Input_Output_Stream;

// Package RMICallbacks: Gói (package) chứa các lớp và giao diện liên quan đến việc triển khai RMI với callbacks.

// RemoteTemperatureListener.java
// Lớp RemoteTemperatureListener là lớp triển khai giao diện TemperatureListener và kế thừa từ UnicastRemoteObject.
// Lớp này được sử dụng để chuyển tiếp thông báo về sự thay đổi nhiệt độ từ máy chủ RMI đến máy khách (client) sử dụng dịch vụ giám sát nhiệt độ.
// Khi có sự thay đổi nhiệt độ từ máy chủ, lớp này nhận thông báo và chuyển tiếp thông tin đến đối tượng localListener đã được máy khách đăng ký.

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteTemperatureListener extends UnicastRemoteObject implements TemperatureListener {
    private TemperatureListener localListener; // Đối tượng người nghe (listener) của máy khách

    // Constructor của lớp RemoteTemperatureListener
    // Lớp này được tạo ra để chuyển tiếp thông báo về sự thay đổi nhiệt độ từ máy chủ RMI đến máy khách (client).
    public RemoteTemperatureListener(TemperatureListener localListener) throws RemoteException {
        this.localListener = localListener; // Lưu đối tượng người nghe của máy khách
    }

    // Phương thức temperatureChanged triển khai từ giao diện TemperatureListener
    // Phương thức này được gọi từ máy chủ RMI khi có sự thay đổi nhiệt độ.
    // Lớp này tiến hành chuyển tiếp thông báo đến đối tượng localListener đã được máy khách đăng ký.
    @Override
    public void temperatureChanged(double newTemperature) throws RemoteException {
        localListener.temperatureChanged(newTemperature); // Gọi lại phương thức của người nghe (listener) máy khách
    }
}

package Input_Output_Stream;
// Package RMICallbacks: Gói (package) chứa các lớp và giao diện liên quan đến việc triển khai RMI với callbacks.

// TemperatureListener.java (Giao diện chung)
// Giao diện TemperatureListener là một giao diện chia sẻ giữa máy chủ và các máy khách (clients).
// Nó được triển khai từ giao diện Remote của RMI để cho phép các phương thức của nó
// có thể được gọi từ xa qua mạng bằng cách sử dụng RMI.
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TemperatureListener extends Remote {
    // Phương thức temperatureChanged dùng để thông báo về sự thay đổi nhiệt độ.
    // Đối tượng sử dụng giao diện này sẽ được gọi lại từ máy chủ (server) khi nhiệt độ thay đổi.
    // Đối tượng đăng ký làm người nghe (listener) sẽ nhận thông báo thông qua phương thức này.
    // Tham số newTemperature là giá trị nhiệt độ mới đã thay đổi.
    // Phương thức có thể ném ra ngoại lệ RemoteException nếu có lỗi xảy ra trong quá trình gọi từ xa.
    void temperatureChanged(double newTemperature) throws RemoteException;
}

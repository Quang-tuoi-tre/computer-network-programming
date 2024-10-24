package Input_Output_Stream;
// Package RMICallbacks: Gói (package) chứa các lớp và giao diện liên quan đến việc triển khai RMI với callbacks.

// TemperatureSensorService.java (Giao diện chung)
// Giao diện TemperatureSensorService là một giao diện chia sẻ giữa máy chủ (server) và các máy khách (clients).
// Nó được triển khai từ giao diện Remote của RMI để cho phép các phương thức của nó
// có thể được gọi từ xa qua mạng bằng cách sử dụng RMI.

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TemperatureSensorService extends Remote {
     // Phương thức registerListener dùng để đăng ký một đối tượng làm người nghe (listener)
     // để theo dõi sự thay đổi nhiệt độ. Đối tượng listener sẽ được gọi lại (callback)
     // khi có sự thay đổi nhiệt độ. Tham số listener là một đối tượng cài đặt giao diện TemperatureListener.
     // Phương thức có thể ném ra ngoại lệ RemoteException nếu có lỗi xảy ra trong quá trình gọi từ xa.

     void registerListener(TemperatureListener listener) throws RemoteException;

     // Phương thức unregisterListener dùng để hủy đăng ký một đối tượng làm người nghe (listener).
     // Đối tượng listener không còn được theo dõi sự thay đổi nhiệt độ sau khi hủy đăng ký.
     // Tham số listener là một đối tượng cài đặt giao diện TemperatureListener đã được đăng ký trước đó.
     // Phương thức có thể ném ra ngoại lệ RemoteException nếu có lỗi xảy ra trong quá trình gọi từ xa.

     void unregisterListener(TemperatureListener listener) throws RemoteException;
}


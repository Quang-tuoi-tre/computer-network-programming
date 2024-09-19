package Input_Output_Stream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MaHoaCaesar {
   

   
  
   
   

    public static String maHoa(String text, int step) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) ((c - base + step) % 26 + base));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static String giaiMa(String text, int step) {
        return maHoa(text, -step);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Nhập dữ liệu vào file");
            System.out.println("2. Hiển thị nội dung file input");
            System.out.println("3. Mã hóa");
            System.out.println("4. Giải mã");
            System.out.println("5. Hiển thị nội dung file output");
            System.out.println("6. Thoát");
            System.out.print("Nhập lựa chọn của bạn: ");
            System.out.println();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Nhập dữ liệu từ bàn phím và ghi vào file
                    System.out.print("Nhập nội dung: ");
                    scanner.nextLine(); // Clear the buffer after reading the string input
                    String input = scanner.nextLine();
                    try (FileWriter writer = new FileWriter(inputFile)) {
                        writer.write(input);
                        System.out.println("Đã ghi dữ liệu vào file thành công!");
                    } catch (IOException e) {
                        System.out.println("Lỗi khi ghi vào file: " + e.getMessage());
                    }
                    break;
                case 2:
                    // Đọc nội dung từ file và hiển thị
                    try (Scanner reader = new Scanner(new File(inputFile))) {
                        System.out.println("Nội dung file input:");
                        while (reader.hasNextLine()) {
                            System.out.println(reader.nextLine());
                        }
                    } catch (IOException e) {
                        System.out.println("Lỗi khi đọc file: " + e.getMessage());
                    }
                    break;
                case 3:
                    // Mã hóa
                    try (Scanner reader = new Scanner(new File(inputFile))) {
                        StringBuilder content = new StringBuilder();
                        while (reader.hasNextLine()) {
                            content.append(reader.nextLine());
                        }
                        System.out.print("Nhập số bước dịch: ");
                        int step = scanner.nextInt();
                        String encrypted = maHoa(content.toString(), step);
                        try (FileWriter writer = new FileWriter(outputFile)) {
                            writer.write(encrypted);
                        } catch (IOException e) {
                            System.out.println("Lỗi khi ghi vào file: " + e.getMessage());
                        }
                    } catch (IOException e) {
                        System.out.println("Lỗi khi đọc file: " + e.getMessage());
                    }
                    break;
                case 4:
                    // Giải mã và hoán đổi nội dung file
                    try (Scanner reader = new Scanner(new File(outputFile))) { // Đọc từ file output.txt
                        StringBuilder content = new StringBuilder();
                        while (reader.hasNextLine()) {
                            content.append(reader.nextLine());
                        }
                        System.out.print("Nhập số bước dịch: ");
                        int step = scanner.nextInt();
                        String decrypted = giaiMa(content.toString(), step);
                        try (FileWriter writer = new FileWriter(inputFile)) { // Ghi vào file input.txt
                            writer.write(decrypted);
                            System.out.println("Đã ghi kết quả giải mã vào file input thành công!");
                        } catch (IOException e) {
                            System.out.println("Lỗi khi ghi vào file: " + e.getMessage());
                        }
                    } catch (IOException e) {
                        System.out.println("Lỗi khi đọc file: " + e.getMessage());
                    }
                    break;


                case 5:
                    // Đọc nội dung từ file và hiển thị
                    try (Scanner reader = new Scanner(new File(outputFile))) {
                        System.out.println("Nội dung file output:");
                        while (reader.hasNextLine()) {
                            System.out.println(reader.nextLine());
                        }
                    } catch (IOException e) {
                        System.out.println("Lỗi khi đọc file: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
                case 6:
                    System.exit(0);
            }
        }
    }
}

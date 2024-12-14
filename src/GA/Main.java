/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ACER
 */
public class Main {
    static List<Item> items = new ArrayList<>();
    static int maxWeight = 0;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Thêm danh sách items");
            System.out.println("2. Xóa item");
            System.out.println("3. Giải quyết bài toán cái túi");
            System.out.println("4. Thoát");
            System.out.print("Chọn một chức năng: ");
            
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1: // Thêm danh sách items
                    System.out.println("\n1. Thêm danh sách thủ công");
                    System.out.println("2. Thêm từ file CSV");
                    System.out.print("Chọn một phương án: ");
                    int addChoice = scanner.nextInt();

                    if (addChoice == 1) {
                        // Thêm danh sách thủ công
                        System.out.print("Nhập số lượng items: ");
                        int n = scanner.nextInt();

                        for (int i = 0; i < n; i++) {
                            System.out.print("Nhập weight của item " + (i + 1) + ": ");
                            int weight = scanner.nextInt();
                            System.out.print("Nhập value của item " + (i + 1) + ": ");
                            int value = scanner.nextInt();
                            items.add(new Item(weight, value));
                        }

                        System.out.print("Nhập maxWeight: ");
                        maxWeight = scanner.nextInt();

                        System.out.println("Đã thêm danh sách items.");
                    } else if (addChoice == 2) {
                        // Thêm từ file CSV
                        System.out.print("Nhập đường dẫn file CSV: ");
                        scanner.nextLine(); // Bỏ qua ký tự xuống dòng còn sót từ lần nhập trước
                        String filePath = scanner.nextLine();
                        maxWeight = readCSV(filePath, items); // Gán lại maxWeight từ hàm readCSV

                        if (!items.isEmpty()) {
                            System.out.println("\nDanh sách items đã được thêm:");
                            for (Item item : items) {
                                System.out.println("Weight: " + item.weight + ", Value: " + item.value);
                            }
                            System.out.println("MaxWeight: " + maxWeight);
                        } else {
                            System.out.println("Danh sách items vẫn rỗng. Vui lòng kiểm tra file CSV.");
                        }
                    } else {
                        System.out.println("Lựa chọn không hợp lệ.");
                    }
                    break;

                case 2: // Xóa item
                    System.out.println("\n1. Xóa một item");
                    System.out.println("2. Xóa tất cả items");
                    System.out.print("Chọn một phương án: ");
                    int deleteChoice = scanner.nextInt();

                    if (deleteChoice == 1) {
                        System.out.print("Nhập chỉ số của item cần xóa (bắt đầu từ 0): ");
                        int index = scanner.nextInt();
                        if (index >= 0 && index < items.size()) {
                            items.remove(index);
                            System.out.println("Đã xóa item tại vị trí " + index + ".");
                        } else {
                            System.out.println("Chỉ số không hợp lệ.");
                        }
                    } else if (deleteChoice == 2) {
                        items.clear();
                        System.out.println("Đã xóa tất cả items.");
                    } else {
                        System.out.println("Lựa chọn không hợp lệ.");
                    }
                    break;

                case 3: // Giải quyết bài toán cái túi
                    if (items.isEmpty()) {
                        System.out.println("Danh sách items rỗng. Vui lòng thêm items trước.");
                        break;
                    }
                    if (maxWeight == 0) {
                        System.out.println("maxWeight chưa được đặt. Vui lòng nhập hoặc đọc từ file CSV.");
                        break;
                    }

                    GeneticAlgorithm ga = new GeneticAlgorithm(items, maxWeight);
                    KnapSack bestKnapsack = ga.run();

                    System.out.println("Best fitness: " + bestKnapsack.fitness);
                    System.out.print("Selected items: ");
                    for (int i = 0; i < bestKnapsack.genes.length; i++) {
                        if (bestKnapsack.genes[i]) {
                            System.out.print("(" + items.get(i).weight + ", " + items.get(i).value + ") ");
                        }
                    }
                    System.out.println();
                    break;

                case 4: // Thoát
                    System.out.println("Thoát chương trình.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    public static int readCSV(String filePath, List<Item> items) {
        int maxWeight = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Để đọc maxWeight từ dòng đầu tiên
            boolean isHeaderLine = true; // Bỏ qua dòng tiêu đề items
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Tách dữ liệu bằng dấu phẩy
                if (isFirstLine) {
                    // Dòng đầu tiên là maxWeight
                    maxWeight = Integer.parseInt(values[1].trim());
                    isFirstLine = false;
                } else if (isHeaderLine) {
                    // Dòng thứ hai là tiêu đề (weight, value), bỏ qua
                    isHeaderLine = false;
                } else {
                    // Các dòng tiếp theo là items
                    int weight = Integer.parseInt(values[0].trim());
                    int value = Integer.parseInt(values[1].trim());
                    items.add(new Item(weight, value));
                }
            }
            System.out.println("Đã thêm items và maxWeight từ file CSV thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("File CSV không đúng định dạng: " + e.getMessage());
        }
        return maxWeight;
    }
}


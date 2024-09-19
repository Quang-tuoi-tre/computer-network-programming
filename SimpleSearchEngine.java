import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class SimpleSearchEngine {
    private static Map<String, Integer> keywordCountMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Nhập địa chỉ website ban đầu: ");
        String startUrl = scanner.nextLine();

        System.out.println("Nhập độ sâu tìm kiếm: ");
        int searchDepth = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        System.out.println("Nhập từ khóa cần tìm: ");
        String keyword = scanner.nextLine();

        Set<String> visitedLinks = new HashSet<>();
        crawlWebsite(startUrl, keyword, searchDepth, visitedLinks);

        List<Map.Entry<String, Integer>> sortedResults = sortResultsByCount(keywordCountMap);
        saveResultsToFile(sortedResults, keyword);

        System.out.println("Tìm kiếm hoàn tất. Kết quả đã được lưu.");
        scanner.close();
    }

    // Crawler để lập danh sách các đường link từ trang ban đầu
    public static void crawlWebsite(String url, String keyword, int depth, Set<String> visitedLinks) {
        if (depth == 0 || visitedLinks.contains(url)) return;
        visitedLinks.add(url);

        try {
            Document doc = Jsoup.connect(url).get();
            String pageContent = doc.text().toLowerCase();

            // Đếm số lần từ khóa xuất hiện trong trang
            int count = countKeywordInPage(pageContent, keyword.toLowerCase());
            if (count > 0) {
                keywordCountMap.put(url, count);
            }

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.absUrl("href");
                if (!visitedLinks.contains(absUrl)) {
                    crawlWebsite(absUrl, keyword, depth - 1, visitedLinks);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi truy cập vào URL: " + url);
        }
    }

    // Đếm số lần từ khóa xuất hiện trong trang
    public static int countKeywordInPage(String content, String keyword) {
        int count = 0;
        Pattern pattern = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            count++;
        }
        return count;
    }

    // Sắp xếp kết quả theo số lần từ khóa xuất hiện giảm dần
    public static List<Map.Entry<String, Integer>> sortResultsByCount(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return list;
    }

    // Lưu kết quả vào file
    public static void saveResultsToFile(List<Map.Entry<String, Integer>> results, String keyword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("search_results.txt"))) {
            writer.write("Kết quả tìm kiếm từ khóa: " + keyword);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : results) {
                writer.write("URL: " + entry.getKey() + " - Số lần xuất hiện: " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu kết quả vào file.");
        }
    }
}

import java.io.*;
import java.net.*;
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
            Document doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                            .timeout(10000)
                            .get();
        
            // Lấy nội dung từ các thẻ có ý nghĩa
            String pageContent = doc.select("body").text() + " " + doc.select("title").text() + " " + doc.select("meta[name=description]").attr("content");
            pageContent = pageContent.toLowerCase();

            // Đếm số lần từ khóa xuất hiện trong trang
            int count = countKeywordInPage(pageContent, keyword.toLowerCase());
            if (count > 0) {
                keywordCountMap.put(url, count);
                System.out.println("Found " + count + " occurrences of '" + keyword + "' at: " + url); // Debug output
            }

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.absUrl("href");

                // Bỏ qua những URL không hợp lệ hoặc ngoài domain
                if (isValidUrl(absUrl) && isSameDomain(url, absUrl) && !visitedLinks.contains(absUrl)) {
                    crawlWebsite(absUrl, keyword, depth - 1, visitedLinks);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi truy cập vào URL: " + url);
        }
    }

    private static boolean isValidUrl(String url) {
        return url.startsWith("http") && !url.contains("javascript:") && !url.endsWith(".pdf");
    }

    private static boolean isSameDomain(String baseUrl, String url) {
        try {
            String baseDomain = new URL(baseUrl).getHost();
            String urlDomain = new URL(url).getHost();
            return urlDomain.equals(baseDomain) || urlDomain.endsWith("." + baseDomain);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    // Đếm số lần từ khóa xuất hiện trong trang
    public static int countKeywordInPage(String content, String keyword) {
        int count = 0;
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE);
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
 /* private static final int MAX_DEPTH = 2; // Độ sâu tìm kiếm mặc định
    private static Set<String> visitedUrls = new HashSet<>();
    private static Map<String, Integer> searchResults = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String url1, String url2) {
            return searchResults.get(url2) - searchResults.get(url1);
        }
    });

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Nhập địa chỉ website ban đầu và độ sâu tìm kiếm
        System.out.print("Nhập địa chỉ website ban đầu: ");
        String startUrl = scanner.nextLine();
        System.out.print("Nhập độ sâu tìm kiếm (mặc định: " + MAX_DEPTH + "): ");
        int depth = scanner.hasNextInt() ? scanner.nextInt() : MAX_DEPTH;

        // Nhập từ khóa cần tìm kiếm
        System.out.print("Nhập từ khóa cần tìm kiếm: ");
        String keyword = scanner.next();

        // Bắt đầu quá trình crawl và tìm kiếm
        crawl(startUrl, depth, keyword);

        // Lưu kết quả vào file
        try (PrintWriter writer = new PrintWriter(new FileWriter("searchResults.txt"))) {
            for (Map.Entry<String, Integer> entry : searchResults.entrySet()) {
                writer.println(entry.getKey() + " (" + entry.getValue() + " lần)");
            }
        }
    }

    private static void crawl(String url, int depth, String keyword) throws IOException {
        if (depth <= 0 || visitedUrls.contains(url)) {
            return;
        }

        visitedUrls.add(url);

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

            for (Element link : links)
            {
                String nextLink = link.absUrl("href");
                crawl(nextLink, depth - 1, keyword);
            }

            // Tìm kiếm từ khóa trong nội dung trang
            int count = doc.body().text().toLowerCase().split("\\s+").length - doc.body().text().toLowerCase().replaceAll(keyword.toLowerCase(), "").split("\\s+").length;
            if (count > 0) {
                Integer existingCount = searchResults.get(url); // Get existing count
                count = existingCount != null ? count + existingCount : count; // Add existing count if not null
                searchResults.put(url, count);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi crawl: " + e.getMessage());
        }
    }*/
}

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    private Set<String> visitedUrls;

    public WebCrawler() {
        visitedUrls = new HashSet<>();
    }

    public void crawlWebsite(String url, int depth) {
        visitedUrls.clear();
        crawlUrl(url, depth);
    }

    private void crawlUrl(String url, int depth) {
        visitedUrls.add(url);

        // Your crawling logic here

        if (depth > 0) {
            Set<String> childUrls = getChildUrls(url);
            Iterator<String> iterator = childUrls.iterator();
            while (iterator.hasNext()) {
                String childUrl = iterator.next();
                if (visitedUrls.contains(childUrl)) {
                    iterator.remove();
                }
            }
            for (String childUrl : childUrls) {
                crawlUrl(childUrl, depth - 1);
            }
        }
    }

    private Set<String> getChildUrls(String url) {
        Set<String> childUrls = new HashSet<>();

        try {
            URL website = new URL(url);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(website.openStream()));
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }

            bufferedReader.close();

            // Regular expression pattern to match URLs
            Pattern pattern = Pattern.compile("<a\\s+href\\s*=\\s*\"(.*?)\"");
            Matcher matcher = pattern.matcher(content.toString());

            while (matcher.find()) {
                String childUrl = matcher.group(1);

                // Normalize and validate the child URL
                childUrl = normalizeUrl(childUrl, url);

                if (childUrl != null) {
                    childUrls.add(childUrl);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while extracting child URLs: " + e.getMessage());
        }

        return childUrls;
    }

    private String normalizeUrl(String childUrl, String parentUrl) {
        try {
            URL base = new URL(parentUrl);
            URL absolute = new URL(base, childUrl);

            // Remove fragments from the URL
            String normalizedUrl = absolute.getProtocol() + "://" + absolute.getHost() + absolute.getPath();

            // Optionally, remove query parameters from the URL
            // normalizedUrl = normalizedUrl.split("\\?")[0];

            return normalizedUrl;
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + childUrl);
        }

        return null; // Return null for invalid URLs
    }
    public void saveVisitedUrlsToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String url : visitedUrls) {
                writer.write(url + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving visited URLs to file: " + e.getMessage());
        }
    }

    private static String getWebsiteName(String websiteUrl) {
        // Extract the website name from the URL (e.g., "https://hutech.edu.vn" -> "hutech")
        String[] parts = websiteUrl.split("//");
        String domain = parts[1].split("/")[0];
        String[] domainParts = domain.split("\\.");
        return domainParts[0];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the website URL: ");
        String websiteUrl = scanner.nextLine();

        System.out.print("Enter the crawl depth: ");
        int crawlDepth = scanner.nextInt();

        scanner.nextLine(); // Consume the newline character

        String websiteName = getWebsiteName(websiteUrl);
        String outputFileName = websiteName + "_urls.txt";

        System.out.println("Crawling " + websiteName + "...");
        WebCrawler webCrawler = new WebCrawler();
        webCrawler.crawlWebsite(websiteUrl, crawlDepth);
        webCrawler.saveVisitedUrlsToFile(outputFileName);

        System.out.println("Visited URLs saved to file: " + outputFileName);
    }


}

package LTM_04;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexExample {
    public static void main(String[] args) {
        String input = "Hello 1234 xyz J09ava fg78 9876";

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            System.out.println("Match: " + match);
            System.out.println("Start index: " + start);
            System.out.println("End index: " + end);
        }
    }
}

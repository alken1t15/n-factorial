import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

public class NGram {
    private static String PATH_TO_FILE = "names.txt";
    private static int BIGRAM = 2;

    private String alphabet = "^abcdefghijklmnopqrstuvwxyz$";

    public void start() throws IOException {
        List<String> lines = readFile(PATH_TO_FILE);
        Map<String, Integer> bigramProbability = generateBigram(lines);
        printProbability(bigramProbability);
    }

    private void printProbability(Map<String, Integer> bigramProbability) {
        String tableFormat = "%-10s" + " %-7s".repeat(alphabet.length());

        List<String> header = new ArrayList<>();
        header.add("end with");
        header.addAll(List.of(alphabet.split("")));
        System.out.printf((tableFormat) + "%n", header.toArray());

        for (String s: alphabet.split("")) {
            ArrayList<String> row = new ArrayList<>();
            row.add(s);

            int sumOfCount = bigramProbability.entrySet()
                    .stream()
                    .filter(x -> x.getKey().startsWith(s))
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            for (String c: alphabet.split("")) {
                String bgKey = s + c;
                if (bigramProbability.containsKey(bgKey))
                    row.add(new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
                            .format((bigramProbability.get(bgKey) * 1.0) / sumOfCount));
                else
                    row.add("0.00");
            }

            System.out.printf((tableFormat) + "%n", row.toArray());
        }
    }

    private Map<String, Integer> generateBigram(List<String> lines) {
        return lines.stream()
                .map(s -> "^"+s+"$")
                .map(str -> getCurrentWordNgram(BIGRAM, str))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(x -> x, x -> 1, Integer::sum));
    }

    private List<String> getCurrentWordNgram(int n, String str) {
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i < str.length() - n + 1; i++)
            ngrams.add(str.substring(i, i + n));
        return ngrams;
    }

    private List<String> readFile(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    public static void main(String[] args) throws IOException {
        new NGram().start();
    }
}

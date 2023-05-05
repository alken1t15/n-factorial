import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NameGenerator {

    private HashMap<String, Integer> bigrams;
    private ArrayList<String> letters;
    private Random random;

    public NameGenerator() {
        bigrams = new HashMap<>();
        letters = new ArrayList<>();
        random = new Random();
    }

    public void readDataFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.toLowerCase();
                for (int i = 0; i < line.length() - 1; i++) {
                    String bigram = line.substring(i, i + 2);
                    if (bigrams.containsKey(bigram)) {
                        int count = bigrams.get(bigram);
                        bigrams.put(bigram, count + 1);
                    } else {
                        bigrams.put(bigram, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculateBigramProbabilities() {
        int totalBigrams = 0;
        for (int count : bigrams.values()) {
            totalBigrams += count;
        }
        for (String bigram : bigrams.keySet()) {
            int count = bigrams.get(bigram);
            double probability = (double) count / totalBigrams;
            System.out.println(bigram + " " + probability);
        }
    }

    public void generateLettersFromBigrams() {
        for (String bigram : bigrams.keySet()) {
            String letter = bigram.substring(0, 1);
            if (!letters.contains(letter)) {
                letters.add(letter);
            }
        }
    }

    public String generateName(int length) {
        String name = "";
        String currentLetter = getRandomLetter();
        name += currentLetter;
        for (int i = 1; i < length; i++) {
            String nextLetter = getNextLetter(currentLetter);
            name += nextLetter;
            currentLetter = nextLetter;
        }
        return name;
    }

    private String getRandomLetter() {
        int index = random.nextInt(letters.size());
        return letters.get(index);
    }

    private String getNextLetter(String currentLetter) {
        ArrayList<String> possibleNextLetters = new ArrayList<>();
        for (String bigram : bigrams.keySet()) {
            String letter = bigram.substring(0, 1);
            if (letter.equals(currentLetter)) {
                possibleNextLetters.add(bigram.substring(1));
            }
        }
        int index = random.nextInt(possibleNextLetters.size());
        return possibleNextLetters.get(index);
    }

    public static void main(String[] args) {
        NameGenerator nameGenerator = new NameGenerator();
        nameGenerator.readDataFromFile("names.txt");
        nameGenerator.calculateBigramProbabilities();
        nameGenerator.generateLettersFromBigrams();
        String name = nameGenerator.generateName(10);
        System.out.println("Generated name: " + name);
    }

}
package lab1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bartosz Dębowski & Igor Toboja
 */

public class TextAnalyzer {

    static class WordByLength implements Comparable<WordByLength> {

        String word;

        public WordByLength(String word) {
                this.word = word;
        }

        @Override
        public int compareTo(WordByLength other) {
            return Integer.compare(this.word.length(), other.word.length());
        }

        @Override
        public String toString() {
            return word + " (dł: " + word.length() + ")";
        }
    }

        static class WordByLexico implements Comparable<WordByLexico> {
            String word;

            public WordByLexico(String word) {
                this.word = word;
            }

            @Override
            public int compareTo(WordByLexico other) {
                return this.word.compareTo(other.word);
            }

            @Override
            public String toString() {
                return word;
            }
        }

        static class WordByFreq implements Comparable<WordByFreq> {
            String word;
            int frequency;

            public WordByFreq(String word, int frequency) {
                this.word = word;
                this.frequency = frequency;
            }

            @Override
            public int compareTo(WordByFreq other) {
                return Integer.compare(this.frequency, other.frequency);
            }

            @Override
            public String toString() {
                return word + " (wystąpień: " + frequency + ")";
            }
        }

        public static void main(String[] args) {

            String filePath = "lab1/pustynia.txt";
            String content = "";

            try {
                content = Files.readString(Paths.get(filePath));
            } catch (IOException e) {
                System.err.println("Nie udało się wczytać pliku: " + filePath);
                return;
            }

            String[] rawWords = content.toLowerCase().replaceAll("[^a-ząćęłńóśźż]+", " ").split("\\s+");

            Map<String, Integer> wordCounts = new HashMap<>();

            MinMaxHeap<WordByLength> lengthHeap = new MinMaxHeap<>();
            MinMaxHeap<WordByLexico> lexicoHeap = new MinMaxHeap<>();
            MinMaxHeap<WordByFreq> freqHeap = new MinMaxHeap<>();

            for (String word : rawWords) {
                if (word.isEmpty()) continue;

                lengthHeap.insert(new WordByLength(word));
                lexicoHeap.insert(new WordByLexico(word));

                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                freqHeap.insert(new WordByFreq(entry.getKey(), entry.getValue()));
            }

            System.out.println("Liczba przeanalizowanych słów: " + rawWords.length + "\n");

            System.out.println("1. Długość słów:");
            System.out.println("   Najkrótsze: " + lengthHeap.findMinimum());
            System.out.println("   Najdłuższe: " + lengthHeap.findMaximum());

            System.out.println("\n2. Porządek leksykograficzny:");
            System.out.println("   Pierwsze słowo (A-Z): " + lexicoHeap.findMinimum());
            System.out.println("   Ostatnie słowo (A-Z): " + lexicoHeap.findMaximum());

            System.out.println("\n3. Częstotliwość występowania:");
            System.out.println("   Najrzadziej: " + freqHeap.findMinimum());
            System.out.println("   Najczęściej: " + freqHeap.findMaximum());
        }
    }


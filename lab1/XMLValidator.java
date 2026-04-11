package lab1;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa narzędziowa służąca do walidacji poprawności zagnieżdżenia
 * i domknięcia wybranych znaczników w pliku XML.
 * Wykorzystuje strukturę stosu (LIFO) do śledzenia otwartych znaczników.
 */
public class XMLValidator {

    /**
     * Główna metoda testująca działanie walidatora na przykładowych plikach.
     * * @param args Argumenty wiersza poleceń (nieużywane).
     * @throws Exception W przypadku błędu wejścia/wyjścia podczas odczytu plików.
     */
    public static void main(String[] args) throws Exception {
        Set<String> tags = loadTags("src/lab1/test1.txt");

        boolean isValid = checkXML("src/lab1/test1fail.xml", tags);
        System.out.println("Is the wrong XML valid? " + isValid);

        boolean isValid2 = checkXML("src/lab1/test1.xml", tags);
        System.out.println("Is the XML valid? " + isValid2);
    }

    /**
     * Wczytuje z pliku konfiguracyjnego listę znaczników XML,
     * które mają podlegać walidacji. Plik musi być w formacie obsługiwanym
     * przez klasę java.util.Properties.
     *
     * @param file Ścieżka do pliku z właściwościami (np. test1.txt).
     * @return Zbiór (Set) zawierający nazwy znaczników do śledzenia.
     * @throws Exception W przypadku błędu odczytu pliku.
     */
    private static Set<String> loadTags(String file) throws Exception {
        Set<String> tags = new HashSet<>();
        Properties props = new Properties();
        props.load(new FileInputStream(file));

        int count = Integer.parseInt(props.getProperty("count"));
        String prefix = props.getProperty("prefix");
        int start = Integer.parseInt(props.getProperty("start"));

        for (int i = 0; i < count; i++) {
            tags.add(props.getProperty(prefix + (start + i)));
        }

        return tags;
    }

    /**
     * Sprawdza, czy w podanym pliku XML znaczniki zdefiniowane na liście
     * są poprawnie zagnieżdżone i domknięte. Znaczniki niewymienione
     * na liście są ignorowane podczas sprawdzania.
     *
     * @param file Ścieżka do pliku XML podlegającego walidacji.
     * @param tags Zbiór znaczników, których poprawność ma zostać sprawdzona.
     * @return true, jeśli wszystkie śledzone znaczniki są poprawnie domknięte i zagnieżdżone;
     * false w przypadku wykrycia błędu strukturalnego.
     * @throws Exception W przypadku błędu odczytu pliku.
     */
    private static boolean checkXML(String file, Set<String> tags) throws Exception {
        Stack<String> stack = new Stack<>();
        Pattern pattern = Pattern.compile("<(/)?([a-zA-Z0-9_]+)[^>]*>");
        Scanner scanner = new Scanner(new FileInputStream(file));

        while (scanner.hasNextLine()) {
            Matcher matcher = pattern.matcher(scanner.nextLine());

            while (matcher.find()) {
                boolean isClosing = matcher.group(1) != null;
                String tagName = matcher.group(2);

                if (tags.contains(tagName)) {
                    if (!isClosing) {
                        stack.push(tagName);
                    } else {
                        if (stack.isEmpty() || !stack.pop().equals(tagName)) {
                            return false;
                        }
                    }
                }
            }
        }

        return stack.isEmpty();
    }
}
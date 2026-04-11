package lab1;

/**
 * Reprezentuje kolejkę priorytetową obsługującą pacjentów na Szpitalnym Oddziale Ratunkowym (SOR).
 * Struktura oparta jest na jednokierunkowej liście wiązanej i zapewnia, że pacjenci
 * z wyższym priorytetem (niższą wartością liczbową) są obsługiwani w pierwszej kolejności.
 * W przypadku pacjentów o tym samym priorytecie, zachowywana jest kolejność zgłoszeń (zasada FIFO).
 *
 * @author Bartosz Dębowski & Igor Toboja
 */
public class PriorityQueue {

    private Node first;

    /**
     * Inicjalizuje nową, pustą kolejkę priorytetową.
     */
    public PriorityQueue() {
        this.first = null;
    }

    /**
     * Dodaje nowego pacjenta do kolejki z uwzględnieniem jego priorytetu.
     * Pacjent jest umieszczany za wszystkimi pacjentami o wyższym lub równym
     * priorytecie, zachowując stabilność kolejki.
     *
     * @param p Obiekt pacjenta, który ma zostać dodany do kolejki.
     */
    public void push(Patient p) {
        Node patient = new Node(p);
        if (first == null) {
            first = patient;
        } else {
            Node current = first;
            // Sprawdzenie, czy nowy pacjent ma wyższy priorytet niż pierwszy na liście
            if (current.getKey() > patient.getKey()) {
                patient.setNext(current);
                first = patient;
            } else {
                // Szukanie odpowiedniego miejsca w dalszej części listy
                while (current.getNext() != null) {
                    if (patient.getKey() < current.getNext().getKey()) {
                        patient.setNext(current.getNext());
                        current.setNext(patient);
                        return;
                    }
                    current = current.getNext();
                }
                // Jeśli ma najniższy priorytet, ląduje na samym końcu
                current.setNext(patient);
            }
        }
    }

    /**
     * Zwraca napisową postać stanu całej kolejki.
     * @return Tekstowa reprezentacja kolejki, w której każdy pacjent
     * jest wypisany w nowej linii, zaczynając od pierwszego do obsłużenia.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;

        if (current == null) {
            return "Kolejka jest pusta.";
        }

        while (current != null) {
            sb.append(current.getPatient().toString()).append("\n");
            current = current.getNext();
        }

        // Usunięcie ostatniego, niepotrzebnego znaku nowej linii
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    /**
     * Pobiera i usuwa z kolejki pierwszego pacjenta o najwyższym aktualnie priorytecie.
     * Jest to pacjent znajdujący się na samym początku struktury listy wiązanej.
     *
     * @return Obiekt pacjenta o najwyższym priorytecie lub null, jeśli kolejka jest pusta.
     */
    public Patient pop() {
        if (first == null) {
            return null;
        }

        Node oldFirst = first;
        first = first.getNext();
        return oldFirst.getPatient();
    }
}
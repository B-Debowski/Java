package lab1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


    public class PriorityQueueTest {

        @Test
        public void testPushAndPopHighestPriority() {
            PriorityQueue pq = new PriorityQueue();
            Patient p1 = new Patient(3, "Zwykly", "Kowalski", 1);
            Patient p2 = new Patient(1, "Pilny", "Nowak", 2);

            pq.push(p1);
            pq.push(p2);

            Patient firstToServe = pq.pop();

            assertEquals(p2, firstToServe, "Pacjent z priorytetem 1 powinien być pierwszy w kolejce!");
        }

        @Test
        public void testFIFOForSamePriority() {
            PriorityQueue pq = new PriorityQueue();
            Patient p1 = new Patient(2, "Igor", "Toboja", 1);
            Patient p2 = new Patient(2, "Juan", "Lucas", 2);

            pq.push(p1);
            pq.push(p2);

            Patient firstToServe = pq.pop();
            Patient secondToServe = pq.pop();

            assertEquals(p1, firstToServe, "Igor przyszedł pierwszy, powinien wejść przed Juanem.");
            assertEquals(p2, secondToServe, "Juan powinien być obsłużony jako drugi.");
        }

        @Test
        public void testPopOnEmptyQueue() {
            PriorityQueue pq = new PriorityQueue();

            Patient p = pq.pop();

            assertNull(p, "Pobieranie z pustej kolejki powinno zwracać null.");
        }

        @Test
        public void testComplexQueueSorting() {
            PriorityQueue pq = new PriorityQueue();
            Patient p1 = new Patient(4, "A", "A", 1);
            Patient p2 = new Patient(1, "B", "B", 2);
            Patient p3 = new Patient(2, "C", "C", 3);
            Patient p4 = new Patient(1, "D", "D", 4);

            pq.push(p1);
            pq.push(p2);
            pq.push(p3);
            pq.push(p4);

            assertEquals(p2, pq.pop());
            assertEquals(p4, pq.pop());
            assertEquals(p3, pq.pop());
            assertEquals(p1, pq.pop());
        }
    }

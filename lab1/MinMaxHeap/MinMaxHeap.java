package lab1;

import java.util.ArrayList;

/**
 * Generyczna kolejka priorytetowa zrealizowana na kopcu Min-Max.
 * Znajduje minimum i maksimum w czasie stałym O(1) oraz przetwarza
 * operacje dodawania i usuwania w czasie logarytmicznym O(log n).
 * * @param <T> Typ elementów przechowywanych w kopcu (musi implementować Comparable).
 */
public class MinMaxHeap<T extends Comparable<T>> {

    private ArrayList<T> kopiec;

    public MinMaxHeap() {
        this.kopiec = new ArrayList<>();
    }

    /**
     * Zwraca najmniejszy element ze zbioru.
     * Czas realizacji wynosi O(1), ponieważ minimum znajduje się
     * zawsze w korzeniu kopca (indeks 0), co zapewnia natychmiastowy odczyt.
     * * @return Najmniejszy element lub null, jeśli kopiec jest pusty.
     */
    public T findMinimum() {
        if (kopiec.isEmpty()) return null;
        return kopiec.get(0);
    }

    /**
     * Zwraca największy element ze zbioru.
     * Czas realizacji wynosi O(1), ponieważ maksimum zawsze znajduje się
     * na jednym z dwójki dzieci korzenia (indeks 1 lub 2), co wymaga tylko jednego porównania.
     * * @return Największy element lub null, jeśli kopiec jest pusty.
     */
    public T findMaximum() {
        if (kopiec.isEmpty()) return null;
        if (kopiec.size() == 1) return kopiec.get(0);
        if (kopiec.size() == 2) return kopiec.get(1);

        if (kopiec.get(1).compareTo(kopiec.get(2)) >= 0) {
            return kopiec.get(1);
        } else {
            return kopiec.get(2);
        }
    }

    /**
     * Wstawia nowy element do kopca.
     * Czas realizacji wynosi O(log n), ponieważ w najgorszym przypadku element
     * jest "przesiewany" w górę tylko wzdłuż wysokości drzewa.
     * * @param element Wstawiany element.
     */
    public void insert(T element) {
        kopiec.add(element);
        int index = kopiec.size() - 1;
        siftUp(index);
    }

    /**
     * Usuwa i zwraca najmniejszy element z kopca.
     * Czas realizacji wynosi O(log n), ponieważ po usunięciu minimum w jego miejsce
     * wędruje ostatni element, który "spada" odtwarzając kopiec (Sift-Down).
     * * @return Najmniejszy element lub null.
     */
    public T extractMin() {
        if (kopiec.isEmpty()) return null;
        T minimum = kopiec.get(0);

        if (kopiec.size() == 1) {
            kopiec.remove(0);
            return minimum;
        }

        T last = kopiec.remove(kopiec.size() - 1);
        kopiec.set(0, last);
        siftDown(0);
        return minimum;
    }

    /**
     * Usuwa i zwraca największy element z kopca.
     * Czas realizacji wynosi O(log n), uzasadnienie analogiczne do extractMin().
     * * @return Największy element lub null.
     */
    public T extractMax() {
        if (kopiec.isEmpty()) return null;
        if (kopiec.size() == 1) return extractMin();
        if (kopiec.size() == 2) return kopiec.remove(1);

        int indexMax = 1;
        if (kopiec.size() > 2 && kopiec.get(2).compareTo(kopiec.get(1)) > 0) {
            indexMax = 2;
        }

        T maximum = kopiec.get(indexMax);
        T last = kopiec.remove(kopiec.size() - 1);

        if (indexMax < kopiec.size()) {
            kopiec.set(indexMax, last);
            siftDown(indexMax);
        }
        return maximum;
    }

    private void siftUp(int i) {
        if (i == 0) return;
        int parent = (i - 1) / 2;

        if (isLevelMin(i)) {
            if (kopiec.get(i).compareTo(kopiec.get(parent)) > 0) {
                swap(i, parent);
                siftUpMax(parent);
            } else {
                siftUpMin(i);
            }
        } else {
            if (kopiec.get(i).compareTo(kopiec.get(parent)) < 0) {
                swap(i, parent);
                siftUpMin(parent);
            } else {
                siftUpMax(i);
            }
        }
    }

    private void siftUpMin(int i) {
        if (i > 2) {
            int parent = (i - 1) / 2;
            int grandParent = (parent - 1) / 2;

            if (kopiec.get(i).compareTo(kopiec.get(grandParent)) < 0) {
                swap(i, grandParent);
                siftUpMin(grandParent);
            }
        }
    }

    private void siftUpMax(int i) {
        if (i > 2) {
            int parent = (i - 1) / 2;
            int grandParent = (parent - 1) / 2;

            if (kopiec.get(i).compareTo(kopiec.get(grandParent)) > 0) {
                swap(i, grandParent);
                siftUpMax(grandParent);
            }
        }
    }

    private void siftDown(int i) {
        if (isLevelMin(i)) {
            siftDownMin(i);
        } else {
            siftDownMax(i);
        }
    }

    private void siftDownMin(int i) {
        int minChild = getMinDescendant(i);

        if (minChild != -1) {
            if (kopiec.get(minChild).compareTo(kopiec.get(i)) < 0) {
                swap(i, minChild);

                if (minChild > 2 * i + 2) {
                    int parentOfGrandchild = (minChild - 1) / 2;
                    if (kopiec.get(minChild).compareTo(kopiec.get(parentOfGrandchild)) > 0) {
                        swap(minChild, parentOfGrandchild);
                    }
                    siftDownMin(minChild);
                }
            }
        }
    }

    private void siftDownMax(int i) {
        int maxChild = getMaxDescendant(i);

        if (maxChild != -1) {
            if (kopiec.get(maxChild).compareTo(kopiec.get(i)) > 0) {
                swap(i, maxChild);

                if (maxChild > 2 * i + 2) {
                    int parentOfGrandchild = (maxChild - 1) / 2;
                    if (kopiec.get(maxChild).compareTo(kopiec.get(parentOfGrandchild)) < 0) {
                        swap(maxChild, parentOfGrandchild);
                    }
                    siftDownMax(maxChild);
                }
            }
        }
    }

    private int getMinDescendant(int i) {
        int minIndex = -1;
        int[] descendants = {2 * i + 1, 2 * i + 2, 4 * i + 3, 4 * i + 4, 4 * i + 5, 4 * i + 6};

        for (int idx : descendants) {
            if (idx < kopiec.size()) {
                if (minIndex == -1 || kopiec.get(idx).compareTo(kopiec.get(minIndex)) < 0) {
                    minIndex = idx;
                }
            }
        }
        return minIndex;
    }

    private int getMaxDescendant(int i) {
        int maxIndex = -1;
        int[] descendants = {2 * i + 1, 2 * i + 2, 4 * i + 3, 4 * i + 4, 4 * i + 5, 4 * i + 6};

        for (int idx : descendants) {
            if (idx < kopiec.size()) {
                if (maxIndex == -1 || kopiec.get(idx).compareTo(kopiec.get(maxIndex)) > 0) {
                    maxIndex = idx;
                }
            }
        }
        return maxIndex;
    }

    private void swap(int i, int j) {
        T temp = kopiec.get(i);
        kopiec.set(i, kopiec.get(j));
        kopiec.set(j, temp);
    }

    private boolean isLevelMin(int index) {
        int depth = 31 - Integer.numberOfLeadingZeros(index + 1);
        return depth % 2 == 0;
    }

    public int size() {
        return kopiec.size();
    }
}

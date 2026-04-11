package lab1;

/**
 * @author Bartosz Dębowski & Igor Toboja
 */
public class Node {
    private Patient patient;
    private int key;
    private Node next;
    public Node (Patient p){
        this.key = p.getPriority();
        this.patient = p;
    }
    public Node getNext() {
        return next;
    }
    public int getKey() {
        return key;
    }
    public void setNext(Node next) {
        this.next = next;
    }
    public Patient getPatient() {
        return patient;
    }
}

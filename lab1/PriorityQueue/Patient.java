package lab1;

import java.util.Objects;

/**
 * @author Bartosz Dębowski & Igor Toboja
 */
public class Patient {
    private int priority;
    private String firstName;
    private String lastName;
    private int id;
    public Patient(int priority, String firstName, String lastName, int id) {
        if (priority <1 || priority > 4) {
            throw new IllegalArgumentException("Invalid priority");
        }
        this.priority = priority;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Patient [ First Name: " + firstName + ", Last Name: " + lastName + " priority: " + priority + " id: " + id + "]";
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public int getPriority() {
        return priority;
    }

}

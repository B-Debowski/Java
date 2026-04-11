package lab1;

import java.util.Scanner;

/**
 * @author Bartosz Dębowski & Igor Toboja
 */
public class SOR {
    PriorityQueue pq;
    public SOR() {
        this.pq=new PriorityQueue();
    }

        public static void main (String[] args) {
        SOR sor = new SOR();
            Patient p1 = new Patient(2,"Igor","Toboja",1 );
            Patient p2 = new Patient(3,"Juan","Lucas",2 );
            Patient p3 = new Patient(4,"Bartosz","Debowski",3 );
            Patient p4 = new Patient(1,"Krysia","Ratko",4 );
            Patient p5 = new Patient(2,"Dan","Smith",5 );
            sor.pq.push(p1);
            sor.pq.push(p2);
            sor.pq.push(p3);
            sor.pq.push(p4);
            sor.pq.push(p5);
            System.out.println(sor.pq);
            System.out.println();
            sor.pq.pop();
            System.out.println(sor.pq);

        }
    }


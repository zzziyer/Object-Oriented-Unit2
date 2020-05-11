import com.oocourse.elevator1.PersonRequest;

public class Passenger extends Thread {
    private Elevator elevator;
    private PersonRequest personRequest;

    public Passenger(Elevator e, PersonRequest a) {
        this.elevator = e;
        this.personRequest = a;
    }

    public void run() {
        try {
            elevator.carry(personRequest);
        } catch (Exception e) {
            //System.out.print("");
        }
    }
}

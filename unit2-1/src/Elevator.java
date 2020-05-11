import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class Elevator {
    private int stairs;

    public Elevator() {
        this.stairs = 1;
    }

    public synchronized void carry(PersonRequest p) throws Exception {


        int from = p.getFromFloor();

        Thread.sleep(Math.abs(stairs - from) * 500);//从之前停留的层到请求层

        int id = p.getPersonId();
        TimableOutput.println("OPEN-" + from);
        Thread.sleep(250);//开门
        TimableOutput.println("IN-" + id + "-" + from);
        Thread.sleep(250);//关门
        TimableOutput.println("CLOSE-" + from);

        int to = p.getToFloor();
        Thread.sleep(Math.abs(from - to) * 500);//到目的层

        TimableOutput.println("OPEN-" + to);
        Thread.sleep(250);//开门
        TimableOutput.println("OUT-" + id + "-" + to);
        Thread.sleep(250);//关门
        TimableOutput.println("CLOSE-" + to);
        stairs = to;
        notifyAll();
    }
}

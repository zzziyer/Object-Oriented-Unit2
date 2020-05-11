import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueue requestQueue = new RequestQueue();
        Elevator newElevator = new Elevator(requestQueue);
        Input input = new Input(requestQueue);
        //System.out.println("here111");
        input.start();
        //System.out.println("here");
        newElevator.start();
    }
}

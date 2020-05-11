import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Elevator newElevator = new Elevator();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                new Passenger(newElevator, request).start();
            }
        }
        elevatorInput.close();
    }
}

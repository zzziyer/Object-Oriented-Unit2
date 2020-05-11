import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private Queue queue;

    public Input(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                Queue.setStopped(true);
                queue.putRequest(Request.STOP_SIGNAL, false);
                //System.out.println("input stopped");
                break;
            } else {
                queue.putRequest(new Request(request.getPersonId(),
                        request.getFromFloor(), request.getToFloor()), false);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

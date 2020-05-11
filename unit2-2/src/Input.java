import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private RequestQueue requestQueue;

    public Input(RequestQueue queue) {
        requestQueue = queue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                requestQueue.putRequest(Request.STOP_SIGNAL);
                break;
            } else {
                requestQueue.putRequest(new Request(request.getPersonId(),
                        request.getFromFloor(), request.getToFloor()));
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

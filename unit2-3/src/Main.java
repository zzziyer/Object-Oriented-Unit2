import com.oocourse.TimableOutput;

public class Main {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Queue queue = new Queue();
        Queue queueA = new Queue();
        Queue queueB = new Queue();
        Queue queueC = new Queue();

        Global global = new Global(queue, queueA, queueB, queueC);
        Input input = new Input(queue);
        input.start();
        global.start();
        Elevator eleA = new Elevator(queueA, queue,
                6, 400, Methods.getA(), 'A');
        Elevator eleB = new Elevator(queueB, queue,
                8, 500, Methods.getB(), 'B');
        Elevator eleC = new Elevator(queueC, queue,
                7, 600, Methods.getC(), 'C');
        eleA.start();
        eleB.start();
        eleC.start();


    }
}

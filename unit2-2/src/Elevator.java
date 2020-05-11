import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private final RequestQueue requestQueue;
    private int floor;
    private int state;//0 执行上行任务 1 执行下行任务
    private ArrayList<Request> passengersInside = new ArrayList<>();
    private int[] corr = {-3, -2, -1, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    private boolean stopped = false;
    private boolean init;

    public Elevator(RequestQueue r) {
        this.requestQueue = r;
        this.floor = 3;
        this.state = 0;
        this.init = true;
    }

    public void changeState() {
        if (state == 0) {
            state = 1;
        } else {
            state = 0;
        }
        //System.out.println("switch");
    }

    public ArrayList<Request> checkGetOff() { //检测此层是否有人下电梯
        ArrayList<Request> re = new ArrayList<>();
        int size = passengersInside.size();
        for (int i = 0; !passengersInside.isEmpty() && i < size; i++) {
            if (passengersInside.get(i).getTo() == corr[floor]) {
                re.add(passengersInside.get(i));
                passengersInside.remove(i);
                i--;
            }
        }
        return re;
    }

    public void print(ArrayList<Request> in, ArrayList<Request> out) {
        try {
            TimableOutput.println("OPEN-" + corr[floor]);
            Thread.sleep(200);//开门
            if (out.size() != 0) {
                for (Request r :
                        out) {
                    TimableOutput.println("OUT-" +
                            r.getId() + "-" + corr[floor]);
                }
            }
            if (in.size() != 0) {
                for (Request r :
                        in) {
                    TimableOutput.println("IN-" +
                            r.getId() + "-" + corr[floor]);
                }
            }
            Thread.sleep(200);//关门
            TimableOutput.println("CLOSE-" + corr[floor]);
        } catch (Exception e) {
            //
        }
    }

    public void toEdge() {
        try {
            if (state == 0) {   //下到-3
                while (floor != 0) {
                    Thread.sleep(500);//上一层或下一层
                    floor--;
                }
            } else {    //上到16
                while (floor != 18) {
                    Thread.sleep(500);//上一层或下一层
                    floor++;
                }
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void run() {
        //System.out.println("here");
        ArrayList<Request> in;
        ArrayList<Request> out;
        while (true) {
            try {
                if (stopped && passengersInside.size() == 0) {
                    break;
                }

                //                if (requestQueue.getRequestNum(state) == 0
                //                        && passengersInside.size() == 0) {
                //                    changeState();
                //                    toEdge();
                //                }
                in = requestQueue.getRequest(floor, state);
                if (!in.isEmpty() && in.get(0) == Request.STOP_SIGNAL) {
                    stopped = true;
                    in.clear();
                }
                synchronized (requestQueue) {
                    if (requestQueue.getRequestNum(0) +
                            requestQueue.getRequestNum(1) == 0
                            && passengersInside.size() == 0
                            && !stopped && in.size() == 0) {
                        requestQueue.wait();
                        continue;
                        //wait();
                        //System.out.println("circle");
                    }
                }
                if (init != true) {
                    TimableOutput.println("ARRIVE-" + corr[floor]);
                }
                init = false;

                out = checkGetOff();
                if (in.size() != 0 || out.size() != 0) {
                    print(in, out);
                }
                passengersInside.addAll(in);
                if (floor == 0 || floor == 18) {
                    changeState();
                }
                Thread.sleep(400);//上一层或下一层
                if (state == 0) {
                    floor++;
                } else {
                    floor--;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

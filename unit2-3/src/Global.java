import java.util.ArrayList;

public class Global extends Thread {
    private Queue queue;
    private Queue queueA;
    private Queue queueB;
    private Queue queueC;
    private static int wait = 0;

    public Global(Queue q, Queue aq, Queue bq, Queue cq) {
        this.queue = q;
        this.queueA = aq;
        this.queueB = bq;
        this.queueC = cq;
    }

    public static int getWait() {
        return Global.wait;
    }

    public static void setWait(int wait) {
        Global.wait = wait;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (Queue.isStopped() == true &&
                        (queue.getRequestNum(0) +
                                queue.getRequestNum(1) == 0)) {
                    Elevator.setStopped(true);
                    queueA.putRequest(Request.STOP_SIGNAL, false);
                    queueB.putRequest(Request.STOP_SIGNAL, false);
                    queueC.putRequest(Request.STOP_SIGNAL, false);
                    if (wait == 0) {
                        break;
                    }
                }
                allocate();
            } catch (InterruptedException e) {
                //
            }
        }
    }

    public void allocate() throws InterruptedException {
        //System.out.println("allo");
        synchronized (queue) {
            if (queue.getRequestNum(0) +
                    queue.getRequestNum(1) == 0) {
                //System.out.println("wait");
                queue.wait();
                return;
            }
        }
        //System.out.println("here");
        int i;
        int j;
        ArrayList<Request> temp = new ArrayList<>();
        for (i = 0; i < 23; i++) {
            temp.clear();
            temp.addAll(queue.getRequest(i, 0, 100));
            temp.addAll(queue.getRequest(i, 1, 100));
            //System.out.println("temp.size() = " + temp.size());
            for (j = 0; j < temp.size(); j++) {
                Request re = temp.get(j);
                add(re);
            }
        }
    }

    public int getBuildingFooter(int floor) {
        int tempFloor = floor;
        if (tempFloor < 0) {
            tempFloor += 3;
        } else {
            tempFloor += 2;
        }
        return tempFloor;
    }

    public void add(Request r) {
        int from = getBuildingFooter(r.getFrom());
        int a = 0;
        int b = 0;
        int c = 0;
        if (Methods.getA()[from] == 0) {
            a = -20;
        }
        if (Methods.getB()[from] == 0) {
            b = -20;
        }
        if (Methods.getC()[from] == 0) {
            c = -20;
        }
        int to = getBuildingFooter(r.getTo());
        if (Methods.getA()[to] == 1) {
            a += 10;
        }
        if (Methods.getB()[to] == 1) {
            b += 10;
        }
        if (Methods.getC()[to] == 1) {
            c += 10;
        }
        int[] temp = compare();
        a += temp[0];
        b += temp[1];
        c += temp[2];
        a *= 6;
        b *= 5;
        c *= 4;
        int max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        if (max == a) {
            queueA.putRequest(r, false);
        } else if (max == b) {
            queueB.putRequest(r, false);
        } else {
            queueC.putRequest(r, false);
        }
        if (r.isTransfer()) {
            Global.wait++;
        }
    }

    public int[] compare() {
        int[] re = new int[3];
        int a = queueA.getRequestNum(0) + queueA.getRequestNum(1);
        int b = queueB.getRequestNum(0) + queueB.getRequestNum(1);
        int c = queueC.getRequestNum(0) + queueC.getRequestNum(1);
        if (a > b) {
            re[0]++;
        }
        if (a > c) {
            re[0]++;
        }
        if (b > a) {
            re[1]++;
        }
        if (b > c) {
            re[1]++;
        }
        if (c > b) {
            re[2]++;
        }
        if (c > a) {
            re[2]++;
        }
        return re;
    }
}

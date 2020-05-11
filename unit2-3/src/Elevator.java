import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private char name;
    private int limitPeople;
    private int floorTime;
    private int[] canOpen;
    private final Queue myQueue;
    private final Queue globalQueue;
    private int floor;
    private int stateM;//0 执行上行任务 1 执行下行任务
    private int stateE;//0 上行 1 下行
    private ArrayList<Request> passengersInside = new ArrayList<>();
    private int[] corr = {-3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8,
        9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    private static boolean stopped = false;
    private static boolean canstopped = false;
    private boolean init;
    private static int stopNum = 0;
    private boolean first;

    public Elevator(Queue queue, Queue globalq, int limitPeople,
                    int floorTime, int[] canOpen, char name) {
        this.myQueue = queue;
        this.globalQueue = globalq;
        this.limitPeople = limitPeople;
        this.floorTime = floorTime;
        this.canOpen = canOpen;
        this.name = name;
        this.floor = 3;
        this.stateM = 0;
        this.stateE = 0;
        this.init = true;
        this.first = true;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (myQueue.getRequestNum(0) + myQueue.getRequestNum(1) == 0
                        && passengersInside.size() == 0 && Elevator.stopped
                        && Global.getWait() == 0
                ) {
                    //Elevator.stopNum++;
                    //if (stopNum == 3) {
                    //    Elevator.canstopped = true;
                    //}
                    //System.out.println(name + " stopped");
                    break;
                }
                carry();
            } catch (InterruptedException e) {
                //
            }
        }
    }

    public void carry() throws InterruptedException {
        synchronized (myQueue) {
            if (myQueue.getRequestNum(0) +
                    myQueue.getRequestNum(1) == 0
                    && passengersInside.size() == 0) {
                myQueue.wait();
                return;
            }
        }
        getOffIn();
        int off = checkMaxOff();   //实际楼层
        int maxRe = myQueue.getMaxRequest(floor, stateM);  //虚楼层
        if ((off == -100 || off == 100) && maxRe == -100) {
            init = true;
            first = true;
            changeStateM();
            if (myQueue.getRequestNum(stateM) == 0) {
                changeStateM();
                changeStateE();
                int temp = myQueue.check(floor, stateM);
                if (temp != -100) {
                    take(temp, stateE);
                    first = false;
                    init = false;
                }
                changeStateE();
            } else {
                int temp = myQueue.check(floor, stateM);
                if (temp != -100) {
                    take(temp, stateE);
                    first = false;
                    init = false;
                }
                changeStateE();
            }
            //System.out.println("here" + corr[floor]);
        } else {
            goUpDown(maxRe, off);
        }
    }

    public void goUpDown(int maxRe, int off) throws InterruptedException {
        if (stateE == 0) {
            int t;
            if (!(maxRe >= 0 && maxRe < 23)) {
                t = -100;
            } else {
                t = corr[maxRe];
            }
            int tempmax;
            if (off > t) {
                tempmax = off;
            } else {
                tempmax = t;
            }
            if (corr[floor] < tempmax) {
                Thread.sleep(floorTime);
                floor++;
            }
        } else {
            int t;
            if (!(maxRe >= 0 && maxRe < 23)) {
                t = 100;
            } else {
                t = corr[maxRe];
            }
            int tempmax;
            if (off < t) {
                tempmax = off;
            } else {
                tempmax = t;
            }
            if (corr[floor] > tempmax) {
                Thread.sleep(floorTime);
                floor--;
            }
        }
    }

    public void getOffIn() throws InterruptedException {
        //System.out.println(name+" arrive " + corr[floor]);
        if (init == false) {
            TimableOutput.println("ARRIVE-" + corr[floor] + "-" + name);
        }
        init = false;
        if (canOpen[floor] != 1) {
            return;
        }
        ArrayList<Request> out = checkGetOff();
        ArrayList<Request> in = new ArrayList<>();
        if (limitPeople - passengersInside.size() > 0) {
            in = myQueue.getRequest(floor, stateM,
                    limitPeople - passengersInside.size());
        }
        if (out.size() == 0 && in.size() == 0) {
            return;
        } else {
            TimableOutput.println("OPEN-" + corr[floor] + "-" + name);
            Thread.sleep(200);//开门

            if (out.size() != 0) {
                for (Request r :
                        out) {
                    TimableOutput.println("OUT-" +
                            r.getId() + "-" + corr[floor] + "-" + name);
                    if (r.isTransfer() == true) {
                        r.setFrom(r.getTo());
                        r.setTo(r.getTransfer());
                        r.setIsTransfer(false);
                        Global.setWait(Global.getWait() - 1);
                        globalQueue.putRequest(r, true);
                    }
                }
            }
            //System.out.println("in.size() = " + in.size());
            if (in.size() != 0) {
                for (Request r :
                        in) {
                    TimableOutput.println("IN-" +
                            r.getId() + "-" + corr[floor] + "-" + name);
                }
                passengersInside.addAll(in);
            }
            Thread.sleep(200);//关门
            TimableOutput.println("CLOSE-" + corr[floor] + "-" + name);
        }
    }

    public ArrayList<Request> checkGetOff() { //检测此层是否有人下电梯
        ArrayList<Request> re = new ArrayList<>();
        int size = passengersInside.size();
        for (int i = 0; !passengersInside.isEmpty() && i < size; i++) {
            if (passengersInside.get(i).getTo() == corr[floor]) {
                re.add(passengersInside.get(i));
                passengersInside.remove(i);
                i--;
                size--;
            }
        }
        return re;
    }

    public void take(int tofloor, int estate) throws InterruptedException {
        while (floor != tofloor) {
            //return;
            {
                if (estate == 0) {
                    floor++;
                } else {
                    floor--;
                }
            }
            Thread.sleep(floorTime);
            if (floor != tofloor) {
                if (!(init == true && first == false)) {

                    TimableOutput.println("ARRIVE-" + corr[floor] + "-" + name);
                }
                init = false;
            }
        }
    }

    public void changeStateM() {
        if (stateM == 0) {
            stateM = 1;
        } else {
            stateM = 0;
        }
    }

    public int checkMaxOff() {
        int re = -100;
        for (Request r :
                passengersInside) {
            if (stateM == 0) {
                re = -100;
                if (r.getTo() > corr[floor]) {
                    if (r.getTo() > re) {
                        re = r.getTo();
                    }
                }
            } else {
                re = 100;
                if (r.getTo() < corr[floor]) {
                    if (r.getTo() < re) {
                        re = r.getTo();
                    }
                }
            }
        }
        return re;
    }

    public void changeStateE() {
        if (stateE == 0) {
            stateE = 1;
        } else {
            stateE = 0;
        }
    }

    public static boolean isStopped() {
        return stopped;
    }

    public static boolean isCanstopped() {
        return canstopped;
    }

    public static int getStopNum() {
        return stopNum;
    }

    public static void setStopped(boolean stopped) {
        Elevator.stopped = stopped;
    }

    public static void setCanstopped(boolean canstopped) {
        Elevator.canstopped = canstopped;
    }

    public static void setStopNum(int stopNum) {
        Elevator.stopNum = stopNum;
    }
}

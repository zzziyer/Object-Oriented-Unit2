import java.util.ArrayList;

public class Queue {
    private ArrayList[] upBuilding = new ArrayList[23];
    private ArrayList[] downBuilding = new ArrayList[23];

    public static void setStopped(boolean stopped) {
        Queue.stopped = stopped;
    }

    public static boolean isStopped() {
        return stopped;
    }

    private static boolean stopped = false;

    public Queue() {
        for (int i = 0; i < 23; i++) {
            upBuilding[i] = new ArrayList();
            downBuilding[i] = new ArrayList();
        }
    }

    public int getMaxRequest(int floor, int state) {  //需不需要切换任务
        int i;
        int re = -100;
        if (state == 0) {
            for (i = floor + 1; i < 23; i++) {
                if (upBuilding[i].size() != 0) {
                    re = i;
                }
            }
        } else {
            for (i = floor - 1; i >= 0; i--) {
                if (downBuilding[i].size() != 0) {
                    re = i;
                }
            }
        }
        return re;
    }

    public int check(int floor, int state) {  //需不需要切换方向
        //下任务，方向向上，上面有没有下任务
        //上任务，方向向下，下面有没有上任务
        int i;
        int re = -100;
        if (state == 0) {
            for (i = floor - 1; i >= 0; i--) {
                if (upBuilding[i].size() != 0) {
                    re = i;
                }
            }
        } else {
            for (i = floor + 1; i < 23; i++) {
                if (downBuilding[i].size() != 0) {
                    re = i;
                }
            }
        }
        return re;
    }

    public int getRequestNum(int state) {
        int cnt = 0;
        int i;
        if (state == 0) {
            for (i = 0; i < 23; i++) {
                cnt += upBuilding[i].size();
            }
        } else {
            for (i = 0; i < 23; i++) {
                cnt += downBuilding[i].size();
            }
        }
        return cnt;
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

    public synchronized void putRequest(Request request, boolean midway) {
        if (request == Request.STOP_SIGNAL) {
            notifyAll();
            return;
        }
        int tempFloor = getBuildingFooter(request.getFrom());

        if (request.getFrom() < request.getTo()) {
            if (midway == false) {
                upBuilding[tempFloor].add(request);
            } else {
                upBuilding[tempFloor].add(0, request);
            }
        } else {
            if (midway == false) {
                downBuilding[tempFloor].add(request);
            } else {
                downBuilding[tempFloor].add(0, request);
            }
        }
        this.notify();
    }

    public synchronized ArrayList<Request> getRequest(int eleFloor, int state,
                                                      int num) {
        int tempnum = num;
        ArrayList<Request> re = new ArrayList<>();
        if (state == 0) {
            if ((upBuilding[eleFloor]).size() <= num) {
                re.addAll(upBuilding[eleFloor]);
                upBuilding[eleFloor].clear();
            } else {
                while (tempnum-- > 0) {
                    re.add((Request) upBuilding[eleFloor].get(0));
                    upBuilding[eleFloor].remove(0);
                }
            }
        } else {
            if (downBuilding[eleFloor].size() <= num) {
                re = new ArrayList<>(downBuilding[eleFloor]);
                downBuilding[eleFloor].clear();
            } else {
                while (tempnum-- > 0) {
                    re.add((Request) downBuilding[eleFloor].get(0));
                    downBuilding[eleFloor].remove(0);
                }
            }
        }
        return re;
    }
}

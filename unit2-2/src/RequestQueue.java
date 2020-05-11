import java.util.ArrayList;

public class RequestQueue {
    private ArrayList[] upBuilding = new ArrayList[19];
    private ArrayList[] downBuilding = new ArrayList[19];
    private boolean full = false;
    private boolean stopped = false;

    public int getRequestNum(int state) {
        int cnt = 0;
        int i;
        if (state == 0) {
            for (i = 0; i < 19; i++) {
                cnt += upBuilding[i].size();
            }
        } else {
            for (i = 0; i < 19; i++) {
                cnt += downBuilding[i].size();
            }
        }
        return cnt;
    }

    public RequestQueue() {
        for (int i = 0; i < 19; i++) {
            upBuilding[i] = new ArrayList();
            downBuilding[i] = new ArrayList();
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

    public synchronized void putRequest(Request request) {
        if (request == Request.STOP_SIGNAL) {
            stopped = true;
            this.notify();
            return;
        }
        int tempFloor = getBuildingFooter(request.getFrom());
        if (request.getFrom() < request.getTo()) { //go up
            upBuilding[tempFloor].add(request);
        } else {
            downBuilding[tempFloor].add(request);
        }
        this.notify();
    }

    public synchronized ArrayList<Request> getRequest(int eleFloor, int state) {
        ArrayList<Request> re;
        if (state == 0) {
            re = new ArrayList<>(upBuilding[eleFloor]);
            upBuilding[eleFloor].clear();
        } else {
            re = new ArrayList<>(downBuilding[eleFloor]);
            downBuilding[eleFloor].clear();
        }
        if (re.isEmpty() && stopped) {
            re.add(Request.STOP_SIGNAL);
        }
        return re;
    }
}

public class Request {
    private int id;
    private int from;
    private int to;
    private int transfer;
    private boolean isTransfer;
    private Methods methods = new Methods();

    public static final Request STOP_SIGNAL = new Request(-1, 0, 0);

    public Request(int id, int from, int to) {
        this.id = id;
        this.from = from;
        this.to = to;
        if (from < to) {
            this.transfer = methods.get(from, to);
        } else {
            this.transfer = methods.get(to, from);
        }
        if (this.transfer != 100) {
            this.isTransfer = true;
            this.to = this.transfer;
            this.transfer = to;
        } else {
            this.isTransfer = false;
        }
    }

    public int getId() {
        return id;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getTransfer() {
        return transfer;
    }

    public boolean isTransfer() {
        return isTransfer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }

    public void setIsTransfer(boolean transfer) {
        isTransfer = transfer;
    }

}

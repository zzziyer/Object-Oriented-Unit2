public class Request {
    private int id;
    private int from;
    private int to;

    public static final Request STOP_SIGNAL = new Request(-1, 0, 0);

    public Request(int id, int from, int to) {
        this.id = id;
        this.from = from;
        this.to = to;
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
}

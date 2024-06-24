package session;

public class Session {
    private static int patronId;

    public static void setPatronId(int id) {
        patronId = id;
    }

    public static int getPatronId() {
        return patronId;
    }
}

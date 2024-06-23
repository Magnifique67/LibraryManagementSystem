package session;

public class Session {
    private static int patronId;

    public static int getPatronId() {
        return patronId;
    }

    public static void setPatronId(int patronId) {
        Session.patronId = patronId;
    }
}
package AppGui.User;

public class Session {
    private static Session ourInstance = new Session();

    public static Session getInstance() {
        return ourInstance;
    }

    protected int userId = -1;

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
    {
        return this.userId;
    }

}

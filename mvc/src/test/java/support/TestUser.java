package support;

public class TestUser {

    private final long id;
    private final String account;

    public TestUser(long id, String account) {
        this.id = id;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }
}

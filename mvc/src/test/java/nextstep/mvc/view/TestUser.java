package nextstep.mvc.view;

public class TestUser {
    private String name;
    private String nickname;

    public TestUser() {
    }

    public TestUser(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }
}
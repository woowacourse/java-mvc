package samples;

public class TestUser {

    private final String name;
    private final int age;

    public TestUser(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

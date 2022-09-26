package samples.dummy;

public class DummyObject {
    private final String name;
    private final int price;

    public DummyObject() {
        this.price = 1_000;
        this.name = "DUMMY";
    }

    public DummyObject(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

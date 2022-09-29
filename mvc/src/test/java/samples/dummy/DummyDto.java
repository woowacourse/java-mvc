package samples.dummy;

public class DummyDto {
    private final DummyObject test1;
    private final DummyObject test2;

    public DummyDto(DummyObject test1, DummyObject test2) {
        this.test1 = test1;
        this.test2 = test2;
    }

    public DummyObject getTest1() {
        return test1;
    }

    public DummyObject getTest2() {
        return test2;
    }
}

package samples;

public class TestObject {

    private final Object field1;
    private final Object field2;

    public TestObject(Object field1, Object field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public Object getField1() {
        return field1;
    }

    public Object getField2() {
        return field2;
    }
}

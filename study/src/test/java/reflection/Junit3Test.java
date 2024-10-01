package reflection;

public class Junit3Test {

    private int methodCallCount;

    public Junit3Test() {
        methodCallCount = 0;
    }

    public void test1() {
        System.out.println("Running Test1");
        methodCallCount++;
    }

    public void test2() {
        System.out.println("Running Test2");
        methodCallCount++;
    }

    public void three() {
        System.out.println("Running Test3");
        methodCallCount++;
    }

    public int getMethodCallCount() {
        return methodCallCount;
    }
}

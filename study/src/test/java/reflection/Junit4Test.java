package reflection;

public class Junit4Test {

    private int methodCallCount;

    public Junit4Test() {
        methodCallCount = 0;
    }

    @MyTest
    public void one() {
        System.out.println("Running Test1");
        methodCallCount++;
    }

    @MyTest
    public void two() {
        System.out.println("Running Test2");
        methodCallCount++;
    }

    public void testThree() {
        System.out.println("Running Test3");
        methodCallCount++;
    }

    public int getMethodCallCount() {
        return methodCallCount;
    }
}

package reflection;

public class Junit4Test {

    @MyTest
    public void one() {
        System.out.println("Running Test1");
    }

    @MyTest
    public void two() {
        System.out.println("Running Test2");
    }

    public void testThree() {
        System.out.println("Running Test3");
    }
}

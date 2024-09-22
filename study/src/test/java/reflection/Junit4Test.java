package reflection;

public class Junit4Test {

    @MyTest
    public void one() throws Exception {
        System.out.println("Running Test4-1");
    }

    @MyTest
    public void two() throws Exception {
        System.out.println("Running Test4-2");
    }

    public void testThree() throws Exception {
        System.out.println("Running Test4-3");
    }
}

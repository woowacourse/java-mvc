package reflection;

public class Junit4Test {

    @MyTest
    public void one() throws Exception {
        System.out.println("Running Test11");
    }

    @MyTest
    public void two() throws Exception {
        System.out.println("Running Test22");
    }

    public void testThree() throws Exception {
        System.out.println("Running Test3");
    }
}

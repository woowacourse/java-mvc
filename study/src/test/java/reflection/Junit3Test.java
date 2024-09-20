package reflection;

public class Junit3Test {
    public String test1(final int number) throws Exception {
        System.out.println("Running Test1");
        return "test1()의 결과값 : " + number;
    }

    public String test2(final int number) throws Exception {
        System.out.println("Running Test2");
        return "test2()의 결과값 : " + number;
    }

    public void three() throws Exception {
        System.out.println("Running Test3");
    }
}

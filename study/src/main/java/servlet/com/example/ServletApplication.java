package servlet.com.example;

public class ServletApplication {

    public static void main(String[] args) {
        TomcatStarter tomcatStarter = new TomcatStarter();
        tomcatStarter.start();
        tomcatStarter.await();
    }
}

package reflection;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        ByteArrayOutputStream bs= new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bs);
        System.setOut(ps);

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] methods = clazz.getMethods();
        Junit4Test junit4Test = new Junit4Test();
        for(Method m : methods){
            if(m.getAnnotation(MyTest.class) != null){
                m.invoke(junit4Test);
            }
        }

        assertThat(bs.toString()).isEqualTo("Running Test1\nRunning Test2\n");
    }
}

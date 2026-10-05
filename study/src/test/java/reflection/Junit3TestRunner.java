package reflection;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;


class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        ByteArrayOutputStream bs= new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bs);
        System.setOut(ps);

        Method[] methods = clazz.getMethods();

        Junit3Test junit3Test = new Junit3Test();
        for (Method m : methods) {
            if (m.getName().startsWith("test")){
                m.invoke(junit3Test);
            }
        }

        assertThat(bs.toString()).isEqualTo("Running Test1\nRunning Test2\n");
    }
}

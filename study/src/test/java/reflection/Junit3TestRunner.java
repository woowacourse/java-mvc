package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Arrays.stream(clazz.getMethods())
              .map(Method::getName)
              .filter(name -> name.startsWith("test"))
              .forEach(name -> {
                  try {
                      clazz.getMethod(name).invoke(clazz.getDeclaredConstructor().newInstance());
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              });
    }
}

package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // 메서드 리스트 추출
        Method[] methods = clazz.getMethods();
        // 클래스 생성자 추출 -> 객체 생성
        Object junit3Test = clazz.getDeclaredConstructor().newInstance();
        for(Method m : methods){
            if(m.getName().startsWith("test")){
                m.invoke(junit3Test);   // 메서드 실행(실행 객체, 인자(필요시))
            }
        }
    }
}

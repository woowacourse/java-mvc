package reflection;

import javassist.bytecode.annotation.AnnotationImpl;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = clazz.getDeclaredConstructor().newInstance();

        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            for(Annotation annotation : method.getAnnotations()){
                Class<? extends Annotation> aClass = annotation.annotationType();
                if(aClass.equals(MyTest.class)){
                    method.invoke(instance);
                }

            }
        }
    }
}

package reflection;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;

        // 9 버전부터 Depreacted
        // throws InstantiationException, IllegalAccessException
        // => Checked Exception 을 Runtime Exception 으로 우회 가능하게 해주었다.
        // ( try-catch 로 못 잡는다는 의미 )
        //final Junit3Test instance = Mockito.spy(clazz.newInstance());

        // throws NoSuchMethodException, SecurityException 발생 시킴
        //clazz.getDeclaredConstructor();

        //        throws InstantiationException, IllegalAccessException,
        //               IllegalArgumentException, InvocationTargetException 발생 시킴
        // clazz.getDeclaredConstructor().newInstance();


        // e.newInstance() 는 발생하는 예외를 잡지 못하고, SQLException 이 발생한다.

        // e.getCause 는 SQLException 을 나타낸다.
//        try {
//            ExceptionClass.class.getDeclaredConstructor().newInstance();
//        } catch (InvocationTargetException e) {
//            e.getCause().printStackTrace();
//        }


        final Junit3Test instance = Mockito.spy(clazz.getDeclaredConstructor()
                .newInstance());

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method ->
                        method.getName()
                                .startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(instance, (Object[]) null);
                    } catch (final IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

        Mockito.verify(instance, Mockito.times(1)).test1();
        Mockito.verify(instance, Mockito.times(1)).test2();
        Mockito.verify(instance, Mockito.times(0)).three();
    }
}

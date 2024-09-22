package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {
    /**
     * Class<T>
     *
     * Class<T>는 Java에서 제네릭을 사용하여 특정 타입의 클래스 메타데이터를 다루기 위한 클래스이다.
     * Class는 Java Reflection API의 핵심 클래스로, 프로그램 실행 중에 클래스의 정보를 확인하거나 객체를 동적으로 생성하는 데 사용된다.
     */
    /**
     * getDeclaredXX() vs getXX()
     *
     * getDeclaredXXX()
     * 해당 클래스에 선언된 모든 필드, 메서드, 생성자를 반환한다.
     * 접근 제한자에 관계없이 클래스에 선언된 모든 멤버를 가져올 수 있다.
     * 단, 상속받은 멤버는 포함하지 않는다. 해당 클래스 자체에 정의된 멤버만 가져온다.
     *
     * getXXX()
     * 해당 클래스와 상속받은 클래스의 public 필드, 메서드, 생성자만 반환한다.
     * public으로 선언된 멤버만 가져오며, 상속된 클래스의 public 멤버들도 포함된다.
     */
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        /**
         * Reflection을 사용하여 Junit3Test 클래스의 객체를 동적으로 생성한다.
         * Junit3Test 클래스의 인스턴스 메서드들을 호출하기 위한 인스턴스를 생성하는 것이다.
         *
         * clazz.getMethods()로 메서드를 가져오는 것은 메타 데이터를 가져오는 것이지, 실제로 그 메서드를 실행할 객체를 제공해주지는 않는다.
         * 따라서, 실행할 객체를 별도로 생성해줘야 하는 것이다.
         */
        Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if(method.getName().startsWith("test")){
                method.invoke(instance);
            }
        }
    }
}

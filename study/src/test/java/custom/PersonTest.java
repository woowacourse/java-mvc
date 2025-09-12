package custom;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import custom.Human.Person;
import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    void test()
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        StringBuilder sb = new StringBuilder();

        // 1단계: Class 객체 얻기
        Class<?> clazz1 = Person.class;
        Person person = Person.of("리버1", 25);
        Class<?> clazz2 = person.getClass();
        Class<?> clazz3 = Class.forName("custom.Human$Person");

        // 결과 비교
        sb.append("\n================================================").append("\n");
        sb.append("1단계: Class 객체 얻기").append("\n");
        sb.append("\n");

        sb.append("clazz1 == clazz2: ").append(clazz1 == clazz2).append("\n"); // clazz1 == clazz2: true
        sb.append("clazz1 == clazz3: ").append(clazz1 == clazz3).append("\n"); // clazz1 == clazz3: true
        sb.append("clazz2 == clazz3: ").append(clazz2 == clazz3).append("\n"); // clazz2 == clazz3: true
        sb.append("\n");

        sb.append("Simple Name: ").append(clazz1.getSimpleName()).append("\n"); // Simple Name: Person
        sb.append("Name: ").append(clazz1.getName()).append("\n"); // Name: custom.Human$Person
        sb.append("Canonical Name: ").append(clazz1.getCanonicalName()).append("\n"); // Canonical Name: custom.Human.Person

        // 2단계: 필드, 생성자, 메서드 정보 조회
        sb.append("\n================================================").append("\n");
        sb.append("2단계: 필드, 생성자, 메서드 정보 조회").append("\n");

        sb.append("\n--------------------------------------------").append("\n");
        sb.append("2.1단계: 필드 메서드 정보 조회").append("\n");
        sb.append("\n");

        sb.append("Declared Fields: "); // Declared Fields: publicField name age
        Field[] fields1 = clazz1.getDeclaredFields();
        for (Field field : fields1) {
            sb.append(field.getName()).append(" ");
        }
        sb.append("\n");

        sb.append("Fields: "); // Fields: publicField publicSuperField
        Field[] fields2 = clazz1.getFields();
        for (Field field : fields2) {
            sb.append(field.getName()).append(" ");
        }
        sb.append("\n");

        sb.append("\n--------------------------------------------").append("\n");
        sb.append("2.2단계: 생성자 메서드 정보 조회").append("\n");
        sb.append("\n");

        sb.append("Declared Constructors:\n"); // Declared Constructors:
        Constructor<?>[] constructors1 = clazz1.getDeclaredConstructors();
        for (Constructor<?> c : constructors1) {
            sb.append(c.getName()).append("\n"); // custom.Human$Person (private)
        }                                        // custom.Human$Person (public)
        sb.append("\n");

        sb.append("Constructors:\n"); // Constructors:
        Constructor<?>[] constructors2 = clazz1.getConstructors();
        for (Constructor<?> c : constructors2) {
            sb.append(c.getName()).append("\n"); // custom.Human$Person (public)
        }

        sb.append("\n--------------------------------------------").append("\n");
        sb.append("2.3단계: 메서드 정보 조회").append("\n");
        sb.append("\n");

        /**
         * Declared Methods
         * : of publicSuperMethod sayHello whisper
         */
        sb.append("Declared Methods: "); // Declared Methods: of publicSuperMethod sayHello whisper
        Method[] methods1 = clazz1.getDeclaredMethods();
        for (Method method : methods1) {
            sb.append(method.getName()).append(" ");
        }
        sb.append("\n");

        sb.append("Methods: "); // Methods: of publicSuperMethod sayHello (and all methods from Object class like equals toString hashCode getClass notify notifyAll wait wait wait)
        Method[] methods2 = clazz1.getMethods();
        for (Method method : methods2) {
            sb.append(method.getName()).append(" ");
        }
        sb.append("\n");

        // 3단계: 동적으로 객체 생성
        sb.append("\n================================================").append("\n");
        sb.append("3단계: 동적으로 객체 생성").append("\n");
        sb.append("\n");

        Constructor<?> constructor = clazz1.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Object personObj = constructor.newInstance("리버2", 30);
        sb.append("Created Object: ").append(personObj.getClass()).append("\n"); // Created Object: class custom.Human$Person

        // 4단계: 메서드 호출 및 필드 값 변경
        sb.append("\n================================================").append("\n");
        sb.append("4단계: 메서드 호출 및 필드 값 변경").append("\n");
        sb.append("\n");

        Method sayHello = clazz1.getDeclaredMethod("sayHello");
        sb.append("'sayHello()' invoked: "); // sayHello() invoked: Hello World!

        sayHello.invoke(personObj);
        Field nameField = clazz1.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(personObj, "리버3");
        Object changedValue = nameField.get(personObj);
        sb.append("Changed name field value: ").append(changedValue).append("\n"); // Changed name field value: 리버3

        // 최종 출력
        System.out.println(sb.toString());
    }
}

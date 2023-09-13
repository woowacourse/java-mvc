package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();

        Junit3Test instance = new Junit3Test();
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    @Test
    void ex1() throws NoSuchFieldException, IllegalAccessException {
        Class<Junit3Test> clazz = Junit3Test.class;
        Field declaredField = clazz.getDeclaredField("name");
        declaredField.setAccessible(true);

        Junit3Test myClass = new Junit3Test();
        String name = (String) declaredField.get(myClass);
        System.out.println("name = " + name);
    }

    private static class MyClass {

        private final String name;
        private final int age;

        private MyClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        protected void print() {
            System.out.println(name + " : " + age);
        }
    }

    private static class MySecondClass extends MyClass {

        private MySecondClass(String name, int age) {
            super(name, age);
        }

        public void print2() {
            System.out.println("This is MySecondClass's print Method");
        }
    }

    @Test
    void getMethodsTest2() {
        MySecondClass mySecondClass = new MySecondClass("name", 1);

        Class<MySecondClass> secondClazz = MySecondClass.class;
        Method[] methods = secondClazz.getMethods();
        for (Method method : methods) {
            System.out.println("mySecondClass's method.getName() = " + method.getName());
        }
    }

    @Test
    void usingPrivateConstructorAndMethods() throws Exception {
        // given
        Class<MyClass> clazz = MyClass.class;
        Constructor<MyClass> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);

        Method declaredMethod = clazz.getDeclaredMethod("print");
        declaredMethod.setAccessible(true);

        // when
        MyClass instance = constructor.newInstance("도기", 10);

        // then
        declaredMethod.invoke(instance);

        // additional
        Field declaredName = clazz.getDeclaredField("name");
        declaredName.setAccessible(true);
        Field declaredAge = clazz.getDeclaredField("age");
        declaredAge.setAccessible(true);

        declaredName.set(instance, "도기2");
        declaredAge.set(instance, 100);

        declaredMethod.invoke(instance);
    }

    @Test
    void getMethodsTest() {
        Class<MyClass> clazz = MyClass.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("method.getName() = " + method.getName());
        }
    }
}

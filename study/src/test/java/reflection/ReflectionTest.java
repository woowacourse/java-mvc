package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        System.out.println(clazz.getSimpleName());  // Question
        System.out.println(clazz.getName()); // reflection.Question
        System.out.println(clazz.getCanonicalName()); // reflection.Question

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // 런타임에 클래스를 이름으로 찾는 법. 코드의 타입을 적을 수 없을 때 효과적이다.
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        // getClass()는 변수에 적힌 타입이 아니라 실제로 만들어진 객체의 타입을 런타임에 알려준다.
        final Object student = new Student();
        System.out.println(student);
        final Field[] fields = student.getClass().getDeclaredFields();
        System.out.println(Arrays.toString(fields));
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        // 런타임때, 클래스에서 필드 가져오기
        final Class<?> animalClass = Student.class;
        System.out.println(animalClass);
        final Method[] methods = animalClass.getDeclaredMethods();
        System.out.println(Arrays.toString(methods));
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();
        System.out.println(actualMethods);

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getConstructors();
        System.out.println(Arrays.toString(constructors));

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        // 클래스에서 생성자가 여러개일 때, 파라미터로 구분하는 법
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class,
                String.class);
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class,
                String.class, String.class, Date.class, int.class);

        System.out.println(firstConstructor);
        System.out.println(secondConstructor);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2",
                Date.from(Instant.now()), 1);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        // getDeclaredFields는 순서가 보장되지 않는다.
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        System.out.println(field);
        final Class<?> fieldClass = field.getType();
        System.out.println(fieldClass);

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();
        final Field field = student.getClass().getDeclaredField("age");

        // todo field에 접근 할 수 있도록 만든다.

        // 접근 제어자의 검사를 건너뛰게 한다. private 캡슐화를 밖에서 뚫게 한다.
        field.setAccessible(true);
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // student 객체의 age에 99를 넣는다
        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

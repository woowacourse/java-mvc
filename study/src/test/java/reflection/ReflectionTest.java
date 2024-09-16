package reflection;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        /* 아래에서 호출한 메서드들은 클래스가 어떤 클래스인지에 따라 다르게 출력한다.
        1. 기본 타입의 경우
        2. 일반적인 클래스인 경우
        3. 중첩 클래스인 경우
        4. 익명 클래스인 경우
        5. 배열인 경우
        https://stackoverflow.com/questions/15202997/what-is-the-difference-between-canonical-name-simple-name-and-class-name-in-jav
        */
        assertThat(clazz.getSimpleName()).isEqualTo("Question"); // 클래스 이름만을 반환한다.
        assertThat(clazz.getName()).isEqualTo("reflection.Question"); // 패키지까지 포함한 클래스 이름을 반환한다.
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question"); // 패키지 이름을 포함한 클래스 이름을 통해 클래스를 검색할 수 있다.

        assertThat(clazz.getSimpleName()).isEqualTo("Question"); // 클래스 이름만을 반환한다.
        assertThat(clazz.getName()).isEqualTo("reflection.Question"); // 패키지까지 포함한 클래스 이름을 반환한다.
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        Class<?> clazz = student.getClass(); // .getClass 는 당연히 실제 객체의 클래스를 가져온다. 변수 타입과 무관하다.

        final Field[] fields = clazz.getDeclaredFields();

        final List<String> actualFieldNames = Arrays.stream(fields).map(Field::getName).toList(); // 필드도 이름을 가져올 수 있다.

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods).map(Method::getName).toList(); // 메서드도 이름을 가져올 수 있다.

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getConstructors();
        assertThat(constructors).hasSize(2);
    }

    @Test
    void nestedClass_getConstructors() {
        assertThat(NoConstructorClass.class.getConstructors()).hasSize(0); // 중첩 클래스의 기본 생성자는 가져오지 못한다.
        assertThat(YesConstructorClass.class.getConstructors()).hasSize(1); // 중첩 클래스에 생성자를 명시했으면 가져온다.
    }
    static class NoConstructorClass{}
    static class YesConstructorClass{
        public YesConstructorClass() {
        }
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<Question> questionClass = Question.class;

        final Constructor<Question> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class); // 매개변수 타입을 이용해 생성자를 조회한다.
        Class[] allFieldsClass = Arrays.stream(questionClass.getDeclaredFields())
                .map(Field::getType) // 필드의 실제 클래스를 반환한다.
                .toList().toArray(new Class[0]);
        final Constructor<Question> secondConstructor = questionClass.getConstructor(allFieldsClass);

        final Question firstQuestion = firstConstructor.newInstance("gugu","제목1","내용1");
        final Question secondQuestion = secondConstructor.newInstance(1, "gugu","제목2","내용2", new Date(), 1);

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
        final Field[] fields = questionClass.getFields(); // 퍼블릭 필드만 조회한다.

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields(); // 퍼블릭이 아닌 필드들도 포함해 조회한다.

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId"); // 필드를 이름을 이용해 조회한다.

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType(); // 필드가 어떤 타입으로 선언되었는지 조회한다.

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<Student> studentClass = Student.class;
        final Student student = studentClass.getConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // todo field에 접근 할 수 있도록 만든다.

        field.setAccessible(true); // 접근 지정자에 의해 접근이 불가능한 필드에 접근을 허용한다.

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

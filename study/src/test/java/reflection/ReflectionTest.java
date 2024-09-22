package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("클래스 객체로부터 SimpleName, Name, CanonicalName을 알 수 있다")
    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        String expectedSimpleName = "Question";  // 느슨하게 식별(unique 보장 x)
        String expectedName = "reflection.Question"; // ClassLoader scope에서 동적 로드 시 사용가능한 name unique 보장
        String expectedCanonicalName = "reflection.Question"; // import문 포함

        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo(expectedSimpleName),
                () -> assertThat(clazz.getName()).isEqualTo(expectedName),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo(expectedCanonicalName)
        );
    }

    @DisplayName("inner 클래스의 경우, Name은 $로, CanonicalName은 .로 구분한다")
    @Test
    void givenObject_whenGetsInnerClassName_thenCorrect() {
        final Class<Question.QuestionInnerClass> clazz = Question.QuestionInnerClass.class;

        String expectedSimpleName = "QuestionInnerClass";  // 느슨하게 식별(unique 보장 x)
        String expectedName = "reflection.Question$QuestionInnerClass"; // ClassLoader scope에서 동적 로드 시 사용가능한 name unique 보장
        String expectedCanonicalName = "reflection.Question.QuestionInnerClass"; // import문 포함

        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo(expectedSimpleName),
                () -> assertThat(clazz.getName()).isEqualTo(expectedName),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo(expectedCanonicalName)
        );
    }

    @DisplayName("Class.forName : 클래스명 기반 클래스 객체를 찾음")
    @Test
    void givenClassName_whenCreatesObject_thenCorrect()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Class<?> clazz = Class.forName("reflection.Question");

        Object object = clazz.getDeclaredConstructor(String.class, String.class, String.class)
                .newInstance("testWriter", "testTitle", "testContent");

        String expectedSimpleName = object.getClass().getSimpleName();
        String expectedName = object.getClass().getName();
        String expectedCanonicalName = object.getClass().getCanonicalName();

        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo(expectedSimpleName),
                () -> assertThat(clazz.getName()).isEqualTo(expectedName),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo(expectedCanonicalName)
        );
    }

    @DisplayName("Class.getDeclaredFields : 클래스가 가진 필드 정보를 알 수 있다")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Stream.of(fields)
                .map(Field::getName)
                .toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @DisplayName("Class.getDeclaredMethods : 클래스가 가진 메서드 정보를 알 수 있다")
    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Stream.of(methods)
                .map(Method::getName)
                .toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @DisplayName("Class.getDeclaredConstructor : 클래스가 가진 생성자 정보를 알 수 있다")
    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @DisplayName("생성자 객체를 기반으로 런타임에 동적인 객체 생성이 가능하다")
    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(
                String.class,
                String.class,
                String.class
        );
        final Constructor<?> secondConstructor = questionClass.getConstructor(
                long.class,
                String.class,
                String.class,
                String.class,
                Date.class,
                int.class
        );

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1, "gugu", "제목2", "내용2", new Date(), 0);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @DisplayName("getFields : public 필드만 가져온다. / getDeclaredFields : 접근제어자 무관")
    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @DisplayName("getDeclaredFields : 접근제어자 무관")
    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @DisplayName("getDeclaredField(String fieldName) 으로 필드를 가져올 수 있다")
    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final String fieldName = "questionId";
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField(fieldName);

        assertThat(field.getName()).isEqualTo(fieldName);
    }

    @DisplayName("Field.getType : 필드 클래스 타입를 가져온다")
    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @DisplayName("setAccessible : private 필드 접근을 허용한다")
    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<Student> studentClass = Student.class;
        final Student student = studentClass.getDeclaredConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // todo field에 접근 할 수 있도록 만든다.
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

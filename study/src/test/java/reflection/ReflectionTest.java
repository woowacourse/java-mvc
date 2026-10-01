package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Class 클래스로 클래스 이름을 조회할 수 있다.")
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    @DisplayName("Class 클래스를 클래스 이름으로 조회할 수 있다.")
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    @DisplayName("런타임 객체로 Class 클래스를 조회할 수 있다.")
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();

        Class<?> clazz = student.getClass();

        final Field[] fields = clazz.getDeclaredFields();

        final List<String> actualFieldNames = Arrays.stream(fields).map(Field::getName).toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    @DisplayName("Class 클래스로 메서드 이름을 가져올 수 있다.")
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods).map(Method::getName).toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    @DisplayName("Class 클래스로 생성자를 가져올 수 있다.")
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    @DisplayName("Class 클래스로 생성자를 조회하고 인스턴스로 만들 수 있다.")
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        // Question Class 클래스
        final Class<?> questionClass = Question.class;

        // 생성자 조회 방법은, 주어진 매개변수 클래스에 맞는 생성자를 가져온다.
        //String writer, String title, String contents
        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class,
                String.class);
        //Long questionId, String writer, String title, String contents, Date createdDate, int countOfComment
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class,
                String.class, String.class, Date.class, int.class);

        // newInstance()는 기본 생성자를 사용하듯 매개변수를 전달하면 된다.
        // 반환되는 값은 형변환을 해주어야 한다.
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(),
                1);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("getXXX는 public 필드만 가져올 수 있다")
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @Test
    @DisplayName("getDeclaredXXX는 private 필드도 가져온다.")
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    @DisplayName("필드 이름으로 필드를 조회할 수 있다.")
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    @DisplayName("필드의 클래스 타입을 조회할 수 있다.")
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    @DisplayName("별도의 메서드 없이 인스턴스의 필드 변수를 변경할 수 있다.")
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        // Student를 동적으로 생성하고, setter 없이 값을 바꾼다.
        final Class<?> studentClass = Student.class;
        Constructor<?> constructor = studentClass.getConstructor();
        final Student student = (Student) constructor.newInstance();
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

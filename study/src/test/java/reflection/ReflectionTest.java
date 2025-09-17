package reflection;

import java.util.Arrays;
import java.util.stream.Collectors;
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

    // 객체로 리플렉션
    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question"); // 가장 단순한 클래스 이름 불러옴, 로그 출력에 사용
        assertThat(clazz.getName()).isEqualTo("reflection.Question"); // JVM이 내부적으로 사용하는 이름, 클래스를 동적 로딩할때 필요
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question"); // Java 언어 명세에 따른 공식 이름. 경로까지 포함함
    }

    // 클래스 이름으로 리플렉션
    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    // 클래스의 필드 이름 리플렉션
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        Class<?> clazz = student.getClass();
        final Field[] fields = clazz.getDeclaredFields(); // 접근 제어자에 상관없이 모두 불러옴. getFields 는 public 만 불러옴
        final List<String> actualFieldNames = Arrays.stream(fields).toList().stream().map(Field::getName).toList();

        assertThat(actualFieldNames).contains("name", "age");
    }


    // 클래스의 메서드 이름 불러옴
    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods(); // 메서드명 불러옴
        final List<String> actualMethods = Arrays.stream(methods).toList().stream().map(Method::getName).toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    // 생성자의 갯수를 가져온다.
    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    // 인스턴스를 동적으로 생성한다.
    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class); // 어떤 타입이 들어가는지 명시해줘야 함
        final Constructor<?> secondConstructor = questionClass.getConstructor(String.class, String.class, String.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance("gugu", "제목2", "내용2");

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");

        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }


    // public 필드만 가져옴
    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    // 접근 제어자와 상관없이 모든 필드를 가져옴
    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    // 특정 이름을 가진 필드를 불러옴
    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    // 필드가 주어지면 타입 이름을 가져오기
    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    // private 필드에 접근해 값을 동적으로 변경하기
    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = new Student();
        final Field field = studentClass.getDeclaredField("age");

        field.setAccessible(true); // private 필드에 접근할 수 있도록 하기

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

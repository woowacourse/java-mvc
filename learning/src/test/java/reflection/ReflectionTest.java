package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {

        // 해당 클래스를 바탕으로 Class 객체를 끌어내는 방법
        // (Class 객체는 클래스의 모든 정보(필드 정보, 메서드 정보 등)를 담고 있다.)
        final Class<Question> clazz = Question.class;

        // 패키 경로가 포함되지 않은 클래스명
        assertThat(clazz.getSimpleName()).isEqualTo("Question");

        // 패키지명이 포함된 클래스명
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // Class의 이름(String)을 통해 Class 객체를 끌어내는 방법
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();

        // Student 클래스에 존재하는 변수(필드)들을 배열에 담아서 리턴한다.
        final Field[] fields = student.getClass().getDeclaredFields();

        // 변수명 출력 : Field.getName() -> ex) fields[0].getName()
        final List<String> actualFieldNames = Arrays.stream(fields)
            .map(Field::getName)
            .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;

        // Student 클래스에 존재하는 메서드들을 배열에 담아서 리턴한다.
        final Method[] methods = animalClass.getDeclaredMethods();

        // 메서드명 출력 : Method.getName() -> ex) methods[0].getName()
        final List<String> actualMethods = Arrays.stream(methods)
            .map(Method::getName)
            .collect(Collectors.toList());

        assertThat(actualMethods)
            .hasSize(3)
            .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;

        // Question 클래스에 존재하는 생성자들을 배열에 담아서 리턴한다.
        final Constructor<?>[] constructors = questionClass.getConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // Reflection을 활용한 인스턴스 생성 방법
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");

        final Constructor<?> secondConstructor =
            questionClass.getConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);
        final Question secondQuestion = (Question) secondConstructor.newInstance(1, "gugu", "제목2", "내용2", Date.from(Instant.EPOCH), 0);

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
        // public으로 선언된 필드에 대해서만 불러옴
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        // public, private에 상관없이 선언된 모든 필드를 불러옴
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        // 변수명(string)을 통해 해당 클래스의 필드 객체 불러오기
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        // Field의 타입(Type) 형태 불러오기
        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        // Student 인스턴스 생성
        final Student student = (Student) studentClass.getConstructor().newInstance();
        // age라는 Field 불러오기
        final Field field = studentClass.getDeclaredField("age");
        // age라는 private 필드에 접근 가능하도록 설정
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // 특정 인스턴스의 private 필드 값 바꾸기
        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

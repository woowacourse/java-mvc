package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.Question.InnerQuestion;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> questionClass = Question.class; // 컴파일 타임에 정적 로딩

        assertThat(questionClass.getSimpleName()).isEqualTo("Question"); // 이름
        assertThat(questionClass.getPackageName()).isEqualTo("reflection"); // 패키지
        assertThat(questionClass.getName()).isEqualTo("reflection.Question"); // 패키지 포함한 이름

        final Class<InnerQuestion> innerQuestionClass = Question.InnerQuestion.class;
        assertThat(innerQuestionClass.getName())
                .isEqualTo("reflection.Question$InnerQuestion"); // 내부 클래스는 $으로 구분
        assertThat(innerQuestionClass.getCanonicalName())
                .isEqualTo("reflection.Question.InnerQuestion"); // 내부 클래스는 .로 구분
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> questionClass = Class.forName("reflection.Question"); // 런 타임에 동적 로딩

        assertThat(questionClass.getSimpleName()).isEqualTo("Question"); // 이름
        assertThat(questionClass.getPackageName()).isEqualTo("reflection"); // 패키지
        assertThat(questionClass.getName()).isEqualTo("reflection.Question"); // 패키지 포함한 이름

        final Class<?> innerQuestionClass = Class.forName("reflection.Question$InnerQuestion");
        assertThat(innerQuestionClass.getName())
                .isEqualTo("reflection.Question$InnerQuestion"); // 내부 클래스는 $으로 구분
        assertThat(innerQuestionClass.getCanonicalName())
                .isEqualTo("reflection.Question.InnerQuestion"); // 내부 클래스는 .로 구분
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student(); // 런 타임에 동적 로딩
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods(); // 상속 받은 거 제외한 모든 메서드
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

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
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getConstructor(
                String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(
                long.class, String.class, String.class, String.class, Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance(
                "gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(
                1L, "gugu", "제목2", "내용2", new Date(), 0);

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
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getConstructor().newInstance();
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

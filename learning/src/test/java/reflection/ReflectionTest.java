package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        // given
        final Class<Question> clazz = Question.class;

        // when, then
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // given
        final Class<?> clazz = Class.forName("reflection.Question");

        // when, then
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        // given
        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();

        // when
        final List<String> actualFieldNames = Arrays.stream(fields)
            .map(Field::getName)
            .collect(Collectors.toList());

        // then
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        // given
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();

        // when
        final List<String> actualMethods = Arrays.stream(methods)
            .map(Method::getName)
            .collect(Collectors.toList());

        // then
        assertThat(actualMethods)
            .hasSize(3)
            .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        // given
        final Class<?> questionClass = Question.class;

        // when
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        // then
        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        // given
        final Class<?> questionClass = Question.class;
        final Constructor<?> firstConstructor =
            questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor =
            questionClass.getDeclaredConstructor(
                long.class,
                String.class,
                String.class,
                String.class,
                Date.class,
                int.class
            );

        // when
        final Question firstQuestion =
            (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion =
            (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(), 0);

        // then
        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        // given
        final Class<?> questionClass = Question.class;

        // when
        final Field[] fields = questionClass.getFields();

        // then
        assertThat(fields).isEmpty();
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        // given
        final Class<?> questionClass = Question.class;

        // when
        final Field[] fields = questionClass.getDeclaredFields();

        // then
        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        // given
        final Class<?> questionClass = Question.class;

        // when
        final Field field = questionClass.getDeclaredField("questionId");

        // then
        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        // given
        final Field field = Question.class.getDeclaredField("questionId");

        // when
        final Class<?> fieldClass = field.getType();

        // then
        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        // given
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // when, then
        field.setAccessible(true);
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);
        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

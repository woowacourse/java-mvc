package reflection;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.DATE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        // given
        final Class<Question> clazz = Question.class;

        // expected
        assertSoftly(softly -> {
            softly.assertThat(clazz.getSimpleName()).isEqualTo("Question");
            softly.assertThat(clazz.getName()).isEqualTo("reflection.Question");
            softly.assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
        });
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // given
        final Class<?> clazz = Class.forName("reflection.Question");

        // expected
        assertSoftly(softly -> {
            softly.assertThat(clazz.getSimpleName()).isEqualTo("Question");
            softly.assertThat(clazz.getName()).isEqualTo("reflection.Question");
            softly.assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
        });
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        // guven
        final Object student = new Student();
        // getFields() 메서드는 public 필드만 가져온다.
        final Field[] fields = student.getClass().getDeclaredFields();

        // when
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toUnmodifiableList());

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
                .collect(Collectors.toUnmodifiableList());

        // then
        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        // given
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        // expected
        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        // given
        final Class<Question> questionClass = Question.class;

        final Constructor<Question> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<Question> secondConstructor = questionClass.getConstructor(
                long.class,
                String.class,
                String.class,
                String.class,
                Date.class,
                int.class
        );

        // when
        final Question firstQuestion = firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(), 0);

        // then
        assertSoftly(softly -> {
            softly.assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
            softly.assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
            softly.assertThat(firstQuestion.getContents()).isEqualTo("내용1");
            softly.assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
            softly.assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
            softly.assertThat(secondQuestion.getContents()).isEqualTo("내용2");
        });
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        // given
        final Class<Question> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        // expected
        assertThat(fields).hasSize(6);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        // given
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        // expected
        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        // given
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        // expected
        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        // given
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        // expected
        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        // given
        final Class<Student> studentClass = Student.class;
        final Student student = studentClass.getDeclaredConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // when
        // todo field에 접근 할 수 있도록 만든다.
        field.setAccessible(true);

        // then
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // when
        field.set(student, 99);

        // then
        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

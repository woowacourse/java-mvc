package reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {

        // given
        final Class<Question> clazz = Question.class;

        // when
        final String simpleName = clazz.getSimpleName();
        final String name = clazz.getName();
        final String canonicalName = clazz.getCanonicalName();

        // then
        assertThat(simpleName).isEqualTo("Question");
        assertThat(name).isEqualTo("reflection.Question");
        assertThat(canonicalName).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {

        // given
        final Class<?> clazz = Class.forName("reflection.Question");

        // when
        final String simpleName = clazz.getSimpleName();
        final String name = clazz.getName();
        final String canonicalName = clazz.getCanonicalName();

        // then
        assertThat(simpleName).isEqualTo("Question");
        assertThat(name).isEqualTo("reflection.Question");
        assertThat(canonicalName).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {

        // given
        final Field[] fields = Student.class.getDeclaredFields();

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
        final Method[] methods = Student.class.getDeclaredMethods();

        // when
        final List<String> actualMethods = Arrays.stream(methods)
                                                 .map(Method::getName)
                                                 .collect(Collectors.toList());

        // then
        assertThat(actualMethods).hasSize(3)
                                 .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {

        // given
        final Class<?> questionClass = Question.class;

        // when
        final Constructor<?>[] constructors = questionClass.getConstructors();

        // then
        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {

        // given
        final Class<Question> questionClass = Question.class;
        final Constructor<Question> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<Question> secondConstructor = questionClass.getConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        // when
        final Question firstQuestion = firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = secondConstructor.newInstance(1, "gugu", "제목2", "내용2", Date.from(Instant.now()), 0);

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
        final Class<Student> studentClass = Student.class;
        final Student student = studentClass.getConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // when
        field.set(student, 99);

        // then
        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

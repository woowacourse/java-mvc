package reflection;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() throws IllegalAccessException {
        // given
        Object student = new Student();

        // when
        Field[] fields = student.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.get(student);
        }

        // then
        List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(toList());
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() throws InvocationTargetException, IllegalAccessException {
        // given
        Class<?> animalClass = Student.class;

        // when
        Method[] methods = animalClass.getDeclaredMethods();

        // then
        List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .collect(toList());

        assertThat(actualMethods)
                .hasSize(3)
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
        final Class<?> questionClass = Question.class;

        // when
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(), 10);

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
        assertThat(fields).hasSize(0);
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
        final Student student = new Student();
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

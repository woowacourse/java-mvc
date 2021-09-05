package reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        //given
        final Class<Question> clazz = Question.class;
        //when
        //then
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        //given
        final Class<?> clazz = Class.forName("reflection.Question");
        //when
        //then
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        //given
        final Object student = new Student();
        //when
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.toList());
        //then
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        //given
        final Class<?> animalClass = Student.class;
        //when
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Stream.of(methods)
                .map(Method::getName)
                .collect(Collectors.toList());
        //then
        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        //given
        final Class<?> questionClass = Question.class;
        //when
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();
        //then
        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        //given
        final Class<?> questionClass = Question.class;
        //when
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(String.class, String.class, String.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance("gugu", "제목2", "내용2");
        //then
        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        //given
        final Class<?> questionClass = Question.class;
        //when
        final Field[] fields = questionClass.getFields();
        //then
        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        //given
        final Class<?> questionClass = Question.class;
        //when
        final Field[] fields = questionClass.getDeclaredFields();
        //then
        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        //given
        final Class<?> questionClass = Question.class;
        //when
        final Field field = questionClass.getDeclaredField("questionId");
        //then
        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        //given
        final Field field = Question.class.getDeclaredField("questionId");
        //when
        final Class<?> fieldClass = field.getType();
        //then
        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        //given
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getConstructor().newInstance();
        final Field field = student.getClass().getDeclaredField("age");
        //when
        // todo field에 접근 할 수 있도록 만든다.
        field.setAccessible(true);
        //then
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

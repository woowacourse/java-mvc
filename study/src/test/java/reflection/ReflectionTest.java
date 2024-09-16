package reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        log.info("clazz.getSimpleName()={}", clazz.getSimpleName());
        log.info("clazz.getName()={}", clazz.getName());
        log.info("clazz.getCanonicalName()={}", clazz.getCanonicalName());

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        log.info("clazz.getSimpleName()={}", clazz.getSimpleName());
        log.info("clazz.getName()={}", clazz.getName());
        log.info("clazz.getCanonicalName()={}", clazz.getCanonicalName());

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();

        // public fields
        final Field[] fields = student.getClass().getFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();
        log.info("getFields()={}", actualFieldNames);

        // private + public fields
        final Field[] declaredFields = student.getClass().getDeclaredFields();
        final List<String> actualDeclaredFields = Arrays.stream(declaredFields)
                .map(Field::getName)
                .toList();
        log.info("getDeclaredFields()={}", actualDeclaredFields);

        assertAll(
                () -> assertThat(actualFieldNames).hasSize(0),
                () -> assertThat(actualDeclaredFields).hasSize(2).contains("name", "age")
        );
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;

        // public method
        final Method[] methods = animalClass.getMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();
        log.info("getMethods()={}", actualMethods);

        //declared public + private
        final Method[] declaredMethods = animalClass.getDeclaredMethods();
        final List<String> actualDeclaredMethods = Arrays.stream(declaredMethods)
                .map(Method::getName)
                .toList();
        log.info("getDeclaredMethods()={}", actualDeclaredMethods);

        assertAll(
                () -> assertThat(actualMethods)
                        .contains("getAge", "toString", "getName", "equals", "hashCode", "getClass", "notify", "notifyAll", "wait", "wait", "wait"),
                () -> assertThat(actualDeclaredMethods).hasSize(3)
                        .contains("getAge", "toString", "getName")
        );
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;

        final Constructor<?>[] constructors = questionClass.getConstructors();
        log.info("getConstructors()={}", Arrays.stream(constructors).toList());

        Constructor<?>[] declaredConstructors = questionClass.getDeclaredConstructors();
        log.info("declaredConstructors()={}", Arrays.stream(declaredConstructors).toList());

        assertAll(
                () -> assertThat(constructors).hasSize(2),
                () -> assertThat(declaredConstructors).hasSize(2)
        );
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructors()[0];
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructors()[1];

        log.info("getParameterTypes()={}", Arrays.stream(firstConstructor.getParameterTypes()).toList());
        log.info("getGenericParameterTypes()={}", Arrays.stream(firstConstructor.getGenericParameterTypes()).toList());
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");

        log.info("getParameterTypes()={}", Arrays.stream(secondConstructor.getParameterTypes()).toList());
        log.info("getGenericParameterTypes()={}", Arrays.stream(secondConstructor.getGenericParameterTypes()).toList());
        final Question secondQuestion = (Question) secondConstructor.newInstance(0L, "gugu", "제목2", "내용2", new Date(), 0);

        assertAll(
                () -> assertThat(firstQuestion.getWriter()).isEqualTo("gugu"),
                () -> assertThat(firstQuestion.getTitle()).isEqualTo("제목1"),
                () -> assertThat(firstQuestion.getContents()).isEqualTo("내용1"),
                () -> assertThat(secondQuestion.getWriter()).isEqualTo("gugu"),
                () -> assertThat(secondQuestion.getTitle()).isEqualTo("제목2"),
                () -> assertThat(secondQuestion.getContents()).isEqualTo("내용2")
        );
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

        assertAll(
                () -> assertThat(fields).hasSize(6),
                () -> assertThat(fields[0].getName()).isEqualTo("questionId")
        );
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

        field.setAccessible(true);

        assertAll(
                () -> assertThat(field.getInt(student)).isZero(),
                () -> assertThat(student.getAge()).isZero()
        );

        field.set(student, 99);

        assertAll(
                () -> assertThat(field.getInt(student)).isEqualTo(99),
                () -> assertThat(student.getAge()).isEqualTo(99)
        );
    }
}

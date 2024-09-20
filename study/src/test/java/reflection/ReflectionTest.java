package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    private static List<String> getFieldNames(Field[] studentDeclaredFields) {
        return Arrays.stream(studentDeclaredFields)
                .map(Field::getName)
                .toList();
    }

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<reflection.Question> clazz = reflection.Question.class;

        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo("Question"),
                () -> assertThat(clazz.getName()).isEqualTo("reflection.Question"),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question")
        );
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = reflection.Question.class;
        final Class<?> innerClazz = Question.class;
        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo("Question"),
                () -> assertThat(clazz.getName()).isEqualTo("reflection.Question"),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question"),
                () -> assertThat(innerClazz.getSimpleName()).isEqualTo("Question"),
                () -> assertThat(innerClazz.getName()).isEqualTo("reflection.ReflectionTest$Question"),
                () -> assertThat(innerClazz.getCanonicalName()).isEqualTo("reflection.ReflectionTest.Question")
        );
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        final Object subStudent = new SubStudent();

        final Field[] studentDeclaredFields = student.getClass().getDeclaredFields();
        final Field[] subStudentFields = subStudent.getClass().getFields();
        final Field[] subStudentDeclaredFields = subStudent.getClass().getDeclaredFields();

        final List<String> studentDeclaredFieldNames = getFieldNames(studentDeclaredFields);
        final List<String> subStudentFieldNames = getFieldNames(subStudentFields);
        final List<String> subStudentDeclaredFieldNames = getFieldNames(subStudentDeclaredFields);

        assertAll(
                () -> assertThat(studentDeclaredFieldNames).contains("name", "age", "subjectCount"),
                () -> assertThat(subStudentFieldNames).contains("subjectCount"),
                () -> assertThat(subStudentDeclaredFieldNames).contains("degree")
        );
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = reflection.Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Class.forName("reflection.Question");

        List<Constructor<?>> declaredConstructors = List.of(questionClass.getDeclaredConstructors());
        final Constructor<?> firstConstructor = declaredConstructors.get(0);
        final Constructor<?> secondConstructor = declaredConstructors.get(1);

        final reflection.Question firstQuestion = (reflection.Question) firstConstructor.newInstance("gugu", "제목1",
                "내용1");
        final reflection.Question secondQuestion = (reflection.Question) secondConstructor.newInstance(1, "gugu", "제목2",
                "내용2", new Date(), 0);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = reflection.Question.class;
        List<Field> fields = Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> field.getModifiers() == Modifier.PUBLIC)
                .toList();

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = reflection.Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = reflection.Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = reflection.Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = new Student();
        final Field field = studentClass.getDeclaredField("age");

        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }

    class Question {
    }

    class SubStudent extends Student {
        private int degree;
    }
}

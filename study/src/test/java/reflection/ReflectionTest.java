package reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        // 패키지 제외 순수 클래스명
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        // 패키지 포함한 클래스명
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        // 위와 동일하나, 특수 경우에 다름.
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");

//        final CanonicalInterface canonical = new CanonicalInterface() {
//            @Override
//            public void execute() {
//
//            }
//        };
        // reflection.ReflectionTest$1 와 같이 익명 클래스 출력
        // System.out.println(canonical.getClass().getName());

        // 표준적 이름 존재하지 않으므로, null 출력
        // System.out.println(canonical.getClass().getCanonicalName());
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // 문자열에 해당된 클래스,인터페이스 반환
        // 현재 클래스가 속한 클래스 로더를 사용해 호출
        // => 동적으로 로딩 ( 로딩,연결,초기화가 발생할 수도 있음 - 기존 의도적으로 Disable 해놨을 시 )
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz).isEqualTo(Question.class);
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Class<Student> clazz = Student.class;
        final Field[] fields = clazz.getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> clazz = Student.class;
        final Method[] methods = clazz.getDeclaredMethods();
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
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");

        // 없는 메소드 호출 시, NoSuchMethodException
        // 매개변수 올바르지 않을시, IllegalArgumentException
        final Question secondQuestion = (Question) secondConstructor.newInstance(
                1, "gugu", "제목2", "내용2", new Date(), 0
        );

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
        final Field[] fields = Arrays.stream(questionClass.getFields())
                .toArray(Field[]::new);

        assertThat(fields).isEmpty();
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
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student,99);

        assertThat(field.getInt(student)).isEqualTo(99);
        field.setAccessible(false);

        assertThat(student.getAge()).isEqualTo(99);
    }
}

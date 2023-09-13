package reflection;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        // getSimpleName() :클래스 이름만 반환 (패키지 정보 제외)
        assertThat(clazz.getSimpleName()).isEqualTo("Question");

        // getName(): 클래스 이름을 패키지 정보와 함께 반환
        assertThat(clazz.getName()).isEqualTo("reflection.Question");

        // getCanonicalName(): 클래스의 정규화된 이름을 반환
        // 클래스가 배열이나 내부 클래스와 같이 특수한 경우에 유용함
        // com.example.Question$Inner와 같은 내부 클래스 -> com.example.Question.Inner
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
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();

        /**
         * getFields()
         * 클래스의 public 필드만 가져온다.
         * 상위 클래스의 public 필드도 포함된다.
         * 가져온 필드의 순서 보장 X
         *
         * getDeclaredFields()
         * 클래스의 모든 필드를 가져온다. (public, protected, default, private)
         * 상위 클래스의 필드는 가져오지 않는다.
         * 가져온 필드의 순서 보장 X
         */
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
            .map(Field::getName)
            .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        /**
         * getMethods()
         * public 메서드만 가져온다.
         * 상위 클래스에서 상속한 메서드도 포함된다.
         * `Object` 클래스에서 상속한 메서드 (ex. toString(), equals(), hashCode())도 포함된다.
         * 가져온 메서드 순서 보장 X
         *
         * getDeclaredMethods()
         * 현재 클래스에서 선언된 모든 메서드를 가져온다.
         * 상위 클래스에서 상속한 메서드는 포함하지 않는다.
         * `Object` 클래스에서 상속한 메서드도 포함되지 않는다.
         * 가져온 메서드 순서 보장 X
         */
        final Method[] methods = animalClass.getDeclaredMethods();
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
        /**
         * getConstructors()
         * public 생성자만 가져온다.
         * 상위 클래스에서 상속한 생성자도 포함된다.
         * `Object` 클래스에서 상속한 기본 생성자도 포함된다.
         *
         * getDeclaredConstructors()
         * 현재 클래스에서 선언한 모든 생성자를 가져온다.
         * 사우이 클래스에서 상속한 생성자는 포함 X
         * 즉, 현재 클래스에서 직접 선언한 생성자만 가져온다.
         */
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(long.class, String.class, String.class, String.class,
            Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu","제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(0, "gugu", "제목2", "내용2", new Date(), 0);

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
        // getDeclaredFields 는 순서를 보장하지 않는데, 어떻게 통과할까?
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

        // todo field에 접근 할 수 있도록 만든다.
        // private 필드에 접근하려면 기본저긍로 접근 권한이 필요하다.
        // setAccessible(true) 하면 해당 필드에 대한 접근 제한이 해제됨 -> 리플렉션을 통해 필드의 값을 가져오거나 설정할 수 있게됨.
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

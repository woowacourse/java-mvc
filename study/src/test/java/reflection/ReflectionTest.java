package reflection;

import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        // 단순 이름은 패키지를 제외하고, 일반 클래스의 전체 이름과 정식 이름은 패키지를 포함한다.
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // 패키지를 포함한 이름으로 클래스 정보를 가져온다. 객체를 생성하는 것은 아니다.
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        // 객체의 클래스에서 직접 선언한 필드를 찾는다. private도 포함하고 상속받은 필드는 제외한다.
        final Field[] fields = student.getClass().getDeclaredFields();
        // 각 Field에서 이름을 꺼내 문자열 리스트로 모은다.
        final List<String> actualFieldNames = Arrays.stream(fields).map(Field::getName).toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        // 접근 제어자와 관계없이 이 클래스에 직접 선언된 메서드를 가져온다.
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods).map(Method::getName).toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        // public 생성자들을 가져온다.
        final Constructor<?>[] constructors = questionClass.getConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // 매개변수의 타입과 순서로 생성자를 찾는다.
        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        // 찾은 생성자에 실제 값을 전달해 객체를 생성한다.
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(), 0);

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
        // 상속받은 필드까지 포함해 public 필드만 가져온다. Question의 필드는 모두 private이다.
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        // private을 포함해 직접 선언한 필드를 가져온다. 반환 순서는 보장되지 않는다.
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        // 이름으로 직접 선언된 필드 하나를 찾는다.
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        // 필드에 저장된 값이 아니라, 선언된 타입 정보를 가져온다.
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        // 매개변수가 없는 생성자로 값을 읽고 변경할 대상 객체를 만든다.
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();
        final Field field = student.getClass().getDeclaredField("age");

        // private 필드의 접근 검사를 해제한다. 필드 자체가 public으로 바뀌는 것은 아니다.
        field.setAccessible(true);
        // 지정한 객체의 필드 값을 int로 읽는다.
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // student 객체의 age를 변경한다. 일반 getter로 읽어도 변경된 값이 나온다.
        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

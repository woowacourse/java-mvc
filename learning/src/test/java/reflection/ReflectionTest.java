package reflection;

import org.junit.jupiter.api.DisplayName;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("클래스의 SimpleName, 표준에 다른 이름, 패키지 경로를 포함한 SimpleName을 알 수 있다.")
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        // 전체 경로명의 클래스 명
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        // 자바 언어 명세에 따른 클래스 명
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        // 패키지 경로 + SimpleName
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    @DisplayName("패키지 경로를 포함한 클래스 이름으로부터 클래스를 찾아올 수 있다.")
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    @DisplayName("인스턴스로부터 클래스를 추출하고, 선언된 필드의 정보를 가져올 수 있다.")
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        Class<?> studentClass = student.getClass();
        final Field[] fields = studentClass.getDeclaredFields();    // Student 에서 선언된 필드만을 가져온다.
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    @DisplayName("클래스로부터 선언된 메소드의 정보를 가져올 수 있다.")
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();  // Student 에서 선언된 메소드만을 가져온다.
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    @DisplayName("클래스로부터 생성자를 가져올 수 있다.")
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    @DisplayName("클래스로부터 가져온 생성자로 인스턴스를 생성할 수 있다.")
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        Constructor<?>[] declaredConstructors = questionClass.getDeclaredConstructors();

        final Constructor<?> firstConstructor = declaredConstructors[0];
        final Constructor<?> secondConstructor = declaredConstructors[1];

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(0, "gugu", "제목2", "내용2", new Date(), 0);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");

        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("클래스로부터 필드의 접근제어자를 알 수 있다.")
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();
        List<Field> publicFields = Arrays.stream(fields)
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .collect(Collectors.toList());

        assertThat(publicFields).hasSize(0);
    }

    @Test
    @DisplayName("Class.getDeclaredFields() 메소드는 선언된 순서에 따라 필드 인스턴스를 리턴한다.")
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    @DisplayName("필드명으로 클래스의 필드 인스턴스를 가져올 수 있다.")
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    @DisplayName("필드 인스턴스로부터 타입을 알아낼 수 있다.")
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    @DisplayName("필드에 접근 가능하도록 설정을 변경하고, 값을 바꿀 수 있다.")
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getConstructor().newInstance();
        final Field field = student.getClass().getDeclaredField("age");

        // field에 접근 할 수 있도록 만든다.
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

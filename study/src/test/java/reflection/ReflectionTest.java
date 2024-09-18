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

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }


    /**
     * Class.forName() -> 동적 로딩
     * 보통 다른 클래스 파일을 불러올때는 컴파일 시
     * JVM의 Method Area에 클래스 파일이 같이 바인딩(binding)이 되지만,
     * forName()으로 .class파일을 불러올 때는 컴파일에 바인딩이 되지않고
     * 런타임때 불러오게 되기 때문에 동적 로딩이라고 부른다.
     * 그래서 컴파일 타입에 체크 할 수 없기 때문에 클래스 유무가 확인되지 않아 예외 처리를 해주어야 한다.
     * 출처: https://inpa.tistory.com/entry/JAVA-☕-누구나-쉽게-배우는-Reflection-API-사용법 [Inpa Dev 👨‍💻:티스토리]
     */
    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    /**
     * getFields()는 public 필드만 반환하므로,
     * 모든 필드를 가져오려면 getDeclaredFields()를 사용
     */
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
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
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getConstructors();

        assertThat(constructors).hasSize(2);
    }

    /**
     * getConstructors()는 public 메서드만 가져오기 때문에,
     * private 메서드 까지 가져오려면 getDeclaredConstructors()를 사용해야 한다.
     */
    @Test
    void givenClass_whenGetsSpecificConstructor_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Constructor<?> constructor = questionClass.getConstructor(String.class, String.class, String.class);

        assertThat(constructor.getParameterTypes()).hasSize(3);
        assertThat(constructor.getParameterTypes()[0]).isEqualTo(String.class);
        assertThat(constructor.getParameterTypes()[1]).isEqualTo(String.class);
        assertThat(constructor.getParameterTypes()[2]).isEqualTo(String.class);
    }

    /**
     * getConstructors()는 public 메서드만 가져오기 때문에,
     * private 메서드 까지 가져오려면 getDeclaredConstructors()를 사용해야 한다.
     * getConstructors()는 Constructor<?>[] 배열로 나오는데,
     * 선언된 순서로 생성자가 담겨있다.
     */
    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getConstructors()[0];
        final Constructor<?> secondConstructor = questionClass.getConstructors()[1];

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(), 0);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    /**
     * getFields()는 public 필드만 반환하기 때문에,
     * private 필드를 가져오려면 getDeclaredFields()를 사용해야 한다.
     */
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
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    /**
     * getField()
     * 상위 클래스의 public 필드도 포함하여 클래스 계층 구조를 따라 검색한다.
     * 만약 필드가 public이 아니거나 존재하지 않는 경우, NoSuchFieldException이 발생한다.
     *
     * getDeclaredField()
     * 매개변수로 지정된 이름의 모든 접근 제어자를 가진 필드를 반환한다.
     * 이 메서드는 오직 해당 클래스에 선언된 필드만 반환하고, 상위 클래스의 필드를 검색하지 않는다.
     */
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
        final Field field = student.getClass().getDeclaredField("age");

        // todo field에 접근 할 수 있도록 만든다. private 접근 허용
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99); // student 객체의 age 필드에 99를 설정

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

package reflection;

import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Class 객체로, 클래스 이름을 SimpleName, Name, CanonicalName으로 가져온다.")
    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @DisplayName("Class.forName()으로, 문자열 클래스 이름에서 Class 객체를 가져온다.")
    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question"); // 클래스 이름을 문자열로만 알 때, Class 객체 가져옴

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @DisplayName("객체의 getClass()로, 선언된 필드 이름 목록을 가져온다.")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @DisplayName("getDeclaredMethods()는 상속받은 메서드를 제외하고 직접 선언된 메서드만 가져온다.")
    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName"); // Object의 toString()이 아니라, 오버라이딩 된 메서드라 포함됨
    }

    @DisplayName("getDeclaredConstructors()로 선언된 생성자를 모두 가져온다.")
    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @DisplayName("파라미터 타입으로 생성자를 찾아 newInstance()로 객체를 생성한다.")
    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // getMethod("test")와 달리 생성자는 이름이 전부 클래스 이름이라 이름으로 구분할 수 없어서, 파라미터 타입을 .class 로 넘김
        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1, "gugu", "제목2", "내용2", new Date(), 1);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @DisplayName("getFields()는 public 필드만 가져온다.")
    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @DisplayName("getDeclaredFields()는 private을 포함한 선언된 필드를 모두 가져온다.")
    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @DisplayName("getDeclaredField()로 이름에 해당하는 필드를 가져온다.")
    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @DisplayName("Field의 getType()으로 필드의 타입을 가져온다.")
    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType(); // 그 Field가 설명하는 필드의 타입 => long

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @DisplayName("setAccessible(true)로 private 필드의 값을 읽고 변경한다.")
    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance(); // 객체 생성
        final Field ageField = studentClass.getDeclaredField("age");

        ageField.setAccessible(true); // field에 접근 할 수 있도록 만듦

        assertThat(ageField.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        ageField.set(student, 99); // student의 age를 99로 바꿈

        assertThat(ageField.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

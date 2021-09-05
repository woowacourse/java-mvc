package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("객체로 클래스 이름을 얻어온다.")
    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");

        /* getCanonicalName()
         * Java Language Specification에서 정의한 기본 클래스의 정식 이름을 반환합니다.
         * 기본 클래스에 표준 이름이 없는 경우(즉, 로컬 또는 익명 클래스이거나 구성 요소 유형에 표준 이름이 없는 배열인 경우) null을 반환합니다.
         * 반환:
         * 기본 클래스의 표준 이름(있는 경우), 그렇지 않은 경우 null입니다.
         */
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @DisplayName("클래스 이름으로 객체를 생성한다.")
    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @DisplayName("런타임에 클래스의 필드명을 가져온다.")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        /* getDeclaredFields()
         *클래스로 선언된 모든 필드를 반영하는 필드 개체 배열을 반환하거나 이 클래스 개체로 표시되는 인터페이스를 반환합니다.
         * 여기에는 공용, 보호됨, 기본(패키지) 액세스 및 개인 필드가 포함되지만 상속된 필드는 제외됩니다.
         * 이 클래스 개체가 선언된 필드가 없는 클래스 또는 인터페이스를 나타내는 경우 이 메서드는 길이 0의 배열을 반환합니다.
         * 이 클래스 개체가 배열 유형, 원시 유형 또는 void를 나타내는 경우 이 메서드는 길이 0의 배열을 반환합니다.
         * 반환된 배열의 요소는 정렬되지 않으며 특정 순서도 아닙니다.
         * 반환:
         * 이 클래스의 선언된 모든 필드를 나타내는 필드 개체의 배열
         */
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @DisplayName("클래스의 메서드를 가져온다.")
    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());
        ;

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @DisplayName("클래스의 모든 생성자를 가져온다.")
    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @DisplayName("런타임에 인스턴스화된 객체를 가져온다.")
    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(
                long.class, String.class, String.class, String.class, Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1, "gugu", "제목2", "내용2", new Date(), 1);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @DisplayName("public 필드만 가져온다.")
    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).isEmpty();
    }

    @DisplayName("선언된 필드들을 가져온다.")
    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @DisplayName("필드명으로 필드를 가져온다.")
    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @DisplayName("필드의 타입을 가져온다.")
    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @DisplayName("필드 접근 가능을 수정해 그 값을 얻거나 재설정한다.")
    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getConstructor().newInstance();
        final Field field = student.getClass().getDeclaredField("age");
        field.setAccessible(true);

        // todo field에 접근 할 수 있도록 만든다.

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        /*
         *     @CallerSensitive
         *     @ForceInline // to ensure Reflection.getCallerClass optimization
         *     public void set(Object obj, Object value)
         *         throws IllegalArgumentException, IllegalAccessException
         *     {
         *         if (!override) {
         *             Class<?> caller = Reflection.getCallerClass();
         *             checkAccess(caller, obj);
         *         }
         *         getFieldAccessor(obj).set(obj, value);
         *     }
         */

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

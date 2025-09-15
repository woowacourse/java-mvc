package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        // 클래스객체.getSimpleName()
        // - 클래스의 이름을 가져온다.
        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        // 클래스객체.getSimpleName()
        // - JVM 내부에서 사용하는 클래스 이름을 가져온다. (패키지명 포함)
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        // 클래스객체.getCanonicalName()
        // - JVM 내부에서 사용하는 클래스 이름의 경우 일부 알아보기 어려운 경우도 존재한다.
        // - 그렇기에 getCanonicalName()를 활용해 사람일 읽기 좋은 형태로 클래스 이름을 가져온다. (패키지명 포함)
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // Class.forName(클래스명);
        // - 클래스 이름을 통해 클래스 객체를 가져올 수 있다.
        final Class<?> clazz = Class.forName("reflection.Question");

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        // clazz.getDeclaredFields() : 클래스의 모든 필드를 가져옴
        // clazz. getFields() :public 필드만을 가져옴

        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields).map(Field::getName).toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        // clazz.getDeclaredMethods() : 클래스의 모든 메서드를 가져옴
        // clazz. getMethods() : public 메서드만을 가져옴

        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods).map(Method::getName).toList();

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        // clazz.getDeclaredConstructors() : 클래스의 모든 생성자를 가져옴
        // clazz. getConstructors() : public 생성자만을 가져옴

        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // clazz.getDeclaredConstructor(파라미터 Clazz들) : 해당 파라미터 Clazz들을 가진 생성자 탐색
        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class,
                String.class);
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class,
                String.class, String.class, Date.class, int.class);

        // constructure.newInstance(생성자에 넣을 값들) : 생성자로 인스턴스 생성
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2", new Date(),
                3);

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
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        // clazz.getField("필드명") : 필드명으로 클래스의 public 필드 찾기
        // clazz.getDeclaredField("필드명") : 필드명으로 클래스의 public/private 필드 찾기
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        // field.getClass() : 필드 객체의 Field 클래스 타입
        // field.getDeclaringClass() : 해당 필드가 선언된 클래스 타입 리턴
        // field.getType() : 필드의 실제 타입 리턴
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // private 필드에 접근이 가능하도록 한다.
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // 특정 인스턴스의 필드값을 변경한다.
        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

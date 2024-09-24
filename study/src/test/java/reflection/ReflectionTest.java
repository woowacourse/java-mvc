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

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        // 단순 클래스 이름
        assertThat(clazz.getSimpleName()).isEqualTo("Question");

        // 전체 클래스 이름 (패키지 경로 포함)
        assertThat(clazz.getName()).isEqualTo("reflection.Question");  // 실제 패키지 경로로 수정

        // 표준 클래스 이름
        // 지금 예제에서는 전체 클래스와 같지만, InnerClass로 테스트하면 다른 결과가 나온다.
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");  // 실제 패키지 경로로 수정
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

        // 리플렉션을 사용해 Student 클래스의 모든 필드 가져오기
        final Field[] fields = student.getClass().getDeclaredFields();

        // 각 필드의 이름을 가져와 리스트에 추가
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;

        // 선언된 모든 메서드 조회
        final Method[] methods = animalClass.getDeclaredMethods();
        // 메서드 이름 추출
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

        // 선언된 모든 생성자 조회
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // 첫 번째 생성자 (writer, title, contents)
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        // 두 번째 생성자 (writer, title)
        final Constructor<?> secondConstructor = questionClass.getConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        // 첫 번째 생성자를 사용해 객체 생성
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        // 두 번째 생성자를 사용해 객체 생성
        final Question secondQuestion = (Question) secondConstructor.newInstance(1, "gugu", "제목2", "내용2", new Date(), 4444);

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
        // public 필드를 가져오기
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
        // Student 객체 생성
        final Student student = new Student();

        // todo field에 접근 할 수 있도록 만든다.
        // Student 클래스의 'age' 필드에 리플렉션으로 접근
        final Field field = studentClass.getDeclaredField("age");
        // private 필드 접근 허용
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // 필드 값 설정 (age 필드에 99 설정)
        field.setInt(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

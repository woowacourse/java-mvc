package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        // 클래스 타입 얻기
        final Class<Question> clazz = Question.class;

        // 클래스 이름 관련 정보 확인
        assertThat(clazz.getSimpleName()).isEqualTo("Question");               // 단순 이름 (클래스명만)
        assertThat(clazz.getName()).isEqualTo("reflection.Question");          // 패키지 포함 이름
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question"); // 정규화된 이름
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        // 문자열로 클래스 로드
        final Class<?> clazz = Class.forName("reflection.Question");

        // 클래스 이름 검증
        assertThat(clazz.getSimpleName()).isEqualTo("Question");                // 단순 이름 (클래스명만)
        assertThat(clazz.getName()).isEqualTo("reflection.Question");           // 패키지 포함 이름
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");  // 정규화된 이름
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        // Student 클래스에 선언된 모든 필드 가져오기
        final Field[] fields = student.getClass().getDeclaredFields();

        // 필드 이름만 추출
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        // Student 클래스에는 name, age 필드가 있어야 함
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        // Student 클래스에 선언된 모든 메서드 가져오기
        final Method[] methods = animalClass.getDeclaredMethods();

        // 메서드 이름만 추출
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        // Student 클래스에는 getAge, getName, toString 메서드가 있어야 함
        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        // Question 클래스의 모든 생성자 가져오기
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        // 생성자가 2개 있어야 함
        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // (String, String, String) 시그니처를 가진 생성자 가져오기
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(String.class, String.class, String.class);

        // newInstance 로 객체 생성
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance("gugu", "제목2", "내용2");

        // 생성된 객체의 필드 값 검증
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
        // public 필드만 가져오기
        final Field[] fields = questionClass.getFields();

        // Question 클래스에는 public 필드가 없으므로 size = 0
        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        // Question 클래스에 선언된 모든 필드 가져오기
        final Field[] fields = questionClass.getDeclaredFields();

        // 필드 개수와 첫 번째 필드 이름 검증
        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        // 필드 이름으로 특정 필드 가져오기
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        // 특정 필드의 타입 확인
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        // Student 인스턴스 생성
        final Student student = (Student) studentClass.getDeclaredConstructor().newInstance();

        // age 필드 가져오기
        final Field field = studentClass.getDeclaredField("age");

        // private 필드 접근 허용
        field.setAccessible(true);

        // 초기값 검증 (int 기본값 0)
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        // 값 변경 (리플렉션으로 필드 값 설정)
        field.set(student, 99);

        // 변경된 값 검증
        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

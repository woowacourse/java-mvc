package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        // getName(): 클래스의 전체 경로를 포함한 이름을 반환합니다. (예: com.example.Question)
        // getSimpleName(): 패키지 경로를 제외한 클래스명만 반환합니다. (예: Question)
        // getPackageName(): 클래스가 속한 패키지명을 반환합니다. (예: com.example)
        // getSuperclass(): 부모 클래스의 Class 객체를 반환합니다.
        // getInterfaces(): 구현하고 있는 인터페이스들을 Class[] 배열로 반환합니다.
        // getModifiers(): 클래스의 제어자(public, abstract, final 등)를 정수(int) 형태로 반환합니다. (Modifier 클래스와 함께 해석 필요)
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
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
        // getFields(): 해당 클래스와 상속받은 모든 public 필드를 Field[] 배열로 반환
        // getDeclaredFields(): 상속된 것은 제외하고, 해당 클래스에 직접 선언된 모든 필드(private 포함)를 반환
        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        log.debug("추출된 필드 이름 리스트: {}", actualFieldNames);
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        // getMethods(): 해당 클래스와 부모 클래스/인터페이스로부터 상속받은 모든 public 메서드를 Method[] 배열로 반환
        // getDeclaredMethods(): 상속된 것은 제외하고, 해당 클래스에 직접 선언된 모든 메서드(private, protected 포함)를 반환
        final Class<?> animalClass = Student.class;
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
        // getConstructors(): 모든 생성자를 Constructor[] 배열로 반환
        // getDeclaredConstructors(): 접근 제어자에 상관없이(private 포함) 클래스에 선언된 모든 생성자를 반환
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();
        log.debug("{}", (Object) constructors);

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // getConstructor(ParameterType.class, ..): 특정 파라미터 생성자들을 가지고있는 생성자 가져오기
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(String.class, String.class, String.class);

        // newInstance(인자...): 인자값을 가지는 객체 생성
        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance("gugu", "제목2", "내용2");

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        // getFields(): 해당 클래스와 상속받은 모든 public 필드를 Field[] 배열로 반환
        // getDeclaredFields(): 상속된 것은 제외하고, 해당 클래스에 직접 선언된 모든 필드(private 포함)를 반환
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        // getFields(): 해당 클래스와 상속받은 모든 public 필드를 Field[] 배열로 반환
        // getDeclaredFields(): 상속된 것은 제외하고, 해당 클래스에 직접 선언된 모든 필드(private 포함)를 반환
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
        final Student student = new Student();
        final Field field = studentClass.getDeclaredField("age");

        // todo field에 접근 할 수 있도록 만든다.
        // Student 클래스의 age 필드가 private이더라도 자바의 접근 제어를 무시하고 읽고 쓸 수 있도록 허용
        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

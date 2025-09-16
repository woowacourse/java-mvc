package reflection;

import java.util.Arrays;
import org.assertj.core.api.SoftAssertions;
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
        final Class<Question> clazz = Question.class; //얘는 컴파일 타임에 이미 결정이 된 녀석임 컴파일러가 이미 저녀석을 알고 있음

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(clazz.getSimpleName()).isEqualTo("Question");
            softAssertions.assertThat(clazz.getName()).isEqualTo("reflection.Question");
            softAssertions.assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
        });
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question"); //런타임에 검색함, 만약에 클래스가 없으면 ClassNotFoundException 발생
        //Class<?>니까 런타임까지 정확한 타입도 모르네
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(clazz.getSimpleName()).isEqualTo("Question");
            softAssertions.assertThat(clazz.getName()).isEqualTo("reflection.Question");
            softAssertions.assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
        });
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        //런타임에 객체의 필드 가져오기
        final Object student = new Student();
        //런타임이니까 타입을 런타임까지 모름
        final Class<?> clazz = student.getClass();
        //getFields랑 getDeclareFields랑 뭔차이지?
        //아하 getFields는 상위 클래스까지 다 가져옴, 부모 객체, 만약에 Person이 있으면 ㄷㄷ
        //getDelcaredFields는 해당 클래스만
        final Field[] fields = clazz.getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                        .map(Field::getName)
                                .toList();

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
        // 아래로 가져오면 파라미터 없는 생성자를 가져오려고 해서 안될거임 아마
        //왜 됨? 아하, Question은 파라미터가 있는 생성자가 2개나 있는 상황임
        //그래ㅐ서 컴파일 타임에 파라미터가 없는 기본 생성자를 만들지 않은 것임
        //그래새ㅓ 3이 아닌 2가 나오니까 통과가 된거네
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        //일단 생성자들을 다 가져와야할듯
        final Constructor<?> constructor = questionClass.getConstructor(String.class, String.class, String.class);

        final Question firstQuestion = (Question) constructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) constructor.newInstance("gugu", "제목2", "내용2");

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
        final Field nameField = student.getClass().getDeclaredField("name");
        final Field ageField = student.getClass().getDeclaredField("age");
        // todo field에 접근 할 수 있도록 만든다.
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        assertThat(ageField.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        nameField.set(student, null);
        ageField.set(student, 99);

        assertThat(ageField.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

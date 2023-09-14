package reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;
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
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        logInfo(Question.class);

        assertSoftly(softly -> {
            softly.assertThat(clazz.getSimpleName()).isEqualTo("Question");
            softly.assertThat(clazz.getName()).isEqualTo("reflection.Question");
            softly.assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
        });
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        assertSoftly(softly -> {
            softly.assertThat(clazz.getSimpleName()).isEqualTo("Question");
            softly.assertThat(clazz.getName()).isEqualTo("reflection.Question");
            softly.assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
        });
    }

    @DisplayName("ReflectionUtils를 이용해서 Student 클래스 내부 필드를 가져오는데 성공한다.")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrectV1() {
        final List<String> actualFieldNames = ReflectionUtils.getAllFields(Student.class)
                .stream()
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @DisplayName("Student 인스턴스를 Object 타입으로 받은 후 Object.getClass 기능을 이용해 클래스 내부 필드를 가져오는데 성공한다.")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrectV2() {
        final Object student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();

        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @DisplayName("Student 인스턴스를 Student 타입으로 받은 후 Object.getClass 기능을 이용해 클래스 내부 필드를 가져오는데 성공한다.")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrectV3() {
        final Student student = new Student();
        final Field[] fields = student.getClass().getDeclaredFields();

        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @DisplayName("Student 클래스로부터 내부에 정의된 필드를 가져오는데 성공한다.")
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrectV4() {
        final List<String> actualFieldNames = Arrays.stream(Student.class.getDeclaredFields())
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
                .collect(Collectors.toList());

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructors()[0];
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructors()[1];

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
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrectV2() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructors()[0];
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructors()[1];

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

        logInfo(questionClass);

        final Field[] fields = questionClass.getFields();


        assertThat(fields).isEmpty();
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
        final Field field = studentClass.getDeclaredField("age");

        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }

    private void logInfo(final Class<?> clazz) {
        log.info("============================> Class.getClassLoader()          = {}", clazz.getClassLoader());
        log.info("============================> Class.getSuperclass()           = {}", clazz.getSuperclass());
        log.info("============================> Class.getClasses()              = {}", Arrays.stream(clazz.getClasses()).collect(Collectors.toList()));
        log.info("============================> Class.getClass()                = {}", clazz.getClass());
        log.info("============================> Class.getEnclosingClass()       = {}", clazz.getEnclosingClass());
        log.info("============================> Class.getDeclaringClass()       = {}", clazz.getDeclaringClass());
        log.info("============================> Class.getModifiers()             = {}", clazz.getModifiers());

        log.info("============================> Class.toString()                = {}", clazz.toString());
        log.info("============================> Class.toGenericString()         = {}", clazz.toGenericString());

        log.info("============================> Class.getFields()               = {}", Arrays.stream(clazz.getFields()).collect(Collectors.toList()));
        log.info("============================> Class.getDeclaredFields()       = {}", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));

        log.info("============================> Class.getSimpleName()           = {}", clazz.getSimpleName());
        log.info("============================> Class.getCanonicalName()        = {}", clazz.getCanonicalName());
        log.info("============================> Class.getName()                 = {}", clazz.getName());

        log.info("============================> Class.getMethods()              = {}", Arrays.stream(clazz.getMethods()).collect(Collectors.toList()));
        log.info("============================> Class.getInterfaces()           = {}", Arrays.stream(clazz.getInterfaces()).collect(Collectors.toList()));
        log.info("============================> Class.getEnclosingMethod()      = {}", clazz.getEnclosingMethod());
        log.info("============================> Class.getDeclaredMethods()      = {}", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
        log.info("============================> Class.getGenericInterfaces()    = {}", Arrays.stream(clazz.getGenericInterfaces()).collect(Collectors.toList()));

        log.info("============================> Class.getDeclaredConstructors() = {}", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));

        log.info("============================> Class.getNestHost()             = {}", clazz.getNestHost());
        log.info("============================> Class.getAnnotations()          = {}", Arrays.stream(clazz.getAnnotations()).collect(Collectors.toList()));
        log.info("============================> Class.getDeclaredAnnotations()  = {}", Arrays.stream(clazz.getDeclaredAnnotations()).collect(Collectors.toList()));
        log.info("============================> Class.getAnnotatedInterfaces()  = {}", Arrays.stream(clazz.getAnnotatedInterfaces()).collect(Collectors.toList()));
        log.info("============================> Class.getAnnotatedSuperclass()  = {}", clazz.getAnnotatedSuperclass());

        log.info("============================> Class.getProtectionDomain()     = {}", clazz.getProtectionDomain());

        log.info("============================> Class.getPackageName()          = {}", clazz.getPackageName());
        log.info("============================> Class.getPackage()              = {}", clazz.getPackage());

        log.info("============================> Class.getNestMembers()          = {}", Arrays.stream(clazz.getNestMembers()).collect(Collectors.toList()));

        log.info("============================> Class.getModule()               = {}", clazz.getModule());

        log.info("============================> Class.getEnclosingConstructor() = {}", clazz.getEnclosingConstructor());
        log.info("============================> Class.getGenericSuperclass()    = {}", clazz.getGenericSuperclass());


        log.info("============================> Class.getComponentType()        = {}", clazz.getComponentType());

        log.info("============================> Class.getTypeName()             = {}", clazz.getTypeName());
        log.info("============================> Class.getTypeParameters()       = {}", Arrays.stream(clazz.getTypeParameters()).collect(Collectors.toList()));
    }
}

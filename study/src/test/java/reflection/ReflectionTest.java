package reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        final Class<Question> clazz = Question.class;

        /*
        getSimpleName: 클래스명만을 구한다.
        getName: 패키지명이 포함된 클래스명을 구한다.
        getCanonicalName: import문에 적히는 클래스명.
        see https://stackoverflow.com/questions/15202997/what-is-the-difference-between-canonical-name-simple-name-and-class-name-in-jav
         */
        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo("Question"),
                () -> assertThat(clazz.getName()).isEqualTo("reflection.Question"),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question")
        );
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("reflection.Question");

        /*
        class.forName은 인자로 `classLoader`를 받을 수도 있다. 주어지지 않았을 때에는 현재 클래스의 classLoader를 사용한다.
        따라서 위 테스트와 같은 결과를 얻을 수 있다.
         */
        assertAll(
                () -> assertThat(clazz.getSimpleName()).isEqualTo("Question"),
                () -> assertThat(clazz.getName()).isEqualTo("reflection.Question"),
                () -> assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question")
        );
    }

    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        // 런타임에 객체의 클래스 정보를 불러올 수 있다.
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();
        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> studentClass = Student.class;
        final Method[] methods = studentClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .toList();

        assertThat(actualMethods).hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] constructors = questionClass.getConstructors();
        final Constructor<?>[] declaredConstructors = questionClass.getDeclaredConstructors();
        log.info("constructors: {}\n", Arrays.toString(constructors));
        log.info("declaredConstructors: {}", Arrays.toString(declaredConstructors));

        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        // questionClass.getDeclaredConstructor[0], [1]로도 가져올 수 있다.
        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(long.class, String.class, String.class,
                String.class, java.util.Date.class, int.class);

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(1L, "gugu", "제목2", "내용2",
                new java.util.Date(), 0);

        assertAll(
                () -> assertThat(firstQuestion.getWriter()).isEqualTo("gugu"),
                () -> assertThat(firstQuestion.getTitle()).isEqualTo("제목1"),
                () -> assertThat(firstQuestion.getContents()).isEqualTo("내용1"),
                () -> assertThat(secondQuestion.getWriter()).isEqualTo("gugu"),
                () -> assertThat(secondQuestion.getTitle()).isEqualTo("제목2"),
                () -> assertThat(secondQuestion.getContents()).isEqualTo("내용2")
        );
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        // getFields는 public field만을 가져온다.
        // If this Class object represents a class or interface with no accessible public fields, then this method returns an array of length 0.
        final Field[] fields = questionClass.getFields();

        assertThat(fields).isEmpty();

        /*
        부모 클래스에 public field가 있으면 .getFields()를 통해 가져올 수 있을까?
        getXXX()와 getDeclaredXXX()의 차이를 확실하게 알고 가자!
        getFields() : Object에서 상속받은 **모든** public field를 가져온다.
        getDeclaredFields() : **해당 클래스에서 선언된 모든** field를 가져온다.
        */
        class ParentClass {
            public int parentField;
            protected int protectedField;
        }

        class ChildClass extends ParentClass {
            public int childField;
            private int hiddenField;
        }
        Field[] childFields = ChildClass.class.getFields();
        Field[] childDeclaredFields = ChildClass.class.getDeclaredFields();
        log.info("childFields: {}", Arrays.toString(childFields));
        log.info("childDeclaredFields: {}", Arrays.toString(childDeclaredFields));
        assertAll(
                () -> assertThat(childFields).hasSize(2),        // parentField, childField
                () -> assertThat(childDeclaredFields).hasSize(2) // childField, hiddenField
        );
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        /*
        The elements in the returned array **are not sorted and are not in any particular order.**
        위와 같이 써있어서 아래처럼 검증해도 되는지는... 모르겠다. contains를 통해 검증하자
        assertThat(fields[0].getName()).isEqualTo("questionId");
        */
        assertAll(
                () -> assertThat(fields).hasSize(6),
                () -> assertThat(fields).extracting(Field::getName).contains("questionId")
        );
    }

    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        // QuestionId가 private-scoped field라서 getFields()로 가져올 수 없다.
        final Field declaredField = questionClass.getDeclaredField("questionId");
        assertAll(
                () -> assertThat(declaredField).isNotNull(),
                () -> assertThatThrownBy(() -> questionClass.getField("questionId"))
                        .isInstanceOf(NoSuchFieldException.class) // 찾을 수가 없어요~
        );
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
        // new Student();로 해도 될 듯?
        final Student student = (Student) studentClass.getConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        // DeclaredField로 가져오더라도, 이를 접근하기 위해서는 `setAccessable`을 설정해야 한다.
        // `getInt(student)`의 내부를 살펴보자. `checkAccess`가 존재한다!
        field.setAccessible(true);
        assertAll(
                () -> assertThat(field.getInt(student)).isZero(),
                () -> assertThat(student.getAge()).isZero()
        );
        field.set(student, 99);

        assertAll(
                () -> assertThat(field.getInt(student)).isEqualTo(99),
                () -> assertThat(student.getAge()).isEqualTo(99)
        );
    }
}

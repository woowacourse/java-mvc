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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");

        /*
         * Class<> 의 getSimpleName은 패키지가 포함되지 않은 클래스 자체의 이름을 가져온다.
         * getName()은 패키지를 포함해 클래스의 바이너리 이름을,
         * getCanonicalName()은 패키지를 포함한 표준 클래스 이름을 가져온다.(Canonical)
         * 이 표준 클래스명은 import 문에서 확인할 수 있는 바로 그 이름이다.
         */
    }

    @Test
    void 배열의_경우_getCanonicalName과_getName의_결과가_다르다() {
        final Class<Question[]> clazz = Question[].class;

        assertThat(clazz.getName()).isEqualTo("[Lreflection.Question;");
        // 클래스의 바이너리 이름
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question[]");
        // 클래스의 표준 이름
        assertThat(clazz.getName()).isNotEqualTo(clazz.getCanonicalName());
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
        final Field[] fields = student.getClass().getDeclaredFields();
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(actualFieldNames).contains("name", "age");
    }

    @Test
    void getFields는_public한_모든_필드를_가져온다() {
        final Object student = new Student() {
            public int subclassDeclaredInt;
        };

        Field[] fields = student.getClass().getFields();
        List<String> names = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(names)
                .contains("subclassDeclaredInt");
    }

    @Test
    void getDeclaredFields는_직접_선언된_모든_필드를_가져온다() {
        final Object student = new Student();
        Field[] declaredFields = student.getClass().getDeclaredFields();
        List<String> names = Arrays.stream(declaredFields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(names)
                .containsExactlyInAnyOrder("name", "age");
    }

    @Test
    void getFields는_자식에서_선언된_필드도_가져온다() {
        final Object student = new Student() {
            private int subclassDeclaredInt;
        };

        Field[] declaredFields = student.getClass().getDeclaredFields();
        List<String> names = Arrays.stream(declaredFields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(names)
                .contains("subclassDeclaredInt");
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
        final Constructor<?>[] constructors = questionClass.getConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void getConstructor는_접근가능한_모든_생성자를_가져온다() {
        final Class<?> questionClass = Student.class;
        final Constructor<?>[] constructors = questionClass.getConstructors();

        assertThat(constructors).hasSize(1);
    }

    @Test
    void getDeclaredConstructor는_직접_선언한_모든_생성자를_가져온다() {
        class TwoDeclaredConstructors {
            private TwoDeclaredConstructors(int ignored) {

            }

            public TwoDeclaredConstructors(int ignored, int andIgnored) {

            }
        }
        final Class<?> questionClass = TwoDeclaredConstructors.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(2);
    }

    @Test
    void getDeclaredConstructor는_기본생성자를_포함한다() {
        class NoArgsConstructor {
        }
        final Class<?> questionClass = NoArgsConstructor.class;
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(constructors).hasSize(1);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getConstructors()[0];
        final Constructor<?> secondConstructor = questionClass.getConstructors()[1];

        final Question firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(0, "gugu", "제목2", "내용2", new Date(), 0);

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
        final Student student = new Student();
        final Field field = studentClass.getDeclaredField("age");

        field.setAccessible(true);

        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);

        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);
    }
}

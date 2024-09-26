package reflection;

import static org.assertj.core.api.Assertions.*;

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

		// getSimpleName() : 단순한 클래스 이름을 반환 (패키지명 제외)
		assertThat(clazz.getSimpleName()).isEqualTo("Question");
		// getName() : 패키지명을 포함한 정규 이름을 반환 (내부 클래스는 $로 구분)
		assertThat(clazz.getName()).isEqualTo("reflection.Question");
		// getCanonicalName() : 정식 이름을 반환 (내부 클래스는 .으로 구분)
		assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
	}

	@Test
	void givenClassName_whenCreatesObject_thenCorrect() throws ClassNotFoundException {
		// 런타임에 문자열로부터 클래스 정보를 동적으로 로드하는 코드
		final Class<?> clazz = Class.forName("reflection.Question");

		assertThat(clazz.getSimpleName()).isEqualTo("Question");
		assertThat(clazz.getName()).isEqualTo("reflection.Question");
		assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
	}

	@Test
	void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
		final Object student = new Student();

		// Student 클래스의 필드 정보를 가져옴
		final Field[] fields = student.getClass().getDeclaredFields();

		// 필드 이름을 저장할 리스트 초기화
		final List<String> actualFieldNames = Arrays.stream(fields)
			.map(Field::getName) // 각 필드의 이름을 가져옴
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
		final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

		assertThat(constructors).hasSize(2);
	}

	@Test
	void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
		final Class<?> questionClass = Question.class;

        // 첫 번째 생성자: (String, String, String)
        final Constructor<?> firstConstructor = questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        // 두 번째 생성자: (long, String, String, String, Date, int)
        final Constructor<?> secondConstructor = questionClass.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

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
		// Question 클래스의 모든 public 필드를 런타임에 동적으로 가져와서 그 수를 검증하려는 코드
		final Class<?> questionClass = Question.class;
		final Field[] fields = questionClass.getFields();

		assertThat(fields).hasSize(0);
	}

	@Test
	void givenClass_whenGetsDeclaredFields_thenCorrect() {
		// Question 클래스의 모든 필드를 런타임에 동적으로 가져와서 그 수와 특정 필드의 이름을 검증하려는 코드
		final Class<?> questionClass = Question.class;
		final Field[] fields = questionClass.getDeclaredFields();

		assertThat(fields).hasSize(6);
		assertThat(fields[0].getName()).isEqualTo("questionId");
	}

	@Test
	void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
		// Question 클래스에서 특정 이름을 가진 필드를 런타임에 동적으로 가져와서 그 이름을 검증하려는 코드
		final Class<?> questionClass = Question.class;
		final Field field = questionClass.getDeclaredField("questionId");

		assertThat(field.getName()).isEqualTo("questionId");
	}

	@Test
	void givenClassField_whenGetsType_thenCorrect() throws Exception {
		//Question 클래스의 questionId 필드의 타입을 런타임에 동적으로 가져와서 그 타입의 이름을 검증하려는 코드
		final Field field = Question.class.getDeclaredField("questionId");
		final Class<?> fieldClass = field.getType();

		assertThat(fieldClass.getSimpleName()).isEqualTo("long");
	}

	@Test
	void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
		// Student 클래스의 특정 필드에 대한 값을 설정하고 가져오는 기능을 검증하려는 코드
		final Class<?> studentClass = Student.class;
		final Student student = new Student();
		final Field field = studentClass.getDeclaredField("age");
		field.setAccessible(true); // private 필드 접근을 허용

		assertThat(field.getInt(student)).isZero();
		assertThat(student.getAge()).isZero();

		field.set(student, 99);

		assertThat(field.getInt(student)).isEqualTo(99);
		assertThat(student.getAge()).isEqualTo(99);
	}
}

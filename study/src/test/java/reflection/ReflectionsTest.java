package reflection;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;
import reflection.examples.JdbcQuestionRepository;
import reflection.examples.JdbcUserRepository;
import reflection.examples.MyQnaService;
import reflection.examples.QnaController;

class ReflectionsTest {

	private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

	@Test
	void showAnnotationClass() throws Exception {
		// Reflections로 reflection.examples 패키지를 탐색
		Reflections reflections = new Reflections("reflection.examples");

		// @Controller 애노테이션이 붙은 클래스 찾기
		Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
		log.info("Found Controllers:");
		for (Class<?> clazz : controllers) {
			log.info(clazz.getSimpleName());
		}

		// @Service 애노테이션이 붙은 클래스 찾기
		Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
		log.info("Found Services:");
		for (Class<?> clazz : services) {
			log.info(clazz.getSimpleName());
		}

		// @Repository 애노테이션이 붙은 클래스 찾기
		Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
		log.info("Found Repositories:");
		for (Class<?> clazz : repositories) {
			log.info(clazz.getSimpleName());
		}

		Assertions.assertAll(
			() -> assertThat(controllers).containsExactly(QnaController.class),
			() -> assertThat(services).containsExactly(MyQnaService.class),
			() -> assertThat(repositories).containsExactly(JdbcQuestionRepository.class, JdbcUserRepository.class)
		);
	}
}

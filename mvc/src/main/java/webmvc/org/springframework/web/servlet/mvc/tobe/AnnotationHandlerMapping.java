package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

	private final Object[] basePackage;
	private final Map<HandlerKey, HandlerExecution> handlerExecutions;

	public AnnotationHandlerMapping(final Object... basePackage) {
		this.basePackage = basePackage;
		this.handlerExecutions = new HashMap<>();
	}

	public void initialize() {
		Reflections reflections = new Reflections(basePackage);
		Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);
		for (final Class<?> controller : controllers) {
			mapController(controller);
		}
		log.info("Initialized AnnotationHandlerMapping!");
	}

	private void mapController(final Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		Object controller = createControllerInstance(clazz);
		for (final Method method : methods) {
			mapControllerMethod(controller, method);
		}
	}

	private Object createControllerInstance(final Class<?> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("controller 인스턴스 생성 실패", e);
		}
	}

	private void mapControllerMethod(final Object controller, final Method method) {
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		if (requestMapping == null) {
			return;
		}
		RequestMethod[] requestMethods = requestMapping.method();
		String url = requestMapping.value();
		for (final RequestMethod requestMethod : requestMethods) {
			addHandlerExecution(controller, method, url, requestMethod);
		}
	}

	private void addHandlerExecution(final Object controller, final Method method, final String url,
		final RequestMethod requestMethod) {
		HandlerKey handlerKey = new HandlerKey(url, requestMethod);
		HandlerExecution execution = new HandlerExecution(controller, method);
		handlerExecutions.put(handlerKey, execution);
	}

	public Object getHandler(final HttpServletRequest request) {
		HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
		return handlerExecutions.get(handlerKey);
	}
}

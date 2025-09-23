package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    private final Properties properties;

    public ManualHandlerMapping(Properties properties) {
        this.properties = properties;
    }

    public void register(String path, Controller controller) {
        controllers.put(path, controller);
        log.info("Registered Controller: {} -> {}", path, controller.getClass().getName());
    }

    @Override
    public void initialize() {
        log.info("ManualHandlerMapping 초기화 시작 - 설정 파일 기반 컨트롤러 매핑");
        this.properties.forEach((key, value) -> {
            String propertyKey = key.toString();
            String propertyValue = value.toString();
            if (propertyKey.startsWith("controller./")) {
                String path = propertyKey.substring("controller.".length());
                registerControllerSafely(path, propertyValue);
            }
        });
        log.info("ManualHandlerMapping 초기화 완료 - 총 {} 개 컨트롤러 등록됨", controllers.size());
    }

    private void registerControllerSafely(String path, String className) {
        try {
            Class<?> clazz = loadControllerClass(className);
            Controller controller = createControllerInstance(clazz);
            register(path, controller);
            log.info(" 컨트롤러 등록 성공: {} → {}", path, className);
        } catch (ClassNotFoundException e) {
            String errorMsg = String.format("컨트롤러 클래스를 찾을 수 없습니다: %s (매핑 경로: %s)", className, path);
            log.error("클래스 없음: {}", errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        } catch (ClassCastException e) {
            String errorMsg = String.format("Controller 인터페이스를 구현하지 않음: %s (매핑 경로: %s)", className, path);
            log.error("타입 오류: {}", errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        } catch (NoSuchMethodException e) {
            String errorMsg = String.format("기본 생성자가 없음: %s (매핑 경로: %s)", className, path);
            log.error("생성자 없음: {}", errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        } catch (InstantiationException e) {
            String errorMsg = String.format("인스턴스화 불가: %s (매핑 경로: %s)", className, path);
            log.error("인스턴스화 실패: {}", errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        } catch (IllegalAccessException e) {
            String errorMsg = String.format("생성자 접근 불가: %s (매핑 경로: %s)", className, path);
            log.error("접근 권한 없음: {}", errorMsg, e);
            throw new IllegalArgumentException(errorMsg, e);
        } catch (Exception e) {
            String errorMsg = String.format("컨트롤러 등록 실패: %s (매핑 경로: %s) - 원인: %s", className, path, e.getClass().getSimpleName());
            log.error("예상치 못한 시스템 오류: {}", errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    private Class<?> loadControllerClass(String className) throws ClassNotFoundException {
        return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
    }

    private Controller createControllerInstance(Class<?> clazz) throws Exception {
        if (!Controller.class.isAssignableFrom(clazz)) {
            throw new ClassCastException(clazz.getName() + " does not implement Controller interface");
        }

        return (Controller) clazz.getDeclaredConstructor().newInstance();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        log.debug("수동 매핑 대상: {}", controllers.keySet());
        return controllers.get(requestURI);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

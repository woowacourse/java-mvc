package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object singlePackage : basePackage) {
            String packageName = (String) singlePackage;
            String packageDirectoryPath = packageNameToPath(packageName);

            File packageDirectory = getPackageDirectoryFile(packageDirectoryPath);
            List<String> classFqcns = getFqcnsInDirectory(packageName, packageDirectory);

            addHandlerExecutions(classFqcns);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private String packageNameToPath(final String packageName) {
        return packageName.replace(".", "/");
    }

    private File getPackageDirectoryFile(final String packageDirectoryPath) {
        URL packageUrl = Thread.currentThread()
                .getContextClassLoader()
                .getResource(packageDirectoryPath);

        if (packageUrl == null) {
            throw new IllegalArgumentException("패키지를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return new File(packageUrl.getFile());
    }

    private List<String> getFqcnsInDirectory(final String packageName, final File packageDirectory) {
        // FQCN: Fully Qualified Class Name
        String[] fqcns = packageDirectory.list();

        if (fqcns == null) {
            throw new IllegalArgumentException("클래스를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return Arrays.stream(fqcns)
                .map(it -> it.replace(".class", ""))
                .map(it -> packageName + "." + it)
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(final List<String> classFqcns) {
        List<Class<?>> controllerClasses = getControllerClasses(classFqcns);

        for (Class<?> clazz : controllerClasses) {
            for (Method method : clazz.getDeclaredMethods()) {
                List<HandlerKey> handlerKeys = getHandlerKeys(method);
                HandlerExecution handlerExecution = getHandlerExecution(clazz, method);

                for (HandlerKey handlerKey : handlerKeys) {
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    private List<Class<?>> getControllerClasses(final List<String> classFqcns) {
        return classFqcns.stream()
                .map(this::getClassByFqcn)
                .filter(it -> it.getAnnotation(Controller.class) != null)
                .collect(Collectors.toList());
    }

    private Class<?> getClassByFqcn(final String fqcn) {
        try {
            return Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("경로에 해당하는 클래스를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }
    }

    private List<HandlerKey> getHandlerKeys(final Method method) {
        List<HandlerKey> handlerKeys = new ArrayList<>();

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] httpMethods = requestMapping.method();

        for (RequestMethod httpMethod : httpMethods) {
            handlerKeys.add(new HandlerKey(url, httpMethod));
        }

        return handlerKeys;
    }

    private HandlerExecution getHandlerExecution(final Class<?> clazz, final Method method) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            Object controller = constructor.newInstance();

            return new HandlerExecution(controller, method);
        } catch (Exception e) {
            throw new IllegalArgumentException();
            // TODO: 적절한 예외로 변경
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            throw new IllegalArgumentException("컨트롤러를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return handlerExecution;
    }
}

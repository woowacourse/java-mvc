# 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드
1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.

## 학습 테스트
1. [Reflection API](study/src/test/java/reflection)
2. [Servlet](study/src/test/java/servlet)

----

## 공부한 내용


### forward와 Redirect
```
requestDispatcher.forward(request, response); // 서버 내부 처리 위임해서 기존 요청으로 응답
response.sendRedirect(jspPath); // 리다이렉트 응답 → 브라우저가 새 요청
```

jsp.render()에서는 리다이렉트 또는 포워딩으로 동작한다.
requestDispatcher.forward()는 새로운 요청 호출이 아니라 기존 요청에서 JSP로 처리를 넘겨 모델 데이터로 HTML응답을 만든다.
response.sendRedirect()는 리다이렉트 응답을 보내서, 브라우저가 새로운 요청을 호출하도록 한다.

### ReflectiveOperationException
getDeclaredConstructor()는 NoSuchMethodException을 던지고, 
newInstance()는 InvocationTargetException, InstantiationException, IllegalAccessException을 던진다. 

모두 ReflectiveOperationException에 묶어서 처리할 수 있다. 

### AnnotationHandlerMapping

1. AnnotationHandlerMapping에서 @Controller 가 붙은 클래스 탐색
2. 찾은 클래스에서 @RequestMapping이 붙은 메서드 탐색
3. RequestMethod(HttpMethod)와 url을 기준으로 HandlerKey를 생성
   - RequestMethod가 지정되지 않았다면 모든 메서드를 허용
4. Controller와 Method로 HandlerExecution 생성
5. key: HandlerKey, value: HandlerExecution으로 된 handlerMapping에 추가

url과 HttpMethod를 기준으로 HandlerExecution을 매핑하여 초기화한다.


-----

1. ControllerScanner
ControllerScanner는 @Controller가 붙은 클래스를 탐색하고 각 클래스의 인스턴스를 생성해 반환한다. 
기존 코드에서 AnnotationHandlerMapping이 수행하던 그 책임이 이번 미션에서 분리되었다. 
AnnotationHandlerMapping에서는 controllerScanner.getControllers()를 해서 컨트롤러를 반환 받고, 
그 컨트롤러의 @RequestMapping 메서드를 찾아서 컨트롤러 인스턴스와 메서드를 HandlerExecution으로 묶어 등록한다.
컨트롤러는 엄밀한 싱글톤은 아니지만 초기화 시 한 번 생성되어 모든 요청에서 재사용되므로, 프레임워크 안에서는 사실상 싱글톤처럼 동작한다.


2. ReflectionUtils.getAllMethods()

기존에는 controllerClass.getDeclaredMethods()를 사용했지만, 이번 미션에서는 Reflections 라이브러리의 ReflectionUtils.getAllMethods()로 변경하였다.

1)controllerClass.getDeclaredMethods()   
해당 클래스에 직접 선언된 메서드만 반환하고(public, protected, private 등 모두 포함), 
부모 클래스와 인터페이스에서 상속받은 메서드는 제외한다.

```java
Method[] methods = controllerClass.getDeclaredMethods();
```

@RequestMapping 여부와 관계없이 모든 메서드를 반환하기 때문에, method.getAnnotation(RequestMapping.class);을 하면 null이 반환될 수 있다.

```java
for (Method method : controllerClass.getDeclaredMethods()) {
    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

    if (requestMapping == null) {
        continue;
    }

    // HandlerExecution 등록
}
```
2)ReflectionUtils.getAllMethods()

해당 클래스에 선언된 메서드, 부모 클래스, 인터페이스의 메서드도 모두 탐색해서 반환한다.
또한, 전달된 조건으로 필터링이 된다.

아래 코드에서는@RequestMapping 메서드만 필터링 되어서 반환된다.

```java
Set<Method> methods = ReflectionUtils.getAllMethods(
        controllerClass,
        ReflectionUtils.withAnnotation(RequestMapping.class)
);
```

반환된 Method에는 @RequestMapping으로 이미 필터링 되었기 때문에,
1)번과 달리  별도의 null 검증이 필요없다. 

```java
for (Method method : methods) {
    RequestMapping requestMapping =
            method.getAnnotation(RequestMapping.class);

    // HandlerExecution 등록
}
```

3. HandlerMapping
HandlerMappingRegistry를 통해서 manualHandlerMapping과 annotationHandlerMapping을 동일하게 조회할 수 있게 되었다.
handlerMappingRegistry.getHandler()를 하면, 등록된 두 방식 중 하나로 하나로 처리된다.

4. HandlerAdapter

이전에는 DispatcherServlet이 컨트롤러가 반환한 뷰 이름을 사용해 직접 JspView를 생성했다. 
또한 항상 빈 모델을 전달해 렌더링했다.
```
final JspView jspView = new JspView(viewName);
jspView.render(Map.of(), request, response);
```

HandlerAdapter 적용 후에는 핸들러 실행 결과를 ModelAndView로 통일한다. 
DispatcherServlet은 ModelAndView가 가진 View와 모델 데이터를 사용해 렌더링한다.
```
mav.getView().render(mav.getModel(), request, response);
```
따라서 DispatcherServlet이 JspView, JsonView처럼 View 인터페이스를 구현한 다양한 View를 동일한 방식으로 처리할 수 있다.
또한, 컨트롤러가 추가한 모델 데이터도 View에 전달할 수 있게 되었다. 

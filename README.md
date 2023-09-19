# @MVC 구현하기


요청을 처리하기 위해 controller를 실행시키는 방법이 2가지이다.
- MannualHandlerMapping => getController()의 반환값 Controller => Controller.execute()은 String ViewName을 반환한다
    이후, 디스패처 서블릿에서, viewName이 redirect:로 시작하는 경우와 .jsp 파일 반환하는 경우로 나뉨
- AnnotationHandlerMapping => getController()의 반환값 HandlerExecution => HandlerExecution.handle()은 ModelAndView를 반환한다.
- [x] Mapper 인터페이스 생성
- [x] HandlerMapping으로 묶기

# @MVC 구현하기


요청을 처리하기 위해 controller를 실행시키는 방법이 2가지이다.
- MannualHandlerMapping => getController()의 반환값 Controller => Controller.execute()은 String ViewName을 반환한다
    이후, 디스패처 서블릿에서, viewName이 redirect:로 시작하는 경우와 .jsp 파일 반환하는 경우로 나뉨
- AnnotationHandlerMapping => getController()의 반환값 HandlerExecution => HandlerExecution.handle()은 ModelAndView를 반환한다.
- [x] Mapper 인터페이스 생성
- [x] HandlerMapping으로 묶기

요청의 반환값이 다른 문제를 ModelAndView 객체를 반환하는 것으로 통일한다.
핸들러마다 어댑터(여러handler의 실행 결과를 통일 시키는 객체)를 둔다.
- [x] Adapter 인터페이스 생성
  - [x] Controller 어댑터 생성
  - [x] HandlerExecution 어댑터 생ㅇ
- [x] HandlerAdapter로 묶기

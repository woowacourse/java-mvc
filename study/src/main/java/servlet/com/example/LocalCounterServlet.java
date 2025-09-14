package servlet.com.example;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "localCounterServlet", urlPatterns = "/local-counter")
public class LocalCounterServlet extends HttpServlet {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().log("init() 호출");
    }

    /**
     * localCounter 같은 로컬 변수는 다른 스레드와 공유되지 않는다.
     * 비즈니스 로직 처리는 로컬 변수를 사용한다.
     */

    /*
    서블릿 인스턴스 변수와 메모리 구조
    서블릿 객체 인스턴스는 JVM 힙(heap) 영역에 생성된다. 힙 영역은 JVM 내에서 여러 스레드가 공유하는 메모리 공간.
    여러 클라이언트 요청을 처리할 때, 서블릿 컨테이너는 단 하나의 서블릿 인스턴스를 생성해 이 힙 영역에 유지하고 여러 스레드가 동시에 이 하나의 객체에 접근한다.
    반면, 각 요청을 처리하는 스레드는 자신만의 스택(stack) 영역을 가지며, 스택에는 로컬 변수, 함수 호출 정보 등이 저장되어 요청마다 독립적이고 격리된 메모리를 사용한다.

    따라서 인스턴스 변수는 힙에 저장되어, 처리하는 모든 스레드가 하나의 같은 서블릿 인스턴스를 참조하므로 인스턴스 변수도 공유된다.
    로컬 변수는 각 스레드의 스택에 저장되어 스레드마다 독립적이며 공유되지 않는다.

    이 구조 때문에 서블릿의 인스턴스 변수는 멀티스레드 환경에서 동시에 여러 요청이 접근하면 **경쟁 상태(race condition)**나 데이터 불일치 문제가 발생할 위험이 있다.
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        getServletContext().log("service() 호출");
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        int localCounter = 0;
        localCounter++;
        response.getWriter().write(String.valueOf(localCounter));
    }

    @Override
    public void destroy() {
        getServletContext().log("destroy() 호출");
    }
}

package servlet.com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/method")
public class HttpMethodServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("service() 메서드 호출됨 - HTTP 메서드: " + request.getMethod());

        // 부모의 service 메서드 호출 (이게 doGet/doPost로 분배해줌)
        super.service(request, response);

        System.out.println("service() 메서드 완료");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("doGet() 메서드 실행");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>GET 요청 처리됨</h1>");
        out.println("<p>이 페이지는 doGet() 메서드가 생성했습니다.</p>");
        out.println("<p>콘솔 로그를 확인해보세요!</p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("doPost() 메서드 실행");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>POST 요청 처리됨</h1>");
        out.println("<p>이 페이지는 doPost() 메서드가 생성했습니다.</p>");
        out.println("<p>콘솔 로그를 확인해보세요!</p>");
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
        log("이것도 된다");
    }
}

package servlet.com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/basic")
public class BasicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println("현재 스레드: " + Thread.currentThread().getName());

        out.println("<html><body>");
        out.println("<h1>🎯 BasicServlet 동작 확인</h1>");
        out.println("<p>현재 시간: " + LocalDateTime.now() + "</p>");
        out.println("<p>요청 URL: " + request.getRequestURL() + "</p>");
        out.println("<p>HTTP 메서드: " + request.getMethod() + "</p>");
        out.println("</body></html>");
    }
}

package servlet.com.example;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "koreanServlet", urlPatterns = "/korean")
public class KoreanServlet extends HttpServlet {

    public static final String 인코딩 = "인코딩";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().log("korean init() 호출");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getServletContext().log("korean service() 호출");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(인코딩);
    }

    @Override
    public void destroy() {
        getServletContext().log("korean destroy() 호출");
    }
}

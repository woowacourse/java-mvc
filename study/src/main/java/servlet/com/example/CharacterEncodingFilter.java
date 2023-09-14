package servlet.com.example;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        //getWriter 호출된 후 또는 응답이 커밋된 후에 이 메서드를 호출하면 아무런 효과가 없습니다.
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        chain.doFilter(request, response);
    }
}

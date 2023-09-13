package servlet.com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

import static org.eclipse.jdt.internal.compiler.util.Util.UTF_8;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        response.setCharacterEncoding(UTF_8);
        chain.doFilter(request, response);
        request.getServletContext().log("doFilter() 종료");
    }
}

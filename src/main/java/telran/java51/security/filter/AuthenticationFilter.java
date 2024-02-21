package telran.java51.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import telran.java51.accounting.dao.AccountRepository;
import telran.java51.accounting.model.User;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Order(20)
public class AuthenticationFilter implements Filter {
    final AccountRepository accountRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            User user;
            try {
                String[] credentials = getCredentials(request.getHeader("Authorization"));

                user = accountRepository.findById(credentials[0]).orElseThrow(RuntimeException::new);
                if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                response.sendError(401);
                return;
            }
            request = new WrappedRequest(request, user.getLogin());
        }
        request.getUserPrincipal();
        filterChain.doFilter(request, response);
    }

    private String[] getCredentials(String header) {
        String token = header.split(" ")[1];
        String decode = new String(Base64.getDecoder().decode(token));
        return decode.split(":");
    }

    private boolean checkEndPoint(String method, String path) {
        return !(
                (HttpMethod.POST.matches(method) && path.matches("/account/register"))
                        || path.matches("/forum/posts/\\w+(/\\w+)?")
        );
    }

    private class WrappedRequest extends HttpServletRequestWrapper {
        private String login;

        public WrappedRequest(HttpServletRequest request, String login) {
            super(request);
            this.login = login;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> login;
        }
    }
}

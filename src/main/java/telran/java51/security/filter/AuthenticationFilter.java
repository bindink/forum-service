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
import telran.java51.security.context.SecurityContext;
import telran.java51.security.model.UserPrincipal;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import static telran.java51.security.filter.utils.FilterUtils.ERROR_401_RESPONSE_TEXT;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {
    final AccountRepository accountRepository;
    final SecurityContext securityContext;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            String sessionId = request.getSession().getId();
            UserPrincipal userPrincipal = securityContext.getUserPrincipalBySessionId(sessionId);

            User user;
            if (userPrincipal == null) {

                try {
                    String[] credentials = getCredentials(request.getHeader("Authorization"));

                    user = accountRepository.findById(credentials[0]).orElseThrow(RuntimeException::new);
                    if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
                        throw new RuntimeException();
                    }
                    Set<String> roles = user.getRoles().stream()
                            .map(Enum::toString)
                            .collect(Collectors.toSet());
                    userPrincipal = new UserPrincipal(user.getLogin(),roles);
                    securityContext.addUserSession(sessionId, userPrincipal);
                } catch (Exception e) {
                    response.sendError(401, ERROR_401_RESPONSE_TEXT);
                    return;
                }
                request = new WrappedRequest(request, userPrincipal.getName(), userPrincipal.getRoles());
            }
            Set<String> roles = userPrincipal.getRoles();
            request = new WrappedRequest(request, userPrincipal.getName(), roles);
        }
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
        private Set<String> roles;

        public WrappedRequest(HttpServletRequest request, String login, Set<String> roles) {
            super(request);
            this.login = login;
            this.roles = roles;
        }

        @Override
        public Principal getUserPrincipal() {
            return new UserPrincipal(login, roles);
        }
    }
}

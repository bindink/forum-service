package telran.java51.security.context;

import org.springframework.stereotype.Component;
import telran.java51.security.model.UserPrincipal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecuritySessionImpl implements SecurityContext {
    private Map<String, UserPrincipal> context = new ConcurrentHashMap<>();
    @Override
    public UserPrincipal addUserSession(String sessionId, UserPrincipal user) {
        return context.put(sessionId, user);
    }

    @Override
    public UserPrincipal removeUserSession(String sessionId) {
        return context.remove(sessionId);
    }

    @Override
    public UserPrincipal getUserPrincipalBySessionId(String sessionId) {
        return context.get(sessionId);
    }
}

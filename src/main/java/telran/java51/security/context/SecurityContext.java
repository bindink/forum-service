package telran.java51.security.context;

import telran.java51.security.model.UserPrincipal;

public interface SecurityContext {
    UserPrincipal addUserSession(String sessionId, UserPrincipal user);
    UserPrincipal removeUserSession(String sessionId);
    UserPrincipal getUserPrincipalBySessionId(String sessionId);
}

package telran.java51.security.filter.utils;

import jakarta.servlet.http.HttpServletRequest;

public class FilterUtils {
    public static final String ERROR_403_RESPONSE_TEXT = "Authorization failed. Permission denied";
    public static final String ERROR_401_RESPONSE_TEXT = "Authentication failed. Check your credentials.";
    public static final String ERROR_POST_NOT_FOUND_RESPONSE_TEXT = "Authorization failed. Post not found by ID";

    public static boolean isRequestPathUserAuthenticated(HttpServletRequest request) {
        String[] arr = request.getServletPath().split("/");
        return request.getUserPrincipal().getName().equals(arr[arr.length - 1]);
    }
}

package telran.java51.security.filter;

import jakarta.servlet.http.HttpServletRequest;

public class FilterUtils {
    static boolean isRequestPathUserAuthenticated(HttpServletRequest request) {
        String[] arr = request.getServletPath().split("/");
        return request.getUserPrincipal().getName().equals(arr[arr.length - 1]);
    }
}

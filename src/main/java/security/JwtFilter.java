package security;

import dto.ApiResponse;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    private static final List<String> openPaths = List.of(
            "auth/login",
            "auth/register"
    );

    private final JwtUtil jwtUtil = new JwtUtil(); // Yangi util obyekt

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        System.out.println("PATH >>> " + path);

        if (openPaths.contains(path)) {
            System.out.println("🔓 OPEN PATH >>> " + path);
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext, "Authorization header required");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            String userId = jwtUtil.getIdFromAccessToken(token);
            requestContext.setProperty("userId", userId);
        } catch (Exception e) {
            abort(requestContext, "Invalid or expired token");
        }
    }

    private void abort(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ApiResponse<>(false, message, 401))
                .build());
    }
}

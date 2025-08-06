package resource;

import dto.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;
import repository.UserDAO;
import security.JwtUtil;

import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDAO = new UserDAO();

    @POST
    @Path("/register")
    public Response register(RegisterDTO request) {
        ApiResponse<?> response = userDAO.createUser(request.getUsername(), request.getPassword());
        return Response.status(response.success() ? 201 : 409).entity(response).build();
    }

    @POST
    @Path("/login")
    public Response login(RegisterDTO request) {
        Optional<User> optionalUser = userDAO.findByUsername(request.getUsername());

        if (optionalUser.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiResponse<>(false, "User not found"))
                    .build();
        }

        User user = optionalUser.get();

        String hashedPassword = userDAO.hashPassword(request.getPassword());
        if (!user.getPassword().equals(hashedPassword)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ApiResponse<>(false, "Incorrect password"))
                    .build();
        }

        String token = JwtUtil.generateAccessToken(user);

        return Response.ok(new ApiResponse<>(true, "Login successful", token)).build();
    }
}

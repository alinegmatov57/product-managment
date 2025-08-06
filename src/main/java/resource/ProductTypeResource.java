package resource;

import dto.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.ProductType;
import repository.ProductTypeDAO;

@Path("/product-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductTypeResource {

    private final ProductTypeDAO dao = new ProductTypeDAO();

    @GET
    public Response getAll() {
        ApiResponse<?> response = dao.getAll();
        return Response.status(response.success() ? 200 : 404).entity(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        ApiResponse<?> response = dao.getById(id);
        return Response.status(response.success() ? 200 : 404).entity(response).build();
    }

    @POST
    public Response create(ProductType dto) {
        ApiResponse<?> response = dao.create(dto);
        return Response.status(response.success() ? 201 : 409).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ProductType dto) {
        ApiResponse<?> response = dao.update(id, dto);
        return Response.status(response.success() ? 200 : 409).entity(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        ApiResponse<?> response = dao.delete(id);
        return Response.status(response.success() ? 200 : 404).entity(response).build();
    }

}

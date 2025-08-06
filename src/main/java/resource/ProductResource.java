package resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Product;
import repository.ProductDAO;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private final ProductDAO dao = new ProductDAO();

    @GET
    public Response getAll(@QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("size") @DefaultValue("5") int size) {
        return Response.ok(dao.findAll(page, size)).build();
    }

    @GET
    @Path("/ping")
    public String ping() {
        return "pong";
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {
        return Response.ok(dao.findById(id)).build();
    }


    @POST
    public Response create(Product product) {
        dao.create(product);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id")Integer id, Product product) {

        return Response.ok(dao.update(id,product)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        return Response.ok(dao.delete(id)).build();
    }
}

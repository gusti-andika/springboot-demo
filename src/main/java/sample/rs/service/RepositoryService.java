package sample.rs.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import sample.rs.model.RepositoryItem;
import sample.rs.model.ResultItemContent;
import sample.rs.model.ResultItemType;

/**
 * Repository public API interface
 * 
 * @author Andika
 *
 */
@Path("/repository")
@Produces(value={MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes(value={MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface RepositoryService {

	@POST
	public Response register(RepositoryItem item);

	@DELETE
	@Path("/{itemName}")
	public Response deregister(@PathParam(value = "itemName") String itemName);

	@GET
	@Path("/{itemName}")
	public ResultItemContent retrieve(@PathParam(value = "itemName") String itemName);

	@GET
	@Path("/{itemName}/type")
	public ResultItemType getType(@PathParam(value = "itemName") String itemName);
}

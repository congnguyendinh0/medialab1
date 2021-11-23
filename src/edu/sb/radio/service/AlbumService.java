package edu.sb.radio.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/albums")  
public class AlbumService {
	@GET
    @Path("/{id}")  
    public Response getAlbum(@PathParam("id") int id) {  
        String output = "This is id: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@GET  
    @Path("/")  
    public Response getAlbumFilter() {  
        String output = "hello world"; 
        return Response.status(200).entity(output).build();  
    }
	
	@POST  
	@Produces("application/json")
    @Path("/")  
    public Response addAlbum(@PathParam("id") int id) {  
        String output = "update negotiations of this person: " + id;  
        return Response.status(200).entity(output).build();  
    }
}

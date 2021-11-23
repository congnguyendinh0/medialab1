package edu.sb.radio.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/negotiations")  
public class NegotiationService {
	@GET  
    @Path("/{id}")  
    public Response getNegotiations(@PathParam("id") int id) {  
        String output = "nego id: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@POST
	@Produces("application/json")
    @Path("/")  
    public Response addNegotiations(@PathParam("id") int id) {  
        String output = "update negotiations: " + id;  
        return Response.status(200).entity(output).build();  
    }
}

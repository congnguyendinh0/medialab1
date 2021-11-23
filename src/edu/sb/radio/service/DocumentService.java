package edu.sb.radio.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/documents")  
public class DocumentService {	
	@GET  
    @Path("/{id}")  
    public Response getDocument(@PathParam("id") int id) {  
        String output = "documents id: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@POST
    @Path("/")  
    public Response addDocument(@PathParam("id") int id) {  
        String output = "update docu: " + id;  
        return Response.status(200).entity(output).build();  
    }
}

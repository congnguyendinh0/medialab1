package edu.sb.radio.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/people")  
public class PersonService {
	@GET
    @Path("/")  
    public Response getPersonFilter() {  
        String output = "get all ppl";
        return Response.status(200).entity(output).build();  
    }
	
	@POST
	@Produces("application/json")
	@Path("/")  
    public Response addPerson() {  
        String output = "add person";
        return Response.status(200).entity(output).build();  
    }
	
	@GET  
    @Path("/{id}")  
    public Response getPerson(@PathParam("id") int id) {  
        String output = "This person had id: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@GET  
    @Path("/{id}/tracks")  
    public Response getPersonTrack(@PathParam("id") int id) {  
        String output = "track of this person: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@GET  
    @Path("/{id}/negotiations")  
    public Response getPersonNegotiations(@PathParam("id") int id) {  
        String output = "negotiations of this person: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@POST  
	@Produces("application/x-www-form-urlencoded")
    @Path("/{id}/negotiations")  
    public Response updatePersonNegotiations(@PathParam("id") int id) {  
        String output = "update negotiations of this person: " + id;  
        return Response.status(200).entity(output).build();  
    }
}

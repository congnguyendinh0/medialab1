package edu.sb.radio.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/tracks")  
public class TrackService {
	@GET
    @Path("/{id}")  
    public Response getTrack(@PathParam("id") int id) {  
        String output = "This is id: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@POST  
	@Produces("application/json")
    @Path("/")  
    public Response addTrack(@PathParam("id") int id) {  
        String output = "update: " + id;  
        return Response.status(200).entity(output).build();  
    }
	
	@GET  
    @Path("/genres")  
    public Response getTrackGenres() {  
        String output = "hello world"; 
        return Response.status(200).entity(output).build();  
    }
	
	@GET  
    @Path("/artists")  
    public Response getTrackArtists() {  
        String output = "hello world"; 
        return Response.status(200).entity(output).build();  
    }
}

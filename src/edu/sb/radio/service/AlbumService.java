package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;

import edu.sb.radio.persistence.Album;
import edu.sb.radio.persistence.Document;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/albums")  
public class AlbumService {
	EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
	
	@GET
    @Path("/{id}")  
    public Album getAlbum(@PathParam("id") @Positive long id) {  
		Album album = entityManager.find(Album.class, id);
		if (album == null) throw new ClientErrorException(NOT_FOUND);
        return album; 
    }
	
	@GET  
    @Path("/")  
    public List<Album> getAlbumFilter(
    		@FormParam("title") String title,
    		@FormParam("releaseYear") Integer releaseYear,
    		@FormParam("trackCount") Integer trackCount
    		) {  
		List<Album> albums = entityManager
				.createQuery("select a from Album as a where a.title like '%(:title)%' "
						+ "and a.releaseYear like '%(:releaseYear)%' "
						+ "and a.trackCount like '%(:trackCount)%' ")
				.setParameter("title", title)
				.setParameter("releaseYear", releaseYear)
				.setParameter("trackCount", trackCount)
		        .getResultList();
        return albums;  
    }
	
	@POST
	@Consumes("application/json")
	@Produces("text/plain")
    @Path("/")  
    public Album addAlbum(
    		ContainerRequestContext requestContext,
    		@FormParam("id") long id,
    		@FormParam("title") String title,
    		@FormParam("releaseYear") Integer releaseYear,
    		@FormParam("trackCount") Integer trackCount,
    		@FormParam("coverReference") long coverReference
    		) {
		// TODO 
		// Check if user is admin
		
		Album album = entityManager.find(Album.class, id);
		
		if (album == null) {
			Document cover = entityManager.find(Document.class, coverReference);
			if(cover == null) cover = new Document("new doc".getBytes());	
			album = new Album(cover);
		} else {
			album.setTitle(title);
			album.setReleaseYear(releaseYear);
			album.setTrackCount(trackCount);
		}
		
        return album;  
    }
}

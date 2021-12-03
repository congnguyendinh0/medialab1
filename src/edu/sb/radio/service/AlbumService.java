package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.Comparator;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.sb.radio.persistence.Album;
import edu.sb.radio.persistence.Document;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Person.Group;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/albums")  
public class AlbumService {
	static private final String QUERY_ALBUM = "select a from Album as a where "
			+ "a.title = :title "
			+ "and (:minReleaseYear is null or a.releaseYear >= :minReleaseYear) "
			+ "and (:maxReleaseYear is null or a.releaseYear <= :maxReleaseYear) "
			+ "and (:minTrackCount is null or a.trackCount >= :minTrackCount) "
			+ "and (:maxTrackCount is null or a.trackCount <= :maxTrackCount)";
	
	static private final Comparator<Album> ALBUM_COMPARATOR = Comparator
			.comparing(Album::getTitle)
			.thenComparing(Album::getReleaseYear)
			.thenComparing(Album::getTrackCount);
	
	@GET
    @Path("/{id}")  
    public Album getAlbum(@PathParam("id") @Positive long id) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Album album = entityManager.find(Album.class, id);
		if (album == null) throw new ClientErrorException(NOT_FOUND);
        return album; 
    }
	
	@GET  
    @Path("/")  
    public Album[] queryAlbums(
    		@QueryParam("resultOffset") @PositiveOrZero Integer resultOffset,
    		@QueryParam("resultLimit") @PositiveOrZero Integer resultLimit,
    		@QueryParam("title") String title,
    		@QueryParam("minReleaseYear") short minReleaseYear,
    		@QueryParam("maxReleaseYear") short maxReleaseYear,		
    		@QueryParam("minTrackCount") byte minTrackCount,
    		@QueryParam("maxTrackCount") byte maxTrackCount
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		final TypedQuery<Long> query = entityManager.createQuery(QUERY_ALBUM, Long.class);
		if(resultOffset != null) query.setFirstResult(resultOffset);
		if(resultLimit != null) query.setMaxResults(resultLimit);
		
		final Album[] albums = query
				.setParameter("title", title)
				.setParameter("minReleaseYear", minReleaseYear)
				.setParameter("maxReleaseYear", maxReleaseYear)
				.setParameter("minTrackCount", minTrackCount)
				.setParameter("maxTrackCount", maxTrackCount)
		        .getResultList()
		        .stream()
		        .map(identity -> entityManager.find(Album.class, identity))
		        .filter(album -> album != null)
		        .sorted(ALBUM_COMPARATOR)
		        .toArray(Album[]::new);
		
        return albums;  
    }
	
	@POST
	@Path("/")  
	@Consumes("application/json")
	@Produces("text/plain")
    public Album addAlbum(
    		@HeaderParam(BasicAuthenticationFilter.REQUESTER_IDENTITY) @Positive long requestedIdentity,
    		@QueryParam("id") long id,
    		@QueryParam("title") String title,
    		@QueryParam("releaseYear") short releaseYear,
    		@QueryParam("trackCount") byte trackCount,
    		@QueryParam("coverReference") long coverReference
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		
		// Check if user is admin
		Person user = entityManager.find(Person.class, requestedIdentity);		
		if (user == null) throw new ClientErrorException(NOT_FOUND);
		if (user.getGroup() != Group.ADMIN) throw new ClientErrorException(401);
		
		Album album = entityManager.find(Album.class, id);
		
		if (album == null) {
			album = new Album();
		}
		
		album.setTitle(title);
		album.setReleaseYear(releaseYear);
		album.setTrackCount(trackCount);
		
		Document cover = entityManager.find(Document.class, coverReference);
		if(cover != null) album.setCover(cover);
		
        return album;  
    }
}

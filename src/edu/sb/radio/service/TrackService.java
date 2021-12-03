package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;

import edu.sb.radio.persistence.Album;
import edu.sb.radio.persistence.Document;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Track;
import edu.sb.radio.persistence.Person.Group;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/tracks")
public class TrackService {
	static private final String QUERY_TRACKS = "select t.identity from Track as t where "; 
	static private final String QUERY_GENRES = "select t.genre from Track as t group by t.genre";
	static private final String QUERY_ARTISTS = "select t.artist from Track as t group by t.artist";
	
	static private final Comparator<Track> TRACK_COMPARATOR = Comparator
			.comparing(Track::getGenre)
			.thenComparing(Track::getOrdinal)
			.thenComparing(Track::getName)
			.thenComparing(Track::getArtist);

	@GET
	@Path("/{id}")
	public Track getTrack(@PathParam("id") @Positive long id) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Track track = entityManager.find(Track.class, id);
		if (track == null) throw new ClientErrorException(NOT_FOUND);
		return track;
	}
	
	@GET
	@Path("/")
	public Track[] queryTracks(
			@QueryParam("resultOffset") @PositiveOrZero Integer resultOffset,
    		@QueryParam("resultLimit") @PositiveOrZero Integer resultLimit,
    		@QueryParam("name") String name,
    		@QueryParam("artist") String artist, 
    		@QueryParam("genre") String genre,
    		@QueryParam("ordinal") byte ordinal,
    		@QueryParam("recordingReference") long recordingReference,
    		@QueryParam("albumReference") long albumReference,
    		@QueryParam("ownerReference") long ownerReference
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		
		final TypedQuery<Long> query = entityManager.createQuery(QUERY_TRACKS, Long.class);
		if(resultOffset != null) query.setFirstResult(resultOffset);
		if(resultLimit != null) query.setMaxResults(resultLimit);
		
		final Track[] tracks = query
				.setParameter("name", name)
				.setParameter("artist", artist)
				.setParameter("genre", genre)
				.setParameter("ordinal", ordinal)
		        .getResultList()
		        .stream()
		        .map(identity -> entityManager.find(Track.class, identity))
		        .filter(track -> track != null)
		        .sorted(TRACK_COMPARATOR)
		        .toArray(Track[]::new);
		
		return tracks;
	}

	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("text/plain")
	public long addTrack(
			@HeaderParam(BasicAuthenticationFilter.REQUESTER_IDENTITY) @Positive long requestedIdentity,
    		@QueryParam("id") long id,
    		@QueryParam("artist") String artist, 
    		@QueryParam("genre") String genre,
    		@QueryParam("name") String name,
    		@QueryParam("ordinal") byte ordinal,
    		@QueryParam("recordingReference") long recordingReference,
    		@QueryParam("albumReference") long albumReference,
    		@QueryParam("ownerReference") long ownerReference
			) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		
		// Check if user is admin
		Person user = entityManager.find(Person.class, requestedIdentity);
		if (user == null) throw new ClientErrorException(NOT_FOUND);
		else if (user.getGroup() != Group.ADMIN) throw new ClientErrorException(401);
		
		Track track = entityManager.find(Track.class, id);
		if (track == null) {
			track = new Track(new Person(), new Album(), new Document("".getBytes()));
		}
		
		track.setArtist(artist);
		track.setGenre(genre);
		track.setName(name);
		track.setOrdinal(ordinal);
		
		Document recording = entityManager.find(Document.class, recordingReference);
		Album album = entityManager.find(Album.class, albumReference);
		Person owner = entityManager.find(Person.class, ownerReference);
		if (recording != null) {
			// TODO
			// How to set recording, album, owner with protected method?
		}

		return track.getIdentity();
	}

	@GET
	@Path("/genres")
	@Produces("application/json")
	public String[] getGenres() {
		// TODO: there might be another way to group by?
		// .collect(Collector.groupingBy(Track::getGenre))
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		final String[] genres = entityManager.createQuery(QUERY_GENRES, String.class)
		        .getResultList()
		        .stream()
		        .filter(genre -> genre != "")
		        .sorted()
		        .toArray(String[]::new);				
		
		return genres;
	}

	@GET
	@Path("/artists")
	@Produces("application/json")
	public String[] getArtists() {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		final String[] artists = entityManager.createQuery(QUERY_ARTISTS, String.class)
		        .getResultList()
		        .stream()
		        .filter(artist -> artist != "")
		        .sorted()
		        .toArray(String[]::new);
		
		return artists;
	}
}

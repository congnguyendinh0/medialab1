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
import javax.ws.rs.core.Response;

import edu.sb.radio.persistence.Album;
import edu.sb.radio.persistence.Document;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Track;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/tracks")
public class TrackService {
	EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");

	@GET
	@Path("/{id}")
	public Track getTrack(@PathParam("id") @Positive long id) {
		Track track = entityManager.find(Track.class, id);
		if (track == null)
			throw new ClientErrorException(NOT_FOUND);
		return track;
	}

	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	@Path("/")
	public Track addTrack(
			ContainerRequestContext requestContext, 
			@FormParam("id") long id,
			@FormParam("artist") String artist, 
			@FormParam("genre") String genre,
			@FormParam("name") String name,
			@FormParam("ordinal") Integer ordinal,
			@FormParam("recordingReference") long recordingReference,
			@FormParam("albumReference") long albumReference,
			@FormParam("ownerReference") long ownerReference
			) {
		// TODO
		// Check if user is admin

		Track track = entityManager.find(Track.class, id);

		if (track == null) {
			Document recording = entityManager.find(Document.class, recordingReference);
			Album album = entityManager.find(Album.class, albumReference);
			Person owner = entityManager.find(Person.class, ownerReference);
			track = new Track(owner, album, recording);
		} else {
			track.setArtist(artist);
			track.setGenre(genre);
			track.setName(name);
			track.setOrdinal(ordinal);
		}

		return track;
	}

	@GET
	@Produces("application/json")
	@Path("/genres")
	public List<String> getTrackGenres() {
		List<String> genres = entityManager.createQuery("select genre from Track t group by t.genre order by t.genre")
				.getResultList();
		return genres;
	}

	@GET
	@Produces("application/json")
	@Path("/artists")
	public List<String> getTrackArtists() {
		List<String> artists = entityManager
				.createQuery("select artist from Track t group by t.artist order by t.artist").getResultList();
		return artists;
	}
}

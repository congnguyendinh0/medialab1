package edu.sb.radio.service;

import static edu.sb.radio.service.BasicAuthenticationFilter.REQUESTER_IDENTITY;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import edu.sb.radio.persistence.BaseEntity;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Person.Group;
import edu.sb.radio.util.Copyright;
import edu.sb.radio.util.RestJpaLifecycleProvider;


/**
 * JAX-RS based REST service implementation for polymorphic entity resources, defining the following path and method combinations:
 * <ul>
 * <li><b>GET entities/{id}</b>: Returns the entity matching the given identity.</li>
 * <li><b>DELETE entities/{id}</b>: Deletes the entity matching the given identity.</li>
 * </ul>
 */
@Path("entities")
@Copyright(year = 2018, holders = "Sascha Baumeister")
public class EntityService {

	/**
	 * Returns the entities corresponding to the given identities, ignoring null IDs and
	 * non-existent entities, in natural order.
	 * @param identities the entity identities
	 * @return the corresponding entities (HTTP 200)
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public BaseEntity[] findEntities (
		@QueryParam("id") @NotNull final Set<Long> identities
	) {
		final EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");

		return identities
			.stream()
			.filter(identity -> identity != null)
			.map(identity -> entityManager.find(BaseEntity.class, identity))
			.filter(entity -> entity != null)
			.sorted()
			.toArray(BaseEntity[]::new);
	}


	/**
	 * Returns the entity with the given identity.
	 * @param entityIdentity the entity identity
	 * @return the matching entity (HTTP 200)
	 * @throws ClientErrorException (HTTP 404) if the given entity cannot be found
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@GET
	@Path("{id}")
	@Produces({ APPLICATION_JSON, APPLICATION_XML })
	public BaseEntity findEntity (
		@PathParam("id") @Positive final long entityIdentity
	) {
		final EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		final BaseEntity entity = entityManager.find(BaseEntity.class, entityIdentity);
		if (entity == null) throw new ClientErrorException(NOT_FOUND);

		return entity;
	}


	/**
	 * Deletes the entity matching the given identity, or does nothing if no such entity exists.
	 * @param requesterIdentity the authenticated requester identity
	 * @param entityIdentity the entity identity
	 * @return void (HTTP 204)
	 * @throws ClientErrorException (HTTP 403) if the given requester is not an administrator
	 * @throws ClientErrorException (HTTP 404) if the given entity cannot be found
	 * @throws ClientErrorException (HTTP 409) if there is a database constraint violation (like conflicting locks)
	 * @throws PersistenceException (HTTP 500) if there is a problem with the persistence layer
	 * @throws IllegalStateException (HTTP 500) if the entity manager associated with the current thread is not open
	 */
	@DELETE
	@Path("{id}")
	public void removeEntity (
		@HeaderParam(REQUESTER_IDENTITY) @Positive final long requesterIdentity,
		@PathParam("id") @Positive final long entityIdentity
	) {
		final EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		final Person requester = entityManager.find(Person.class, requesterIdentity);
		if (requester == null || requester.getGroup() != Group.ADMIN) throw new ClientErrorException(FORBIDDEN);

		final BaseEntity entity = entityManager.find(BaseEntity.class, entityIdentity);
		if (entity == null) throw new ClientErrorException(NOT_FOUND);
		entityManager.remove(entity);

		try {
			entityManager.getTransaction().commit();
		} catch (final RollbackException exception) {
			throw new ClientErrorException(CONFLICT);
		} finally {
			entityManager.getTransaction().begin();
		}

		entityManager.getEntityManagerFactory().getCache().evict(BaseEntity.class, entityIdentity);
	}
}
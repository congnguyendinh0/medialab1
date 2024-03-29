package edu.sb.radio.service;

import static javax.ws.rs.core.HttpHeaders.WWW_AUTHENTICATE;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.sb.radio.persistence.Person;
import edu.sb.radio.util.Copyright;
import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.HttpCredentials.Basic;
import edu.sb.radio.util.RestCredentials;
import edu.sb.radio.util.RestJpaLifecycleProvider;


/**
 * JAX-RS filter provider that performs HTTP "basic" authentication on any REST service request. This aspect-oriented
 * design swaps "Authorization" headers for "Requester-Identity" during authentication.
 */
// TODO: remove comments
// @Provider
// @Priority(Priorities.AUTHENTICATION)
@Copyright(year = 2017, holders = "Sascha Baumeister")
public class BasicAuthenticationFilter implements ContainerRequestFilter {

	/**
	 * HTTP request header for the authenticated requester's identity.
	 */
	static public final String REQUESTER_IDENTITY = "X-Requester-Identity";


	/**
	 * Performs HTTP "basic" authentication by calculating a password hash from the password contained in the request's
	 * "Authorization" header, and comparing it to the one stored in the person matching said header's username. The
	 * "Authorization" header is consumed in any case, and upon success replaced by a new "Requester-Identity" header that
	 * contains the authenticated person's identity. The filter chain is aborted in case of a problem.
	 * @param requestContext {@inheritDoc}
	 * @throws NullPointerException if the given argument is {@code null}
	 * @throws ClientErrorException (400) if the "Authorization" header is malformed, or if there is a pre-existing
	 *         "Requester-Identity" header
	 */
	public void filter (final ContainerRequestContext requestContext) throws NullPointerException, ClientErrorException {
		// TODO:
		// - Throw a ClientErrorException(Status.BAD_REQUEST) if the given context's headers map already contains a
		//   "Requester-Identity" key, in order to prevent spoofing attacks.
		// - Remove the "Authorization" header from said map and store the first of it's values in a variable
		//   "textCredentials", or null if the header value is either null or empty.
		// - if the "textCredentials" variable is not null, parse it either programmatically, or using
		//   RestCredentials.newBasicInstance(textCredentials).
		// - Perform the PQL-Query "select p from Person as p where p.email = :email"), using the name of
		//   the parsed credentials as email address. Note that this query will go to the second level cache
		//   before hitting the database if the Person#email field is annotated using @CacheIndex(updateable = true)! 
		// - if the resulting people list contains exactly one element, calculate the hex-string representation
		//   (i.e. 2 digits per byte) of the SHA-256 hash code of the credential's password either programmatically,
		//   or using HashCodes.sha2HashText(256, text).
		// - if this hash representation is equal to queried person's password hash, add a new "Requester-Identity"
		//   header to the request headers, using the person's identity (converted to String) as value, and return
		//   from this method.
		// - in all other cases, abort the request using requestContext.abortWith() in order to challenging the client
		//   to provide HTTP Basic credentials (i.e. status code 401, and "WWW-Authenticate" header value "Basic").
		//   Note that the alternative of throwing NotAuthorizedException("Basic") comes with the disadvantage that
		//   failed authentication attempts clutter the server log with stack traces.
		if(requestContext.getHeaderString(REQUESTER_IDENTITY) != null) throw new ClientErrorException(Status.BAD_REQUEST);
		
		final List<String> authenticationHeaders = requestContext.getHeaders().remove("Authorization");
		final String textCredentials = authenticationHeaders == null || authenticationHeaders.isEmpty() ? null : authenticationHeaders.get(0);
		
		if (textCredentials != null) {
			final Basic credentials = RestCredentials.newBasicInstance(textCredentials);
			final EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
			final List<Person> people = entityManager
					.createQuery("select p from Person as p where p.email = :email", Person.class)
					.setParameter("email", credentials.getName())
			        .getResultList();
			if(people.size() == 1) {
				final String passwordHash = HashCodes.sha2HashText(256, credentials.getPassword());
				
				if(passwordHash == people.get(0).getPasswordHash()) {
					requestContext.getHeaders().add(REQUESTER_IDENTITY, String.valueOf(people.get(0).getIdentity()));
					return;
				}
			} 
		}
		
		requestContext.abortWith(Response.status(UNAUTHORIZED).header(WWW_AUTHENTICATE, "Basic").build());
	}
}
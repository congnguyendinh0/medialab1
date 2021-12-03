package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

import edu.sb.radio.persistence.Address;
import edu.sb.radio.persistence.Document;
import edu.sb.radio.persistence.Name;
import edu.sb.radio.persistence.Negotiation;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Person.Group;
import edu.sb.radio.persistence.Track;
import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/people")  
public class PersonService {
	static private final String QUERY_PEOPLE = "select p.identity from Person as p where "
			+ "p.email = :email "
			+ "and p.groupAlias = :group "
			+ "and p.title = :title "
			+ "and p.family = :family "
			+ "and p.given = :given "
			+ "and p.street = :street "
			+ "and p.postcode = :postcode "
			+ "and p.city = :city "
			+ "and p.country like :country";
	static public final String SET_PASSWORD = "Set-Password";
	
	static private final Comparator<Person> PEOPLE_COMPARATOR = Comparator
			.comparing(Person::getName)
			.thenComparing(Person::getEmail);
	@GET
    @Path("/")  
    public Person[] queryPeople(
    		@QueryParam("resultOffset") @PositiveOrZero Integer resultOffset,
    		@QueryParam("resultLimit") @PositiveOrZero Integer resultLimit,
    		@QueryParam("email") String email,
    		@QueryParam("group") String group,
    		@QueryParam("title") String title,
    		@QueryParam("family") String family,
    		@QueryParam("given") String given,
    		@QueryParam("street") String street,
    		@QueryParam("postcode") String postcode,
    		@QueryParam("city") String city,
    		@QueryParam("country") String country,
    		@QueryParam("offer") boolean offer,
    		@QueryParam("answer") boolean answer
    ) { 
		// TODO:
		// Specifically, make sure there are parameters suitable for filtering the negotiation properties, 
		// including two boolean values to filter for null offers and answers.
		// => offer cannot be null though?
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		final TypedQuery<Long> query = entityManager.createQuery(QUERY_PEOPLE, Long.class);
		if(resultOffset != null) query.setFirstResult(resultOffset);
		if(resultLimit != null) query.setMaxResults(resultLimit);
		
		// TODO
		// Missing parameters identifying omitted criteria => How? A bunch of if-else?
		final Person[] persons = query
				.setParameter("email", email)
				.setParameter("group", group)
				.setParameter("title", title)
				.setParameter("family", family)
				.setParameter("given", given)
				.setParameter("street", street)
				.setParameter("postcode", postcode)
				.setParameter("city", city)
				.setParameter("country", country)
		        .getResultList()
		        .stream()
		        .map(identity -> entityManager.find(Person.class, identity))
		        .filter(person -> person != null)
		        .sorted(PEOPLE_COMPARATOR)
		        .toArray(Person[]::new);
        return persons; 
    }
	
	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("text/plain")
    public Person addPerson(
    		@HeaderParam(BasicAuthenticationFilter.REQUESTER_IDENTITY) @Positive long requestedIdentity,
    		@HeaderParam(SET_PASSWORD) String password,
    		@QueryParam("id") long id,
    		@QueryParam("email") String email,
    		@QueryParam("group") String group,
    		@QueryParam("title") String title,
    		@QueryParam("family") String family,
    		@QueryParam("given") String given,
    		@QueryParam("street") String street,
    		@QueryParam("postcode") String postcode,
    		@QueryParam("city") String city,
    		@QueryParam("country") String country,
    		@QueryParam("avatarReference") long avatarReference
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		
		// Check if user is admin or themselves
		Person user = entityManager.find(Person.class, requestedIdentity);
		Person person = entityManager.find(Person.class, id);
		
		if (user == null) throw new ClientErrorException(NOT_FOUND);
		else if (user.getGroup() != Group.ADMIN || requestedIdentity != id) throw new ClientErrorException(401);
		
		// Check so non-admin cant change group to admin
		if (user.getGroup() != Group.ADMIN && Group.valueOf(group) == Group.ADMIN) throw new ClientErrorException(401);
		
		if (person == null) {
			person = new Person();
		}
		
		Address address = new Address();
		address.setCity(city);
		address.setCountry(country);
		address.setPostcode(postcode);
		address.setStreet(street);
		
		Name name = new Name();
		name.setFamily(family);
		name.setGiven(given);
		name.setTitle(title);
		
		// TODO: How to set Name and Address when set method is protected?
		// person.setAddress(address);
		// person.setName(name);
		person.setEmail(email);
		person.setGroup(Group.valueOf(group));
		
		// TODO: a new password may be set using the header field “Set-Password”
		if (password != null) person.setPasswordHash(HashCodes.sha2HashText(256, password));
		
		// New avatar reference may be passed using the query parameter "avatarReference"
		Document avatar = entityManager.find(Document.class, avatarReference);
		if (avatar != null) {
			person.setAvatar(avatar);
		}
		
        return person;  
    }
	
	@GET  
    @Path("/{id}")  
	public Person getPerson(
			@PathParam("id") @Positive long id,
			@HeaderParam(BasicAuthenticationFilter.REQUESTER_IDENTITY) @Positive long requestedIdentity
			) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Person person = entityManager.find(Person.class, id);
		if (person == null) person = entityManager.find(Person.class, requestedIdentity);
		if (person == null) throw new ClientErrorException(NOT_FOUND);
        return person; 
    }
	
	@GET  
    @Path("/{id}/tracks")  
    public Set<Track> getPersonTrack(@PathParam("id") long id) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Person person = entityManager.find(Person.class, id);
		Set<Track> tracks = person.getTracks(); 
        return tracks;
    }
	
	@GET  
    @Path("/{id}/negotiations")  
    public Set<Negotiation> getPersonNegotiations(@PathParam("id") long id) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Person person = entityManager.find(Person.class, id);
		Set<Negotiation> negotiations = person.getNegotiations(); 
        return negotiations;
    }
	
	@POST
    @Path("/{id}/negotiations")
	@Consumes("application/x-www-form-urlencoded")
    public void updatePersonNegotiations(
    		@PathParam("id") long id,
    		@HeaderParam(BasicAuthenticationFilter.REQUESTER_IDENTITY) @Positive long requestedIdentity,
    		@QueryParam("x") final List<Long> personReferences
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		
		// Check if user is admin or themselves
		Person user = entityManager.find(Person.class, requestedIdentity);
		Person person = entityManager.find(Person.class, id);
		
		if (user == null) throw new ClientErrorException(NOT_FOUND);
		else if (user.getGroup() != Group.ADMIN || requestedIdentity != id) throw new ClientErrorException(401);
		
		// Changes the person’s associated negotiations to the set of person references
		Person[] persons = (Person[]) personReferences
		.stream()
		.map(identity -> entityManager.find(Person.class, identity))
		.toArray();
		
		// TODO: how to set negotiation to person references? (setNegotiation is protected)
    }
}

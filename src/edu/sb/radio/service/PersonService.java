package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;
import java.util.Set;

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

import edu.sb.radio.persistence.Address;
import edu.sb.radio.persistence.Name;
import edu.sb.radio.persistence.Negotiation;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Track;
import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/people")  
public class PersonService {
	EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
	
	@GET
    @Path("/")  
    public List<Person> getPersonFilter(
    		@FormParam("email") String email,
    		@FormParam("group") String group,
    		@FormParam("title") String title,
    		@FormParam("family") String family,
    		@FormParam("given") String given,
    		@FormParam("street") String street,
    		@FormParam("postcode") String postcode,
    		@FormParam("city") String city,
    		@FormParam("country") String country,
    		@FormParam("resultOffset") int resultOffset,
    		@FormParam("resultLimit") int resultLimit    		
    		) { 
		// TODO: how to filter in other table? Left join?
		List<Person> persons = entityManager
				.createQuery(
						"select p from Person as p where p.email like '%(:email)%' "
						+ "and p.groupAlias = (:group) "
						+ "and p.title like '%(:title)%' "
						+ "and p.family like '%(:family)%' "
						+ "and p.given like '%(:given)%' "
						+ "and p.street like '%(:street)%' "
						+ "and p.postcode like '%(:postcode)%' "
						+ "and p.city like '%(:city)%' "
						+ "and p.country like '%(:country)%' "
						+ "order by family, given, email "
						+ "limit (:resultLimit) offset (:resultOffset)"
						)
				.setParameter("email", email)
				.setParameter("group", group)
				.setParameter("title", title)
				.setParameter("family", family)
				.setParameter("given", given)
				.setParameter("street", street)
				.setParameter("postcode", postcode)
				.setParameter("city", city)
				.setParameter("country", country)
				.setParameter("resultLimit", resultLimit)
				.setParameter("resultOffset", resultOffset)
		        .getResultList();
        return persons; 
    }
	
	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("text/plain")
    public Person addPerson(
    		ContainerRequestContext requestContext,
    		@FormParam("id") long id,
    		@FormParam("email") String email,
    		@FormParam("group") String group,
    		@FormParam("title") String title,
    		@FormParam("family") String family,
    		@FormParam("given") String given,
    		@FormParam("street") String street,
    		@FormParam("postcode") String postcode,
    		@FormParam("city") String city,
    		@FormParam("country") String country,
    		@FormParam("password") String password,
    		@FormParam("avatarReference") long avatarReference
    		) {  
		// TODO 
		// Check if user is admin or themselves
		// Check so non-admin cant change group to admin
		
		Person person = entityManager.find(Person.class, id);
		
		if (person == null) {
			person = new Person();
		} else {
			Address address = new Address();
			address.setCity(city);
			address.setCountry(country);
			address.setPostcode(postcode);
			address.setStreet(street);
			
			Name name = new Name();
			name.setFamily(family);
			name.setGiven(given);
			name.setTitle(title);
			
			person.setAddress(address);
			person.setEmail(email);
			// person.setGroup(group);
			person.setName(name);
			
			// TODO: a new password may be set using the header field “Set-Password”
			// new avatar reference may be passed using the query parameter "avatarReference"
			person.setPasswordHash(HashCodes.sha2HashText(256, password));
		}
		
        return person;  
    }
	
	@GET  
    @Path("/{id}")  
	public Person getPerson(@PathParam("id") @Positive long id) {  
		Person person = entityManager.find(Person.class, id);
		if (person == null) throw new ClientErrorException(NOT_FOUND);
        return person; 
    }
	
	@GET  
    @Path("/{id}/tracks")  
    public Set<Track> getPersonTrack(@PathParam("id") long id) {
		Person person = getPerson(id);
		Set<Track> tracks = person.getTracks(); 
        return tracks;
    }
	
	@GET  
    @Path("/{id}/negotiations")  
    public Set<Negotiation> getPersonNegotiations(@PathParam("id") long id) {  
		Person person = getPerson(id);
		Set<Negotiation> negotiations = person.getNegotiations(); 
        return negotiations;
    }
	
	@POST
    @Path("/{id}/negotiations")
	@Produces("application/x-www-form-urlencoded")
    public Set<Negotiation> updatePersonNegotiations(@PathParam("id") long id) { 
		// TODO 
		// Check if user is admin or themselves
		
		// Changes the person’s associated negotiations to the set of person references
		Set<Negotiation> negotiations = getPersonNegotiations(id);
        return negotiations;  
    }
}

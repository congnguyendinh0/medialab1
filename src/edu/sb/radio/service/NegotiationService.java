package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

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

import edu.sb.radio.persistence.Negotiation;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/negotiations")  
public class NegotiationService {
	EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
	
	@GET  
    @Path("/{id}")  
    public Negotiation getNegotiations(@PathParam("id") @Positive long id) {
		Negotiation negotiation = entityManager.find(Negotiation.class, id);
		if (negotiation == null) throw new ClientErrorException(NOT_FOUND);
        return negotiation;
    }
	
	@POST
    @Path("/")
	@Consumes("application/json")
	@Produces("text/plain")
    public Negotiation addNegotiations(@FormParam("personID") long personID) {
		Person person = entityManager.find(Person.class, personID);
		
		if (person == null) throw new ClientErrorException(NOT_FOUND);
		
		Negotiation newNegotiation = new Negotiation(person); 
        return newNegotiation;
    }
}

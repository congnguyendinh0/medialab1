package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.persistence.EntityManager;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.sb.radio.persistence.Negotiation;
import edu.sb.radio.persistence.Person;
import edu.sb.radio.persistence.Negotiation.Type;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/negotiations")  
public class NegotiationService {	
	@GET  
    @Path("/{id}")  
    public Negotiation getNegotiations(@PathParam("id") @Positive long id) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Negotiation negotiation = entityManager.find(Negotiation.class, id);
		if (negotiation == null) throw new ClientErrorException(NOT_FOUND);
        return negotiation;
    }
	
	@POST
    @Path("/")
	@Consumes("application/json")
	@Produces("text/plain")
    public Negotiation addNegotiations(
    		@HeaderParam(BasicAuthenticationFilter.REQUESTER_IDENTITY) @Positive long requestedIdentity,
    		@QueryParam("type") String type,
    		@QueryParam("offer") String offer,
    		@QueryParam("answer") String answer
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Person negotiator = entityManager.find(Person.class, requestedIdentity);
		if (negotiator == null) throw new ClientErrorException(NOT_FOUND);
		
		Negotiation newNegotiation = new Negotiation(negotiator);
		newNegotiation.setType(Type.valueOf(type));
		newNegotiation.setOffer(offer);
		newNegotiation.setAnswer(answer);
		
        return newNegotiation;
    }
}

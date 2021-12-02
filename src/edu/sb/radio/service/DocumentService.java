package edu.sb.radio.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Positive;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import edu.sb.radio.persistence.Document;
import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/documents")  
public class DocumentService {
	static public final String DOCUMENT_CONTENT = "Document-Content";
	static public final String DOCUMENT_TYPE = "Document-Type";
	static public final String CONTENT_TYPE = "Content-Type";
	
	static private final String QUERY_DOCUMENT = "select d from Document as d where d.hash = :hash"; 
	
	@GET  
    @Path("/{id}")  
	public Response getDocument(@PathParam("id") @Positive long id) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Document document = entityManager.find(Document.class, id);
		if (document == null) throw new ClientErrorException(NOT_FOUND);
		
		return Response.status(200)
				.header(DOCUMENT_CONTENT, document.getContent())
				.header(DOCUMENT_TYPE, document.getType())
				.build();
    }
	
	@POST
    @Path("/") 
    @Consumes("*/*")
	@Produces("text/plain")
    public long addDocument(
    		@HeaderParam(CONTENT_TYPE) String contentType,
    		@FormParam("content") byte[] content
    		) {
		EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
		Document newDocument;
		
		// Checks if there is a matching persistent document
		final Document[] documents = entityManager.createQuery(QUERY_DOCUMENT, Long.class)
				.setParameter("hash", HashCodes.sha2HashText(256, content))
		        .getResultList()
		        .stream()
		        .map(identity -> entityManager.find(Document.class, identity))
		        .filter(document -> document != null)
		        .toArray(Document[]::new);
		
		if (documents.length == 1) {
			newDocument = documents[0];
		} else {
			newDocument = new Document(content);	
		}
		
		newDocument.setType(contentType);
		return newDocument.getIdentity();
    }
}

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

import edu.sb.radio.persistence.Document;
import edu.sb.radio.util.RestJpaLifecycleProvider;

@Path("/documents")  
public class DocumentService {
	EntityManager entityManager = RestJpaLifecycleProvider.entityManager("radio");
	
	@GET  
    @Path("/{id}")  
	public Document getDocument(@PathParam("id") @Positive long id) {  
		Document document = entityManager.find(Document.class, id);
		if (document == null) throw new ClientErrorException(NOT_FOUND);
        return document; 
    }
	
	@POST
    @Path("/") 
    @Consumes("*/*")
	@Produces("text/plain")
    public Document addDocument(
    		ContainerRequestContext requestContext,
    		@FormParam("id") long id,
    		@FormParam("content") String content
    		) {
		Document document;
		
		// Checks if there is a matching persistent document
		List<Document> documents = entityManager
				.createQuery("select d from Document as d where d.content =(:content)")
				.setParameter("content", content)
		        .getResultList();
		if (documents.size() == 1) {
			// TODO: content-type is altered to the value of the given header-field "Content-Type"
			document = documents.get(0);
			document.setType(null);
		} else {
			document = entityManager.find(Document.class, id);
			byte[] contentByte = content.getBytes();
			if (document == null) {
				document = new Document(contentByte);
			} else {
				document.setContent(contentByte);	
			}
		}
		
        return document;  
    }
}

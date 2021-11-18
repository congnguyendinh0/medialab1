package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "Document")
@Table(name = "Document")
public class Document extends BaseEntity {
	@Column(nullable = false, name = "hash")
	@Size(min = 64, max = 64)
    private String hash;
	
	@Column(nullable = false, name = "type")
	@Size(min = 1, max = 63)
    private String type;
	
	@Column(nullable = false, name = "content")
	@Size(min = 1, max = 16777215)
    private byte[] content;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    protected Document() {
    	this("123", "some type", "some content".getBytes());
    }
    
    public Document(String hash, String type, byte[] content) {
    	this.hash = hash;
    	this.type = type;
    	this.content = content;
    }
}

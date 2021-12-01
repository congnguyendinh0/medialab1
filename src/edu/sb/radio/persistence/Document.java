package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.eclipse.persistence.annotations.CacheIndex;
import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Document", indexes = {
		@Index(columnList = "hash", unique = false),
		@Index(columnList = "type", unique = true) 
})
@PrimaryKeyJoinColumn(name = "documentIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Document extends BaseEntity {
	@Size(min = 64, max = 64)
	@Column(nullable = false, updatable = false, insertable = true, unique = true)
	@CacheIndex(updateable = false)
	private String hash;
	
	@Size(min = 1, max = 63)
	@Column(nullable = false, updatable = true)
	private String type;
	
	@Size(min = 1, max = 16777215)
	@Column(nullable = false, updatable = false, insertable = true)
	private byte[] content;

	protected Document() {
		this(null);
	}

	public Document(byte[] content) {
		this.hash = HashCodes.sha2HashText(256, content);
		this.type = "application/octet-stream";
		this.content = content;
	}

	@JsonbProperty @XmlAttribute
	public String getHash() {
		return hash;
	}

	protected void setHash(String hash) {
		this.hash = hash;
	}

	@JsonbProperty @XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonbTransient @XmlTransient
	public byte[] getContent() {
		return content;
	}

	protected void setContent(byte[] content) {
		this.content = content;
	}
}

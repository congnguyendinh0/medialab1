package edu.sb.radio.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Document", indexes = { @Index(name = "type", columnList = "type", unique = true) })
@PrimaryKeyJoinColumn(name = "documentIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Document extends BaseEntity {
	private String hash;
	private String type;
	private byte[] content;

	protected Document() {
		this(null);
	}

	// good default for type (search later)
	public Document(byte[] content) {
		this.hash = HashCodes.sha2HashText(256, content);
		
		// TODO
		this.type = "something";
		
		this.content = content;
	}

	@Column(nullable = false, name = "hash", unique = true, updatable = true)
	@Size(min = 64, max = 64)
	@JsonbProperty @XmlAttribute
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Column(nullable = false, name = "type", updatable = true)
	@Size(min = 1, max = 63)
	@JsonbProperty @XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(nullable = false, name = "content", updatable = true)
	@Size(min = 1, max = 16777215)
	@JsonbTransient @XmlTransient
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}

package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import edu.sb.radio.util.HashCodes;

@Entity(name = "Document")
@Table(name = "Document", indexes = { @Index(name = "type", columnList = "type", unique = true) })
@PrimaryKeyJoinColumn(name = "discriminator_id")
public class Document extends BaseEntity {
	private int id;
	private String hash;
	private String type;
	private byte[] content;

	protected Document() {
		this(HashCodes.sha2HashText(256, "hash"), "some type", "some content".getBytes());
	}

	public Document(String hash, String type, byte[] content) {
		this.hash = hash;
		this.type = type;
		this.content = content;
	}

	@Id
	@Column(name = "documentIdentity")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(nullable = false, name = "hash", unique = true, updatable = true)
	@Size(min = 64, max = 64)
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Column(nullable = false, name = "type", updatable = true)
	@Size(min = 1, max = 63)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(nullable = false, name = "content", updatable = true)
	@Size(min = 1, max = 16777215)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}

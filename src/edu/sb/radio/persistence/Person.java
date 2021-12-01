package edu.sb.radio.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.annotations.CacheIndex;

import edu.sb.radio.util.HashCodes;
import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Person", indexes = @Index(columnList = "email", unique = true))
@PrimaryKeyJoinColumn(name = "personIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType @XmlRootElement
public class Person extends BaseEntity {
	static public enum Group { ADMIN, USER }
	static private final String DEFAULT_PASSWORD_HASH = HashCodes.sha2HashText(256, "password");
	
	@NotNull @Email @Size(min = 3, max = 128)
	@Column(nullable = false, updatable = true, length = 128, unique = true)
	@CacheIndex(updateable = true)
	private String email;

	@NotNull @Size(min = 64, max = 64)
	@Column(nullable = false, updatable = true, length = 64)
	private String passwordHash;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "groupAlias", nullable = false, updatable = true)
	private Group group;
	
	@NotNull @Valid
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "title", column = @Column(name = "title")),
		@AttributeOverride(name = "family", column = @Column(name = "surname")),
		@AttributeOverride(name = "given", column = @Column(name = "forename"))
	})
	private Name name;
	
	@NotNull @Valid
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "street", column = @Column(name = "street")),
		@AttributeOverride(name = "postcode", column = @Column(name = "postcode")),
		@AttributeOverride(name = "city", column = @Column(name = "city")),
		@AttributeOverride(name = "country", column = @Column(name = "country"))
	})
	private Address address;
	
	@NotNull
	@ElementCollection
	@CollectionTable(
		schema = "radio",
		name = "PhoneAssociation",
		joinColumns = @JoinColumn(name = "personReference", nullable = false, updatable = true),
		uniqueConstraints = @UniqueConstraint(columnNames = { "personReference", "phone" })
	)
	@Column(name = "phone", nullable = false, updatable = true, length = 16)
	private Set<String> phones;
	
	@NotNull
	@OneToMany(mappedBy = "negotiator", orphanRemoval = true, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	private Set<Negotiation> negotiations;
	
	@NotNull
	@OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE })
	private Set<Track> tracks;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "avatarReference", nullable = false, updatable = true)
	private Document avatar;
	
	public Person() {
		this.passwordHash = DEFAULT_PASSWORD_HASH;
		this.group = Group.USER;
		this.name = new Name();
		this.address = new Address();
		this.phones = new HashSet<>();
		this.negotiations = Collections.emptySet();
		this.tracks = Collections.emptySet();
	}

	@JsonbTransient @XmlTransient
	public Set<Negotiation> getNegotiations() {
		return negotiations;
	}

	protected void setNegotiations(Set<Negotiation> negotiations) {
		this.negotiations = negotiations;
	}

	@JsonbTransient @XmlTransient
	public Set<Track> getTracks() {
		return tracks;
	}

	protected void setTracks(Set<Track> tracks) {
		this.tracks = tracks;
	}
	
	@JsonbTransient @XmlTransient
	public Document getAvatar() {
		return avatar;
	}

	public void setAvatar(Document avatar) {
		this.avatar = avatar;
	}
	
	@JsonbTransient @XmlTransient
	public Set<String> getPhones() {
		return phones;
	}

	protected void setPhones(Set<String> phones) {
		this.phones = phones;
	}

	@JsonbProperty @XmlAttribute
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonbTransient @XmlTransient
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@JsonbProperty @XmlAttribute
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@JsonbProperty @XmlAttribute
	public Name getName() {
		return name;
	}

	protected void setName(Name name) {
		this.name = name;
	}

	@JsonbProperty @XmlAttribute
	public Address getAddress() {
		return address;
	}

	protected void setAddress(Address address) {
		this.address = address;
	}
	
	/**
	 * Aufgabe 2
	 */
	@JsonbProperty @XmlAttribute
	protected Long getAvatarReference() {
		return this.avatar == null ? null : this.avatar.getIdentity();
	}
	
	@JsonbProperty @XmlAttribute
	protected long[] getTrackReferences() {
		return this.tracks.stream().mapToLong(Track::getIdentity).toArray();
	}
}

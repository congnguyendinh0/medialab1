package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;

@Entity(name = "Negotiation")
@Table(name = "Negotiation", indexes = { @Index(name = "type", columnList = "type", unique = true),
		@Index(name = "offer", columnList = "offer", unique = true),
		@Index(name = "answer", columnList = "answer", unique = true) })
@PrimaryKeyJoinColumn(name = "discriminator_id")
public class Negotiation extends BaseEntity {
	public enum NegotiationType {
		WEB_RTC
	}

	private int id;
	private int personID;
	private NegotiationType type;
	private String offer;
	private String answer;

	protected Negotiation() {
		this(1, NegotiationType.WEB_RTC, "pls help", null);
	}

	public Negotiation(int personID, NegotiationType type, String offer, String answer) {
		this.personID = personID;
		this.type = type;
		this.offer = offer;
		this.answer = answer;
	}

	@Id
	@Column(name = "negotiationIdentity")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "negotiatorReference", referencedColumnName = "personIdentity", updatable = true)
	public int getPersonID() {
		return personID;
	}

	protected void setPersonID(int personID) {
		this.personID = personID;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "type", updatable = true)
	@Valid
	public NegotiationType getType() {
		return type;
	}

	public void setType(NegotiationType type) {
		this.type = type;
	}

	@Column(nullable = false, name = "offer", updatable = true)
	@Size(min = 0, max = 2046)
	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	@Column(nullable = true, name = "answer", updatable = true)
	@Size(min = 0, max = 2046)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}

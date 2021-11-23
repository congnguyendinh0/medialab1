package edu.sb.radio.persistence;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbVisibility;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import edu.sb.radio.util.JsonProtectedPropertyStrategy;

@Entity
@Table(schema = "radio", name = "Negotiation", indexes = { 
		@Index(name = "type", columnList = "type", unique = true),
		@Index(name = "offer", columnList = "offer", unique = true),
		@Index(name = "answer", columnList = "answer", unique = true) 
})
@PrimaryKeyJoinColumn(name = "negotiationIdentity")
@JsonbVisibility(JsonProtectedPropertyStrategy.class)
@XmlType
@XmlRootElement
public class Negotiation extends BaseEntity {
	static public enum Type { WEB_RTC }
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "type", updatable = true)
	private Type type;
	
	@NotNull
	@Column(nullable = false, name = "offer", updatable = true, length = 2046)
	@Size(max = 2044)
	private String offer;
	
	@Column(nullable = true, name = "answer", updatable = true, length = 2046)
	@Size(max = 2044)
	private String answer;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "negotiatorReference", nullable = false, updatable = false, insertable = true)
	private Person negotiator;

	public Negotiation(Person negotiator) {
		this.negotiator = negotiator;
		this.type = Type.WEB_RTC;
	}
	
	protected Negotiation() {
		this(null);
	}
	
	@JsonbTransient @XmlTransient
	public Person getNegotiator() {
		return negotiator;
	}

	protected void setNegotiator(Person negotiator) {
		this.negotiator = negotiator;
	}

	@JsonbProperty @XmlAttribute
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@JsonbProperty @XmlAttribute
	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	@JsonbProperty @XmlAttribute
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}

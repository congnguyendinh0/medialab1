package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "Negotiation")
@Table(name = "Negotiation")
public class Negotiation extends BaseEntity {
	@Embedded
	private NegotiationType negotiationType;
	
	@Column(nullable = false, name = "offer")
	@Size(min = 0, max = 2044)
    private String offer;

    @Column(name = "answer")
    @Size(min = 0, max = 2044)
    private String answer;

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    protected Negotiation() {
    	this("pls help", null);
    }
    
    public Negotiation(String offer, String answer) {
    	this.offer = offer;
    	this.answer = answer;
    }
}

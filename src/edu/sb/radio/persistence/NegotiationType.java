package edu.sb.radio.persistence;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class NegotiationType extends BaseEntity {
	@NotNull
	private NegotiationType _WEB_RTC;

    public NegotiationType getFamily() {
        return _WEB_RTC;
    }

    public void setFamily(NegotiationType _WEB_RTC) {
        this._WEB_RTC = _WEB_RTC;
    }

    protected NegotiationType() {
    	this(new NegotiationType());
    }
    
    public NegotiationType(NegotiationType _WEB_RTC) {
    	this._WEB_RTC = _WEB_RTC;
    }
}

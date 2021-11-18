package edu.sb.radio.persistence;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Address extends BaseEntity {	
	@NotNull
	@Size(min = 0, max = 63)
    private String street;
	
	@NotNull
	@Size(min = 0, max = 15)
    private String postcode;
	
	@NotNull
	@Size(min = 1, max = 63)
    private String city;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postcode;
    }

    public void setPostCode(String postcode) {
        this.postcode = postcode;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    protected Address() {
    	this("some street", "some plz", "some city");
    }
    
    public Address(String street, String postcode, String city) {
    	this.street = street;
    	this.postcode = postcode;
    	this.city = city;
    }
}

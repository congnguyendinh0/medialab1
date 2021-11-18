package edu.sb.radio.persistence;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Group extends BaseEntity {	
	@NotNull
    private Group _ADMIN;

    @NotNull
    private Group _USER;

    public Group getAdmin() {
        return _ADMIN;
    }

    public void setAdmin(Group _ADMIN) {
        this._ADMIN = _ADMIN;
    }
    
    public Group getUser() {
        return _USER;
    }

    public void setUser(Group _USER) {
        this._USER = _USER;
    }
    
    protected Group() {
    	this._ADMIN = new Group();
    	this._USER = new Group();
    }
}

package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "Track")
@Table(name = "Track")
public class Track extends BaseEntity {
	@Column(nullable = false, name = "name")
	@Size(min = 1, max = 127)
    private String name;
	
	@Column(nullable = false, name = "artist")
	@Size(min = 1, max = 127)
    private String artist;
	
	@Column(nullable = false, name = "genre")
	@Size(min = 1, max = 31)
    private String genre;

    @Column(nullable = false, name = "ordinal")
    private Integer ordinal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    protected Track() {
    	this("some name", "some artist", "some genre", 1);
    }
    
    public Track(String name, String artist, String genre, Integer ordinal) {
    	this.name = name;
    	this.artist = artist;
    	this.genre = genre;
    	this.ordinal = ordinal;
    }
}

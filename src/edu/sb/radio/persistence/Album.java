package edu.sb.radio.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "Album")
@Table(name = "Album")
public class Album extends BaseEntity {
	@Column(nullable = false, name = "title")
	@Size(min = 0, max = 127)
    private String title;
	
	@Column(nullable = false, name = "releaseYear")
    private Integer releaseYear;
	
	@Column(nullable = false, name = "trackCount")
    private Integer trackCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    
    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    protected Album() {
    	this("some title", 2021, 12);
    }
    
    public Album(String title, Integer releaseYear, Integer trackCount) {
    	this.title = title;
    	this.releaseYear = releaseYear;
    	this.trackCount = trackCount;
    }
}

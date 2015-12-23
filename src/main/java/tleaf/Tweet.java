package tleaf;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @ManyToOne
    @JoinColumn(name = "profile")
    private Profile profile;
    @Column
    private final String message;
    @Column
    private final Date creationDate;


    public Tweet(String message, Date creationDate) {
        this(null, message, creationDate);
    }

    public Tweet(Long id, String message, Date creationDate) {
        this.id = id;
        this.message = message;
        this.creationDate = creationDate;
    }

    public Tweet(Long id, Profile profile, String message, Date creationDate) {
        this.id = id;
        this.profile = profile;
        this.message = message;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, new String[]{"id", "creationDate"});
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[]{"id", "creationDate"});
    }
}

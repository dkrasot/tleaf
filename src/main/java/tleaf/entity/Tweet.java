package tleaf.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Date;

public class Tweet {

    private final Long id;
    private final String message;
    private final Date creationDate;

    public Tweet(String message, Date creationDate) {
        this(null, message, creationDate);
    }

    public Tweet(Long id, String message, Date creationDate) {
        this.id = id;
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

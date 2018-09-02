package dk.darkares.PassProtect.models;

import javax.persistence.*;

@Entity
@Table(name = "EventLog")
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private java.sql.Timestamp timestamp;
    private String event;
    private String message;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
package dk.darkares.PassProtect.models;

import javax.persistence.*;

@Entity
@Table(name = "KeyStore")
public class KeyStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String name;
    private String keyContent;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getkeyContent() {
        return keyContent;
    }

    public void setkeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

}
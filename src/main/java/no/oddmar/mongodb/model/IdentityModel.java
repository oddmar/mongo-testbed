package no.oddmar.mongodb.model;

import javax.persistence.Id;

/**
 * Documentation here.
 *
 * @author oddmar
 */
public class IdentityModel {
    private String id;

    @Id
    public String getId() {
        return id;
    }
}

package no.oddmar.mongodb.model;

import org.mongojack.DBRef;

import java.util.List;

/**
 * Simple organization
 *
 * @author oddmar
 */
public class Organization extends IdentityModel {
    public String name;
    public List<DBRef<User, String>> users;

    @Override
    public String toString() {
        return "Organization {" +
                getId() +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}

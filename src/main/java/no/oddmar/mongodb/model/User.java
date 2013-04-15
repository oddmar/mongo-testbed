package no.oddmar.mongodb.model;

import org.bson.types.ObjectId;

/**
 * Documentation here.
 *
 * @author oddmar
 */
public class User extends IdentityModel {
    public String loginId;
    public String email;
    public String firstName;
    public String lastName;
    public ObjectId organization;

    @Override
    public String toString() {
        return "User {" +
                getId() +
                " '" + loginId + '\'' +
                ", '" + email + '\'' +
                ", '" + firstName + '\'' +
                ", '" + lastName + '\'' +
                ", org=" + organization +
                '}';
    }
}

package no.oddmar.mongodb;

import no.oddmar.mongodb.model.Organization;
import no.oddmar.mongodb.model.User;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.mongojack.JacksonDBCollection;

import java.util.List;

/**
 * Documentation here.
 *
 * @author oddmar
 */
public class MongoService {

    private DB db;

    public MongoService(MongoClient mongoClient, String dbName) {
        db = mongoClient.getDB(dbName);
    }

    public List<Organization> getOrganizations() {
        org.mongojack.DBCursor<Organization> cursor = getCollection("organizations", Organization.class).find();
        return cursor.toArray();
    }

    public void addOrganizations(List<Organization> organizations) {
        getCollection("organizations", Organization.class).insert(organizations);
    }

    public void addUsers(List<User> users) {
        getCollection("users", User.class).insert(users);
    }

    public List<User> getAllUsers() {
        org.mongojack.DBCursor<User> cursor = getCollection("users", User.class).find();
        return cursor.toArray();
    }

    /**
     * Retrieve a named collection of a given type and return a wrapped, generic-supported
     * JacksonDBCollection
     */
    private <T> JacksonDBCollection<T, String> getCollection(String name, Class<T> itemClass) {
        return JacksonDBCollection.wrap(
                db.getCollection(name),
                itemClass,
                String.class);
    }

    public static void main(String... args) {
        System.out.println("Here are my things");

        try {
            MongoService service = new MongoService(new MongoClient("localhost", 27017), "mydb");

            List<User> users = service.getAllUsers();
            System.out.println("Here are the users");
            for (User u : users)
                System.out.println(u.getId() + ": (" + u.loginId + ": " + u.firstName + " " + u.lastName + ")");
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
            System.exit(-1);
        }

    }

}

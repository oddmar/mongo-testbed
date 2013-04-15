package no.oddmar.mongodb;

import no.oddmar.mongodb.model.Organization;
import no.oddmar.mongodb.model.User;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.BeforeClass;
import org.mongojack.JacksonDBCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Documentation here.
 *
 * @author oddmar
 */
public class QueryTest {

    private static MongoClient mongoClient;
    private static DB db;

    @BeforeClass
    public static void initBeforeTests() throws Exception {
        if (mongoClient == null) {
            mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("mymj");
        }
    }

    @Test
    public void testUsers() {
        org.mongojack.DBCursor<User> cursor = getCollection("users", User.class).find();
        System.out.println("Total users: " + cursor.length());
        for ( User u : cursor.toArray() ) {
            System.out.println(u.loginId + ":" + u.email);
        }
    }

    @Test
    public void testWithData() {
        addTestData();

        List<Organization> orgs = getOrganizations();
        List<User> users = getUsers();

        System.out.println("-------- Retrieved orgs (" + orgs.size() + ")");
        for ( Organization org : orgs )
            System.out.println(org);

        System.out.println("-------- Retrieved users (" + users.size() + ")");
        for ( User u:  users )
            System.out.println(u);
    }

    private List<Organization> getOrganizations() {
        return getCollection("organizations", Organization.class).find().toArray();
    }

    private List<User> getUsers() {
        return getCollection("users", User.class).find().toArray();
    }

    private void addTestData() {
        db.getCollection("organizations").drop();
        db.getCollection("users").drop();

        List<User> users = new ArrayList<>();
        Random r = new Random(System.currentTimeMillis());

        List<Organization> orgs = new ArrayList<>();
        for ( int i=1; i<=10; i++ ) {
            Organization o = new Organization();
            o.name = "Org" + i;
            orgs.add(o);
        }
        getCollection("organizations", Organization.class).insert(orgs);

        // NOTE: Must retrieve orgs to get object ids... careful with caching here.

        orgs = getOrganizations();
        for ( int i=1; i<=100; i++ ) {
            User u = new User();
            u.loginId = "user" + i;
            u.email = u.loginId + "@corpland.com";
            u.organization = new ObjectId(orgs.get(r.nextInt(orgs.size())).getId());
            users.add(u);
        }
        getCollection("users", User.class).insert(users);
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
}

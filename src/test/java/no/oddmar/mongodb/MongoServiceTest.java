package no.oddmar.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import no.oddmar.mongodb.model.Organization;
import no.oddmar.mongodb.model.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Documentation here
 *
 * @author oddmar
 */
public class MongoServiceTest {
    static final String TEST_DB = UUID.randomUUID().toString();
    private static MongoClient mongoClient;
    private MongoService mongoService;

    @BeforeClass
    public static void initBeforeTests() throws Exception {
        if (mongoClient == null)
            mongoClient = new MongoClient("localhost", 27017);
        clearDatabase();
    }

    @AfterClass
    public static void cleanAfterTests() throws Exception {
        // We start the test suite with a completely empty database
        clearDatabase();
    }

    @Before
    public void setUp() throws Exception {
        mongoService = new MongoService(mongoClient, TEST_DB);
    }

    @Test
    public void testOrganizations() throws Exception {
        // Start with nothing
        List<Organization> orgs = mongoService.getOrganizations();
        System.out.println(orgs.size() + " organizations found");
        assertTrue(orgs.isEmpty());

        // Add some orgs
        addOrganizations(12);
        orgs = mongoService.getOrganizations();
        System.out.println(orgs.size() + " organizations found");
        assertEquals(12, orgs.size());

        // Again
        addOrganizations(21);
        orgs = mongoService.getOrganizations();
        System.out.println(orgs.size() + " organizations found");
        assertEquals(33, orgs.size());
    }

    @Test
    public void testUsers() throws Exception {
        // Start with nothing
        List<User> users = mongoService.getAllUsers();
        System.out.println(users.size() + " users found");
        assertTrue(users.isEmpty());

        // Add some orgs
        addUsers(29);
        users = mongoService.getAllUsers();
        System.out.println(users.size() + " users found");
        assertEquals(29, users.size());

        // Again
        addUsers(4);
        users = mongoService.getAllUsers();
        System.out.println(users.size() + " users found");
        assertEquals(33, users.size());
    }

    private void addOrganizations(int numOrgs) {
        Random rand = new Random(System.currentTimeMillis());
        List<Organization> orgs = new ArrayList<>(numOrgs);
        for (int i = 0; i < numOrgs; i++) {
            Organization org = new Organization();
            org.name = "Org" + rand.nextInt(10000);
            orgs.add(org);

        }
        mongoService.addOrganizations(orgs);
    }

    private void addUsers(int numUsers) {
        Random rand = new Random(System.currentTimeMillis());
        List<User> users = new ArrayList<>(numUsers);
        for (int i = 0; i < numUsers; i++) {
            User u = new User();
            u.loginId = "user" + rand.nextInt(10000);
            u.firstName = "first";
            u.lastName = "last";
            u.email = u.loginId + "@domain.com";
            users.add(u);
        }
        mongoService.addUsers(users);
    }

    // We start the test suite with a completely empty database
    private static void clearDatabase() throws UnknownHostException {
        DB db = mongoClient.getDB(TEST_DB);
        //db.dropDatabase();
    }

}

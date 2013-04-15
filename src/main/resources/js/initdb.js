/**
 * Stuff some things into Mongo
 */
var mongoose = require('mongoose'),
    Organization = require('./model').Organization,
    User = require('./model').User,
    util = require('./util'),
    async = require('async');

var uri = 'mongodb://localhost/mydb';
var orgs = [];
var users = [];

/**
 * Execute a set of tasks in order using the async library
 * makes for cleaner code
 */
async.series([
    // Connect to the databae
    function(callback) {
        mongoose.connect(uri, function (err) {
            if (err) {
                console.log('ERROR connecting to: ' + uri + '. ' + err);
                throw err;
            } else {
                console.log('Connected to: ' + uri);
            }
        });
        callback();
    },
    // Empty the database first
    function (callback) {
        ['users', 'organizations'].forEach(function (item) {
            console.log('Dropping collection ' + item);
            mongoose.connection.collection(item).drop(function (err) {
                if (err) return console.error(err);
                console.log('Dropped collection ' + item);
            });
        })
        callback();
    },
    // Create some test data
    function (callback) {
        createTestData();
        console.log("Created test data");
        callback();
    },
    // Save organization test data
    function (callback) {
        orgs.map(function (org) {
            org.save(function (err) {
                if (err) return console.error(err);
            })
            console.log("Orgs saved");
        });
        callback();
    },
    // Save user test data
    function (callback) {
        users.map(function (user) {
            user.save(function (err) {
                if (err) return console.error(err);
            })
            console.log("Users saved");
        });
        callback();
    }
],
// Optional callback - close database and clean up
function (err) {
    if ( err ) console.error(err);
    mongoose.connection.close(function (err) {
        if ( !err )
            console.log('Closed database connection');
    });
    console.log("Done");
});

function createTestData() {
    for ( var i=1; i<=10; i++ ) {
        orgs.push(new Organization({name: 'Org' + i}));
    }
    for (var j=0; j<100; j++) {
        var uid = util.createRandomWord(6);
        var org = orgs[Math.floor(Math.random() * orgs.length)];
        var user = new User({
            loginId: uid,
            email: uid + '@org.com',
            name : {
                first: util.createRandomWord(4),
                last: util.createRandomWord(10)
            },
            organization : org});
        users.push(user);
        org.users.push(user);
    }
}


/*
 db.once('open', function callback () {
 // yay!
 console.log('Connected to database');
 // db.collection("users").count(function(err, count) { console.log('Count: ' + count)});
 });
 */

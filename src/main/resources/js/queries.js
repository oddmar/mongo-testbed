/**
 * Get some data out of Mongo
 */
var mongoose = require('mongoose'),
    Organization = require('./model').Organization,
    User = require('./model').User,
    async = require('async');

var uri = 'mongodb://localhost/mydb';

mongoose.connect(uri, function (err) {
    if (err) {
        console.log('ERROR connecting to: ' + uri + '. ' + err);
        throw err;
    } else {
        console.log('Connected to: ' + uri);
    }
});

Organization
    .findOne({name: 'Org2'})
    .populate('users')
    .exec(function(err, result) {
        if ( err ) return handleError(err);
        if ( result ) {
            console.log('------------------ Organization Query ---------------------');
            console.log(result.name + ': ' + result.users.map(function(item) { return item.email}));
        }
    });

User
    .find()
    .populate('organization')
    .exec(function(err, result) {
        if ( err ) return handleError(err);
        if ( result ) {
            console.log('------------------ User Query ---------------------');
            console.log('Found %d users', result.length);
            result.forEach(function(item) {
                console.log(item.name.full + ': ' + item.organization.name);
            });
        }
    });

User
    .find()
    .populate({
        path: 'organization'
        , match: {name: 'Org3'}})
    .exec(function(err, result) {
        if ( err ) return handleError(err);
        if ( result ) {
            console.log('------------------ User Complex Query ---------------------');
            console.log('Found %d users', result.length);
            result.forEach(function(item) {
                console.log(item);
            });
        }
    });
/*
    .populate('organization')
    .where('organization.name').equals('Org3')
    .sort('name.last')
    .select('name email')
    .exec(function(err, result) {
        if ( err ) return handleError(err);
        if ( result ) {
            console.log('------------------ User Complex Query ---------------------');
            console.log('Found %d users', result.length);
            result.forEach(function(item) {
                console.log(item);
            });
        }
    });

Kitten.find().populate({
    path: 'owner'
    , select: 'name'
    , match: { color: 'black' }
    , options: { sort: { name: -1 }}
}).exec(function (err, kittens) {
        console.log(kittens[0].owner.name) // Zoopa
    })
*/
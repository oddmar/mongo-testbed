/**
 *  Model
 */
var mongoose = require('mongoose');
var ObjectId = mongoose.Schema.ObjectId;

// Everything in Mongoose starts with a Schema.
// Each schema maps to a MongoDB collection and defines the
// shape of the documents within that collection.
var UserSchema = new mongoose.Schema({
    organization: {type: ObjectId, ref: 'Organization'},
    loginId: String,
    email: String,
    name: {
        first: String,
        last: String
    }
});

var OrganizationSchema = new mongoose.Schema({
    name: String,
    users: [{ type: ObjectId, ref: 'User' }]
});

// Schemas not only define the structure of your document and casting of properties,
// they also define document instance methods, static Model methods, compound indexes
// and document lifecycle hooks called middleware.

OrganizationSchema.statics.findByName = function (name, cb) {
    this.find({ name: new RegExp(name, 'i') }, cb);
}

// Indexes can be defined at the path level or the schema level. Defining indexes
// at the schema level is necessary when defining compound indexes.
// Here, we create an index on loginId in ascending order
UserSchema.index({loginId : 1});

// Virtual attributes are attributes that are convenient to have around
// but that do not get persisted to MongoDB.
UserSchema.virtual('name.full').get(function () {
    return this.name.first + ' ' + this.name.last;
});

//
// The API we expose for this module
//
module.exports = {
    User: mongoose.model('User', UserSchema),
    Organization: mongoose.model('Organization', OrganizationSchema)
}
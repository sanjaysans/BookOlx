var mongoose = require('mongoose');
var jwt = require('jsonwebtoken');
var _ = require('lodash');

var Schema = mongoose.Schema, _id = Schema.ObjectID;

var userSchema = new Schema({
		roll: { type: Number, unique: true, require: true},
		name: { type : String, require: true},
		department: { type : String, require: true},
		mobile: { type :Number, require: true},
		mail: { type : String, require: true},
		password: { type : String, require: true},
		tokens : [{ type : String}]
	});


userSchema.methods.tosave = function(res) {
	var user = this;
	var token = jwt.sign({ roll: user.roll, _id: user._id}, 'pass' ).toString();
	user.tokens.push(token);
	user.save().then(doc => {
		return res.header('x-auth', token).status(200).send(user);
	}).catch(err => {
		return res.status(404).send("roll number already exists or try again");
	});
};

var record = mongoose.model('record', userSchema);
module.exports = {record};


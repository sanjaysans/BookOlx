var mongoose = require('mongoose');

var Schema = mongoose.Schema, _id = Schema.ObjectID;

var adschema = new Schema({
	roll: { type: Number, require: true},
	name: { type : String, require: true},
	department: { type : String, require: true},
	mobile: { type :Number, require: true},
	mail: { type : String, require: true},
	book_name: { type : String, require: true},
	author:{ type : String, require: true},
	edition:{ type : String, require: true},
	price:{ type : String, require: true},
	image:{ type : String, require: true}
});

var ad = mongoose.model('ad', adschema);
module.exports = {ad};

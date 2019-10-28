var mongoose = require('mongoose');
var express = require('express');
var {record} = require('./userSchema');
var {ad} = require('./adschema');
var bodyParser = require('body-parser');
var promise = require('promise');
var Cryptr = require('cryptr'),
    cryptr = new Cryptr('sanjaykumar');
var jwt = require('jsonwebtoken');
mongoose.set('useCreateIndex', true);
const PORT = process.env.PORT || 3001;

var app = express();
app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));
mongoose.Promise = Promise;

const url = "mongodb://smsk1999:smsk1999@ds139331.mlab.com:39331/bookolx";

mongoose.connect(url,{ useNewUrlParser: true }, function(err){
	if(err) throw err;
	console.log('successfully connected');
	});

app.post("/signup", (req, res) => {
	var data = new record(req.body);
	if(validate(data)){
		data.password = encrypt(data.password);
		data.tosave(res);
	} else {
		res.status(404).send("not a valid data");
	}
});

app.post("/login",(req,res) => {
	record.findOne({ roll: req.body.roll}).then((docs) => {
		var pass = decrypt(docs.password);
		if(req.body.password == pass){
			docs.tosave(res);
		}else{
			res.status(404).send("Incorrect password");
		}
	}).catch(err => {
		res.status(404).send("roll number not registered1");
	});
});

app.post("/logout", (req, res) => {
	var token = req.header('x-auth');
	try{
	var decode = jwt.verify(token, 'pass');
	}
	catch(e){
		return res.status(500).send("Token Error");
	}	
	record.findOneAndUpdate({_id: decode._id}, {$pull: {tokens: token}}).then(() => {
		return res.status(200).send("Logout success");
	}).catch(err => {
		return res.status(500).send("Logout Error");
	});
});

function encrypt(text){
	var encryptedString = cryptr.encrypt(text);
	return encryptedString;
}
 
function decrypt(text){
	var decryptedString = cryptr.decrypt(text);
	return decryptedString;
}

function validate(data){
	var flag = 1;
	return flag;
}

function adminverify(token){
	return new Promise((resolve) => {
		try{
			var decode = jwt.verify(token, 'pass');
		}
		catch(e){
			resolve(0);
		}
		record.findOne({_id: decode._id}).then((docs) => {
			if(!docs){
				resolve(0);
			}
			var i = docs.tokens.indexOf(token);
			if(i > -1){
				resolve(1);
			}else{
				resolve(0);
			}
		}).catch(err => {
			resolve(0);
		});
	});	
}

app.post("/newad", (req, res) => {
	var token = req.header('x-auth');
	var data = new ad(req.body);
	adminverify(token).then((flag) => {
		if(flag == 1){
			data.save().then(doc => {
				return res.status(200).send(doc);
			}).catch(err => {
				return res.status(500).send("not a valid data");
			});
		}else{
			return res.status(500).send("Invalid Token");
		}
	}).catch(err => {
		return res.status(500).send("not a valid token");
	});
});

app.post("/getallad", (req, res) => {
	var token = req.header('x-auth');
	adminverify(token).then((flag) => {
		if(flag == 1){
			ad.find().then((docs) => {
				return res.status(200).send(docs);
			}).catch(err => {
				return res.status(500).send("No Ad Available");
			});
		}else{
			return res.status(500).send("Invalid Token");
		}
	}).catch(err => {
		return res.status(500).send("not a valid token");
	});
});

app.post("/getuserad", (req, res) => {
	var token = req.header('x-auth');
	adminverify(token).then((flag) => {
		if(flag == 1){
			ad.find({roll: req.body.roll}).then((docs) => {
				return res.status(200).send(docs);
			}).catch(err => {
				return res.status(500).send("No Ad Available");
			});
		}else{
			return res.status(500).send("Invalid Token");
		}
	}).catch(err => {
		return res.status(500).send("not a valid token");
	});
});

app.post("/deluserad", (req, res) => {
	var token = req.header('x-auth');
	adminverify(token).then((flag) => {
		if(flag == 1){
			ad.findOneAndDelete({_id: req.body.id}).then((docs) => {
				return res.status(200).send(docs);
			}).catch(err => {
				return res.status(500).send("No Ad Available");
			});
		}else{
			return res.status(500).send("Invalid Token");
		}
	}).catch(err => {
		return res.status(500).send("not a valid token");
	});
});

app.listen(PORT);

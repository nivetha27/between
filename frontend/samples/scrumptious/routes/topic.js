var FB              = require('../../../fb');

var Client = require('node-rest-client').Client;
var client = new Client();

exports.gettopic = function(req, res) {
    parameters.access_token     = req.session.access_token;
};

exports.posttopic = function(req, res) {
  var description=req.body.mysearch;
  var caption1=req.option1;
  var caption2=req.option2;

  FB.api('/me/', {
        fields:         'id,name',
        access_token:   req.session.access_token
    }, function (result) {
        console.log("enter");
        if(!result) {
            return res.send(500, 'error');
        } else if(result.error) {
            if(result.error.type == 'OAuthException') {
                result.redirectUri = FB.getLoginUrl({ scope: 'user_about_me', state: encodeURIComponent(JSON.stringify(parameters)) });
            }
            return res.send(500, result);
        }
        user_id = "fb_" + result.id;
        console.log("test user_id: " + user_id);
        //res.send(user_id);

        // set content-type header and data as json in args parameter
console.log("test: " + user_id);
var args = {
    data: { 
            userId: user_id,
            category: "default",
            description: req.body.mysearch,
            choices: [
            {
              caption: req.body.option1,
              imageUrl: req.body.option1
            },
            {
              caption: req.body.option2,
              imageUrl: req.body.option2
            }] },
    headers: { "Content-Type": "application/json" }
};

    client.post("http://localhost:8080/topic/", args, function (data, response) {
    // parsed response body as js object
    console.log(data);
    // raw response
    console.log(response);
    res.send(data);
}).on('error', function (err) {
    console.log('something went wrong on the request', err);
    res.send(err);
});
    });
};
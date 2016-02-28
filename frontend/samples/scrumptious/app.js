
var express       = require('express'),
    FB            = require('../../fb'),
    http          = require('http'),
    https         = require('https'),
    fs            = require('fs'),
    privateKey    = fs.readFileSync('/Users/vishnu/server.key', 'utf8'),
    certificate   = fs.readFileSync('/Users/vishnu/server.crt', 'utf8'),
    credentials   = {key: privateKey, cert: certificate},
    path          = require('path'),

    config        = require('./config'),

    api           = require('./routes/api'),
    home          = require('./routes/home'),
    meals         = require('./routes/meals');
    topic         = require('./routes/topic'); 

var app = express();

if(!config.facebook.appId || !config.facebook.appSecret) {
    throw new Error('facebook appId and appSecret required in config.js');
}

app.configure(function() {
    app.set('port', process.env.PORT || 3000);
    app.set('views', __dirname + '/views');
    app.set('view engine', 'ejs');
    app.use(express.favicon());
    app.use(express.logger('dev'));
    app.use(express.cookieParser());
    app.use(express.cookieSession({ secret: 'secret'}));
    app.use(express.bodyParser());
    app.use(express.methodOverride());
    app.use(app.router);
    app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function() {
    app.use(express.errorHandler());
});

app.get( '/',                home.index);
app.get( '/login/callback',  home.loginCallback);
app.get( '/logout',          home.logout);
app.get( '/search',          api.search);
app.get( '/friends',         api.friends);
app.post('/announce',        api.announce);
app.get( '/meals/:id',       meals.show);
app.get( '/winjs/meals/:id', meals.showWinJs); // this is used for facebook-winjs-sdk sample and not required to actually run this sample
app.post( '/get_topic',      topic.gettopic);
app.post( '/post_topic',     topic.posttopic);
app.post('/*', function(request, response) {
  response.redirect('/');
});

//http.createServer(app).listen(app.get('port'), function() {
//    console.log("Express server listening on port " + app.get('port'));
//});
httpsServer = https.createServer(credentials, app).listen(3000, function() {
    console.log("Express server listening on port " + 3000);
});

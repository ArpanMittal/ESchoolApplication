<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

use Illuminate\Support\Facades\Route;

App::singleton('oauth2', function() {
    //$storage = new OAuth2\Storage\Pdo(array('dsn' => 'mysql:dbname=laravel;host=localhost', 'username' => 'root', 'password' => ''));

    //return $storage;
    //return $storage;
   $storage = new App\Http\Controllers\MyPdo(array('dsn' => 'mysql:dbname=laravel;host=localhost', 'username' => 'root', 'password' => ''));
    //file_put_contents("a.txt",$storage);
    $server = new OAuth2\Server($storage);

    $server->addGrantType(new OAuth2\GrantType\ClientCredentials($storage));
    $server->addGrantType(new OAuth2\GrantType\UserCredentials($storage));
    
    return $server;
});



Route::get('insert','insert@doinsert');
Route::get('/', function () {
    return view('welcome');
});

/*Route::post('oauth/token', function()
{
    $bridgedRequest  = OAuth2\HttpFoundationBridge\Request::createFromRequest(Request::instance());
    $bridgedResponse = new OAuth2\HttpFoundationBridge\Response();

    $bridgedResponse = App::make('oauth2')->handleTokenRequest($bridgedRequest, $bridgedResponse);

    return $bridgedResponse;
});*/

Route::post('oauth/token','OAuthcontroller@getOAuthToken');

/*Route::get('private', function()
{
    $bridgedRequest  = OAuth2\HttpFoundationBridge\Request::createFromRequest(Request::instance());
    $bridgedResponse = new OAuth2\HttpFoundationBridge\Response();

    if (App::make('oauth2')->verifyResourceRequest($bridgedRequest, $bridgedResponse)) {

        $token = App::make('oauth2')->getAccessTokenData($bridgedRequest);

        return Response::json(array(
            'private' => 'stuff',
            'user_id' => $token['user_id'],
            'client'  => $token['client_id'],
            'expires' => $token['expires'],
        ));
    }
    else {
        return Response::json(array(
            'error' => 'Unauthorized'
        ), $bridgedResponse->getStatusCode());
    }
});*/








Route::group(['prefix' => 'question','middleware' => ['check.session']], function () {
     //list of all question
     Route::get('/list',array('uses' => 'QuestionController@getAllQuestionList'));
    Route::post('/search',array('uses' => 'QuestionController@getAllQuestionList'));
    Route::get('/',array('uses' => 'QuestionController@getNewQuestion'));
    Route::get('/add',array('uses' => 'QuestionController@addQuestion'));
    Route::post('/add',array('uses' => 'QuestionController@addQuestion'));
    Route::post('/update',array('uses' => 'QuestionController@updateQuestion'));
    Route::get('/topic/list',array('uses' => 'QuestionController@getTopics'));
    Route::get('/chapter/list',array('uses' => 'QuestionController@getChapters'));
    Route::get('/{id}',array('uses' => 'QuestionController@editQuestion'));
});



Route::get('/', function () {
    return view('welcome');
})->middleware('check.session');

Route::get('home', array('uses' => 'HomeController@goHome'))->middleware('check.session');

// route to show the login form
Route::get('login', array('uses' => 'HomeController@showLogin'));

// route to process the form
Route::post('login', array('uses' => 'HomeController@doLogin'));

// route to process the form
Route::get('logout', array('uses' => 'HomeController@doLogout'));
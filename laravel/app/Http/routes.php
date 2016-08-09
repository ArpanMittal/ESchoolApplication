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
use App\Http\Controllers;

App::singleton('oauth2', function() {
    /*
     * create custom pdo class to change validation parameter
     */
    $array2=array('dsn'=>'mysql:dbname='.env('DB_DATABASE', 'forge').';host='.env('DB_HOST', 'localhost'),'username'  =>env('DB_USERNAME', 'forge'),'password'=>env('DB_PASSWORD', ''));
    $storage=new App\Http\Controllers\MyPdo($array2);
    $server = new OAuth2\Server($storage);
    /*
     * various grant_type our oath server support and gave response according to them
     */
    $server->addGrantType(new OAuth2\GrantType\ClientCredentials($storage));
    $server->addGrantType(new OAuth2\GrantType\UserCredentials($storage));
    $server->addGrantType(new OAuth2\GrantType\RefreshToken($storage));
    return $server;
});


/*
 * url to insert values into database 
 */
Route::get('insert','insert@doinsert');
/*
 * welcome screen
 */
Route::get('/', function () {
    return view('welcome');
});
/*
 * route path for signup and to refresh token after access_token expires
 * refreshtoken @input granttype,clientid,clientsecret,refreshtoken
 * signup @input username,password,clientid,clientsecret,granttype 
 * trustedclientaccess @input clientid,clientpassword,granttype
 * 
 */
Route::post('oauth/token','OAuthcontroller@getOAuthToken');
/*
 * middleware to check validation of access token and return user_id into request
 */
Route::group(['prefix'=>'post','middleware'=>['oauth']],function(){

    Route::post('getEmail','postController@getAllPost');
    
});






Route::group(['prefix' => 'question','middleware' => ['check.session']], function () {
     //list of all question
     Route::get('/list',array('uses' => 'QuestionController@getAllQuestionList'));
    Route::post('/search',array('uses' => 'QuestionController@getAllQuestionList'));
    Route::get('/',array('uses' => 'QuestionController@getNewQuestion'));
    Route::get('/next',array('uses' => 'QuestionController@nextQuestion'));
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
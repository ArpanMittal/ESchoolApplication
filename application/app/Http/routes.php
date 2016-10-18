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

Route::get('/', function () {

    return view('welcome');
    
});
Route::get('dropdown/{userdata}',function(){
	//echo $userdata;
	//return view('dropdown');
});
Route::get('login', 'login@showLogin');
Route::post('login','login@doLogin');
Route::get('insert','insert@doinsert');






Route::auth();

Route::get('/home', 'HomeController@index');

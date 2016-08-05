<?php

use \App\Http\Controllers\HomeController;
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
})->middleware('check.session');

Route::get('home', array('uses' => 'HomeController@goHome'))->middleware('check.session');

// route to show the login form
Route::get('login', array('uses' => 'HomeController@showLogin'));

// route to process the form
Route::post('login', array('uses' => 'HomeController@doLogin'));

// route to process the form
Route::get('logout', array('uses' => 'HomeController@doLogout'));

Route::group(['prefix' => 'question','middleware' => ['check.session']], function () {
     //list of all question
     Route::get('/list',array('uses' => 'QuestionController@getAllQuestionList'));
    Route::post('/search',array('uses' => 'QuestionController@getAllQuestionList'));
    Route::get('/',array('uses' => 'QuestionController@getNewQuestion'));
    Route::post('/add',array('uses' => 'QuestionController@addQuestion'));
    Route::get('/{id}',array('uses' => 'QuestionController@editQuestion{$id}'));
});
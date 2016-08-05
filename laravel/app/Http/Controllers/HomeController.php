<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\Redirect;

class HomeController extends Controller
{
    public function showLogin()
    {
        // show the form
        return view('auth.login');
    }

    public function doLogin(Request $request)
    {
    // process the form
        // validate the info, create rules for the inputs
        $rules = array(
            'email'    => 'required|email', // make sure the email is an actual email
            'password' => 'required|alphaNum|min:10' // password can only be alphanumeric and has to be greater than 3 characters
        );

        // run the validation rules on the inputs from the form
        $validator = Validator::make(Input::all(), $rules);

        // if the validator fails, redirect back to the form
        if ($validator->fails()) {
            return Redirect::to('login')
                ->withErrors($validator) // send back all errors to the login form
                ->withInput(Input::except('password')); // send back the input (not the password) so that we can repopulate the form
        } else {

            // create our user data for the authentication
            $userdata = array(
                'email'     => Input::get('email'),
                'password'  => Input::get('password')
            );

            // attempt to do the login
            $user = DB::table('user')
                ->whereEmailAndPassword(Input::get('email'),Input::get('password'))
                ->first();
            if ( !is_null($user) ){
                $request->session()->put('id',$user->id);
                return Redirect::to('home');
            }else{
                return Redirect::to('login')
                    ->withErrors('email','Email or Password not matched.')
                    ->withInput(Input::except('password'));
            }
        }
    }

    public function goHome(Request $request)
    {
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;
        return view('home',$data);
    }

    public function doLogout(Request $request)
    {
        $request->session()->forget('id');
        return Redirect::to('login');
    }
}

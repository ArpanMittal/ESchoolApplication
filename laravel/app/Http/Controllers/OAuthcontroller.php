<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;


class OAuthcontroller extends Controller
{
    /*
     * genertate acces token request for api
     * @input client_id, client_secret,grant_type,username,password
     * @output access_token,refresh_token
     */
    public function getOAuthToken(Request $request)
    {

       
        $bridgedRequest  = \OAuth2\HttpFoundationBridge\Request::createFromRequest($request->instance());
       
        $bridgedResponse = new \OAuth2\HttpFoundationBridge\Response();

        $bridgedResponse = \App::make('oauth2')->handleTokenRequest($bridgedRequest, $bridgedResponse);

        return $bridgedResponse;
    }
}

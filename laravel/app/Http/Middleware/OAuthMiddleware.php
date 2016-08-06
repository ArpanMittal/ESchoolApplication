<?php

namespace App\Http\Middleware;
use App\Http\Requests;

use Closure;

use OAuth2\HttpFoundationBridge\Request as OAuthRequest;
class OAuthMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        //file_put_contents("a.txt","hello".$request->input('access_token'));
        if (!$request->input('access_token')) {
            return response('Token Not Found',422);
        }
        
        $req=\Symfony\Component\HttpFoundation\Request::createFromGlobals();
        $bridgedRequest=OAuthRequest::createFromRequest($req);

        $bridgedResponse = new \OAuth2\HttpFoundationBridge\Response();

        if(!$token=\App::make('oauth2')->getAccessTokenData($bridgedRequest,$bridgedResponse))
        {
            $response=\App::make('oauth2')->getResponse();
            if($response->isClientError()&&$response->getParameter('error'))
            {
                if($response->getParameter('error')=='expired_token')
                {
                    return response('The access token is expired',401);
                }
                else{
                    return response('InvalidToken',422);
                }
            }

        }
        else
        {
            //file_put_contents("a.txt",$token);
            $request['user_id']=$token['user_id'];
        }
        return $next($request);
    }
}

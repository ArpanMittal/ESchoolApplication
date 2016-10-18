<?php

namespace App\Http\Middleware;

use Closure;
use Mockery\Exception;

class SignUpMiddleware
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
        if (!$request->input('client_id')&&!$request->input('client_secret')) {
            return response()->json(['sucess'=>'false','message'=>'client id not found','code'=>'422']);
        }
        else
        {

            $data=\DB::table('oauth_clients')
                ->where('client_id','=',$request->input('client_id'))
                ->where('client_secret','=',$request->input('client_secret'))->get();

            if($data!=null)
            {
                return $next($request);
            }
            else
            {

                return response()->json(['sucess'=>'false','message'=>'client_id not valid','code'=>'401']);
            }

        }

    }
}

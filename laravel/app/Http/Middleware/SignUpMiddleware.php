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
            /*try {

                $db=\DB::connection()->getpdo();

                $statement = $db->prepare("select * from :table where client_id = :client_id AND client_secret=:client_secret");
                $statement->execute(array(':client_id' => $request->input('client_id'), ':table'=>"oauth_clients",':client_secret'=>$request->input('client_secret')));
                $row=$statement->fetch();
                return($statement->exec($statement));

            }catch (Exception $e)
            {
                return response()->json(['sucess'=>'false','message'=>'pdo exception','code'=>'422']);
            }*/

            $data=\DB::table('oauth_clients')
                ->where('client_id','=',$request->input('client_id'))
                ->where('client_secret','=',$request->input('client_secret'))->get();

            if(isset($data))
            {
                return $next($request);
            }
            else
            {

                return response()->json(['sucess'=>'false','message'=>'client_id not valid','code'=>'422']);
            }

        }

    }
}

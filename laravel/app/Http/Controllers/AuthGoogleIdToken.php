<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\DB;


class AuthGoogleIdToken extends Controller
{
    //
    public $clientId="964686197695-pnlgnh62j47hlpil5snj30lv7emq5c2v.apps.googleusercontent.com";
    public function doAuth(Request $request)
    {

        $id=$request->input('code');

        $url = 'https://www.googleapis.com/oauth2/v3/tokeninfo?id_token='.$id;
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, false);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);


        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);

        $jArr = json_decode($result, true);

        if(is_null($jArr['email']))
        {
            return response()->json(['sucess'=>false,'message'=>'TokenId Failed or Expired','code'=>498]);
        }

        if(strcmp($this->clientId,$jArr['azp']))
        {
            return response()->json(['sucess'=>false,'message'=>'unknown client','code'=>401]);
        }


        //$request->input(array(['password'=>$jArr['sub'],'email'=>$jArr['email']]));
        return $this->doInsert($request,$jArr);
        //return $jArr['email'];


    }

    public function isFirstTime($jArr)
    {
        /*
         * check weather email already exist or not
         */
            $users = DB::table('user')->where('email', $jArr['email'])->get();

            if (count($users)==0) {
                return "true";
            } else
                return "false";
    }
    
    public function doInsert(Request $request,array $jArr)
    {
        /*
         * insert user details from google into our table
         */
        //return $this->isFirstTime($jArr);
        $value=$this->isFirstTime($jArr);

        if($value=="true")
        {
        try {
            DB::beginTransaction();

                $userId = DB::table('user')->insertGetId(
                    ['email' => $jArr['email'], 'password' => sha1($jArr['sub']), 'role_id' => $request->input('role_id')]
                );

            
            if($userId!=null) {

                $data = DB::table('userdetail')->insertGetId(
                    ['id' => $userId, 'name' => $jArr['name'],'photo_path'=>$jArr['picture']]

                );
            }
            else
            {
                DB::rollback();
                return response()->json(['sucess'=>false,'message'=>'unknown error','code'=>520]);
            }



            }catch (Exception $e)
            {
                DB::rollback();
                return response()->json(['sucess'=>false,'message'=>'unknown error','code'=>520]);
            }
             DB::commit();
             return response()->json(['sucess'=>true,'message'=>'google signup','code'=>200,'email'=>$jArr['email'],'password'=>sha1($jArr['sub'])]);
        }
        else 
        {
            /*
             * if user is already the member
             */
            return response()->json(['sucess'=>true,'message'=>'google signin','code'=>200,'email'=>$jArr['email'],'password'=>sha1($jArr['sub'])]);
        }
        
    }
}

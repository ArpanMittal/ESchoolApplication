<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Mockery\CountValidator\Exception;
use Illuminate\Support\Facades\DB;

class SignUpController extends Controller
{
    public function __construct()
    {
        $this->middleware('client');
    }
    //
    public function doSignUp(Request $request)
    {

        try {
            DB::beginTransaction();

            $userId = DB::table('user')->insertGetId(
                ['email' => $request->input('email'), 'password' => $request->input('password'),'role_id'=>$request->input('role_id')]
            );

            if($userId!=null) {

                $data = DB::table('userdetail')->insertGetId(
                    ['id' => $userId, 'name' => $request->input('name')]
                );
            }
            else
            {
                return response()->json(['sucess'=>false,'message'=>'email already exist','code'=>409]);
            }



        }catch (Exception $e)
        {
            DB::rollback();
            return response()->json(['sucess'=>false,'message'=>'unknown error','code'=>520]);
        }
        DB::commit();
        return response()->json(['sucess'=>true,'code'=>200]);
    }


}

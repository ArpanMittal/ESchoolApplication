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

           $data=DB::table('userdetail')->insertGetId(
                ['id'=>$userId,'name'=>$request->input('name')]
            );



        }catch (Exception $e)
        {
            DB::rollback();
            return response()->json(['sucess'=>false,'code'=>422]);
        }
        DB::commit();
        return response()->json(['sucess'=>true,'code'=>200]);
    }


}

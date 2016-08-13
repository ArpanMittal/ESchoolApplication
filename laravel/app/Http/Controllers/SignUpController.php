<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Mockery\CountValidator\Exception;
use Illuminate\Support\Facades\DB;

class SignUpController extends Controller
{
    
    //
    public function isFirstTime($email, $password)
    {
        /*
         * check weather email already exist or not
         */
        $users = DB::table('user')->where('email', $email)->get();

        if (count($users)==0) {
            return "true";
        } else
            return "false";
    }

    public function doSignUp(Request $request)
    {
        $value=$this->isFirstTime($request->input('email'),$request->input('password'));

        if($value=="true")
        {
            try {
                DB::beginTransaction();


                    $userId = DB::table('user')->insertGetId(
                        ['email' => $request->input('email'), 'password' => $request->input('password'), 'role_id' => $request->input('role_id')]
                    );
                    $data = DB::table('userdetail')->insertGetId(
                        ['id' => $userId, 'name' => $request->input('name')]
                    );
            }
            catch (Exception $e) {
                DB::rollback();
                return response()->json(['sucess' => false, 'message' => 'unknown error', 'code' => 520]);
            }
            DB::commit();
            return response()->json(['sucess' => true, 'code' => 200]);
        }
        else
        {
            return response()->json(['sucess' => false, 'message' => 'email already exist', 'code' => 409]);
        }


    }


}

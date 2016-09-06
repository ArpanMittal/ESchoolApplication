<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

class postController extends Controller
{
    //
    public function getAllPost(Request $request)
    {
        return response()->json(["email"=>$request->input("user_id")]);
    }

    public  function getUserAttempt(Request $request)
    {
        $email=$request->input('user_id');
        
        $attempt=DB::table('user_attempt')
            ->select('user_attempt.id')
            ->join('user','user_attempt.user_id',"=","user.id")
            ->where('user.email',$email)->get();
        
        if(!$attempt==0)
        {
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        else
        {
            return Response::json([
                'success' => true,
                'code' => 200,
                'title' => "Attempt",
                'data' => $attempt
            ]);
        }
        

    }
}

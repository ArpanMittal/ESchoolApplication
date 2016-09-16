<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\DB;

class postController extends Controller
{
    //
    public function getAllPost(Request $request)
    {
        $user = DB::table('user')
            ->leftjoin('userdetail','user.id','=','userdetail.id')
            ->leftjoin('school','userdetail.school_id','=','school.id')
            ->where('user.email',$request->input('user_id'))
            ->first();
        return response()->json($user);
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

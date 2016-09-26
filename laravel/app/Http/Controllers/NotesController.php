<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;

class NotesController extends Controller
{
    //
    public function get(Request $request){
        $user = DB::table('user')
            ->where('user.email',$request->input('user_id'))
            ->first();

        $data = DB::table('notes')->where('student_id',$user->id)->get();

        if (!$data){
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        return Response::json([
            'success' => true,
            'code' => 200,
            'data' => $data
        ]);
    }
}

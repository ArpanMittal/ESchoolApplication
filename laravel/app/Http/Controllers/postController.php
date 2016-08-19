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
}

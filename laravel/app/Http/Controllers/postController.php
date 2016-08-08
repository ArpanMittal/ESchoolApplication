<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

class postController extends Controller
{
    //
    public function getAllPost(Request $request)
    {
        $data=[1,2,3,4,$request->input()];
        return response($data,201);
    }
}

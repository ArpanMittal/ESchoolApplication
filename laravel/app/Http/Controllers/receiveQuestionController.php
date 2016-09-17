<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class receiveQuestionController extends Controller
{
    //
    public function receiveQuestion(Request $request,$tag,$key)
    {

        switch ($tag) {
            case 'SamplePaper':

                $data = $this->saveQuestion($key,$request,4);
                break;
            case 'worksheet':

                $data = $this->saveQuestion($key,$request,2);
                break;
            case 'chapter':

                $data = $this->saveQuestion($key,$request,3);
                break;
            case 'practice':

                $data = $this->saveQuestion($key,$request,1);
                break;
        }

        if (!$data){
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        else {
            return Response::json([
                'success' => true,
                'code' => 200,
                'title' => $tag,

                'data' => $data

            ]);
        }
    }

    public function saveQuestion($key,$request,$attemptType)
    {
        $data=\GuzzleHttp\json_decode($request->input('data'))->data;
        $user_email=$request->input('user_id');
//        $user_email="test1@gmail.com";
        $user_id = DB::table('user')
            ->where('user.email',$user_email)
            ->first();


            DB::beginTransaction();

            $attemptId = DB::table('user_attempt')->insertGetId(
                ['user_id' => $user_id->id,'attempt_type_id'=>$attemptType,'included_id'=>$key]
            );
            if (!$attemptId>0) {
                DB::rollback();
                return null;
            }
      
        foreach ($data as $da)
        {

            DB::table('user_attempt_response')->insert(
                ['user_attempt_id'=> $attemptId,
                    'time_taken'=>$da->time_taken,
                    'response'=>$da->is_correct,
                    'question_id'=>$da->question_id,
                   'option_id'=>$da->option_id
                ]
            );

        }

        DB::commit();
        return "sucess";

    }
}

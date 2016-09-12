<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;

class receiveQuestionController extends Controller
{
    //
    public function receiveQuestion(Request $request,$tag,$key)
    {
        switch ($tag) {
            case 'SamplePaper':

                $data = $this->saveSamplePaperQuestion($key);
                break;
        }

        return Response::json([
            'success' => true,
            'code' => 200,
            'data' =>$data

        ]);
    }

    public function saveSamplePaperQuestion($key)
    {
        $data=$_REQUEST['data'];
        $data=DB::table('user_attempt')
            ->where('user_id',$_REQUEST['user_id'])
            ->where('included_id',$_REQUEST['data']);
    }
}

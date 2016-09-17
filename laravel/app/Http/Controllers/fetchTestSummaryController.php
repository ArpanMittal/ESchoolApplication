<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class fetchTestSummaryController extends Controller
{
    /*
     * to get number of attempt
     */
    public function getDetails(Request $request,$tag,$key,$attemptType)
    {


        switch ($tag) {
            case 'Attempt_Number':
                switch ($attemptType){
                    case 'SamplePaper':

                        $data = $this->getAttemptDetails($key,$request,4);
                        break;
                    case 'worksheet':

                        $data = $this->getAttemptDetails($key,$request,2);
                        break;
                    case 'chapter':

                        $data = $this->getAttemptDetails($key,$request,3);
                        break;
                    case 'practice':

                        $data = $this->getAttemptDetails($key,$request,1);
                        break;
                }
                break;


            case 'Attempt_Question':
            {

                $data=$this->getAttemptedQuestionDetails($key);
                break;
            }

            case 'Test_Detail':{
                $data = $this->getData($key);
                break;
            }


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

    /*
     * to show attempted question in review
     */
    public function getTestDetails(Request $request,$key)
    {

        $data = $this->getAttemptedQuestionDetails($key);
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
                'data' => $data

            ]);
        }
    }

    private function getAttemptedQuestionDetails($key)
    {

        $attempt=DB::table('user_attempt_response')
            ->select('user_attempt_response.id as id',
                'user_attempt_response.time_taken as time_taken',
                'user_attempt_response.response as response',
                'user_attempt_response.question_id as question_id',
                'user_attempt_response.option_id as option_id',
                'question.question as question_text',
                'question.image_path as question_image',
                'question.solution_path as solution_path',
                'answer.answer as answer_id',
                'question.difficulty as difficulty'
            )
            ->join('question','user_attempt_response.question_id','=','question.id')
            ->join('answer','question.id','=','answer.question_id')
            ->where('user_attempt_response.user_attempt_id',$key)
            ->get();

        foreach ($attempt as $at)
        {

            $at->correct_option=DB::table('option')
                ->select('option.id as id', 'option.opt as name')
                ->where('option.id',$at->answer_id)->get();

            if(strcmp($at->response,"empty"))
            {

               $at-> user_option=DB::table('option')
                    ->select('option.id as id', 'option.opt as name')
                    ->where('option.id',$at->option_id)->get();

            }
            else
            {
                $at-> user_option=null;
            }

        }

        return $attempt;
    }

    private function getAttemptDetails($key,$request,$attemptType)
    {

        $user_email=$request->input('user_id');
//        $user_email="test1@gmail.com";
        $user_id = DB::table('user')
            ->where('user.email',$user_email)
            ->first();

        $attempt=DB::table('user_attempt')
            ->select('user_attempt.id as id','user_attempt.started_at as time')
            ->where('user_attempt.user_id',$user_id->id)
            ->where('user_attempt.attempt_type_id',$attemptType)
            ->where('user_attempt.included_id',$key)->get();

        return $attempt;

    }

    private function getData($key)
    {
        $accuracygraphdata=$this->getAccuracyData($key);
        return $accuracygraphdata;
    }
    private function getAccuracyData($key)
    {

        $easy_attempt=$this->getEasyQuestionDetails($key);

        $medium_attempt=$this->getMediumQuestionDetails($key);
        $hard_attempt=$this->getDifficultQuestionDetails($key);
        $total_attempt=$this->getTotalQuestionDetails($key);
        $data=array($easy_attempt,$medium_attempt,$hard_attempt,$total_attempt);
        return $data;
    }
    //get details for acccuracy chart
    private function getTotalQuestionDetails($key)
    {
        return $this->helpful($key,[0,10],"totalQuestion");

    }

    private function getDifficultQuestionDetails($key)
    {
        return $this->helpful($key,[8,10],"difficult");

    }

    private function getMediumQuestionDetails($key)
    {
        return $this->helpful($key,[5,7],"medium");

    }

    private function getEasyQuestionDetails($key)
    {
        return $this->helpful($key,[0,4],"easy");

    }

    private function helpful($key,$val11,$title)
    {
        $val=$this->getQuestion($key,$val11,"true");
        if(isset($val))
            $corrrect_attempt=$val->count;
        else
            $corrrect_attempt=0;

        $val=$this->getQuestion($key,$val11,"empty");
        if(isset($val))
            $unattempted_question=$val->count;
        else
            $unattempted_question=0;


        $val=$this->getTotalQuestion($key,$val11);

        if(isset($val))
            $total_attempt=$val->count;
        else
            $total_attempt=0;

        $attempted_question=$total_attempt-$unattempted_question;
        $data=array('correct_attempt'=>$corrrect_attempt, 'attempt_question'=>$attempted_question, 'total_question'=>$total_attempt,'title'=>$title);
        return $data;
    }

    //fetch details

    private function getQuestion($key,$val,$val2)
    {
        $attempt=DB::table('user_attempt_response')
            ->select(DB::raw('count(*) as count'))
            ->join('question','user_attempt_response.question_id','=','question.id')
            ->where('user_attempt_response.user_attempt_id',$key)
            ->whereBetween('question.difficulty',$val)
            ->groupBy('user_attempt_response.response')
            ->having('user_attempt_response.response','=',$val2)
            ->first();
        return $attempt;
    }

    private function getTotalQuestion($key,$val)
    {
        $attempt=DB::table('user_attempt_response')
            ->select(DB::raw('count(*) as count'))
            ->join('question','user_attempt_response.question_id','=','question.id')
            ->where('user_attempt_response.user_attempt_id',$key)
            ->whereBetween('question.difficulty',$val)
            ->first();
        return $attempt;
    }
}

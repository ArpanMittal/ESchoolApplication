<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class fetchQuestionController extends Controller
{

    //
    public function getQuestion(Request $request,$tag,$key)
    {
        $user = DB::table('user')
            ->where('user.email',$request->input('user_id'))
            ->first();

        switch ($tag) {
            case 'SamplePaper':
                
                $data = $this->getSamplePaperQuestion($key);
                break;
            case 'worksheet':

                $data = $this->getWorksheetQuestion($key);
                break;
            case 'chapter':

                $data = $this->getChapterQuestion($key);
                break;
            case 'practice':

                $data = $this->getPracticeQuestion($key,$user);
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

    private function getSamplePaperQuestion($key)
    {

        $questions= DB::table('question')
           ->select('question.id as id',
               'question.hash as hash',
               'question.question_type_id as type_id',
                'question.question as question_text',
               'question.solution_path as solution_path',
               'question.difficulty as difficulty',
                'question.image_path as question_image_path',
                'answer.answer as answer')
            ->join("questiontags","question.id","=","questiontags.question_id")
            ->join('answer','answer.question_id','=','question.id')
            ->where('questiontags.tag_id',$key)->get();

        foreach ($questions as $question )
        {
            if($question->type_id==1)
                $question->option=$this->getOption($question->id);
        }
        return $questions;

    }

    private function getOption($id)
    {
        return DB::table('option')
            ->select('option.id as id',
                'option.opt as option')
            ->where('option.question_id',$id)->get();

    }

    private function getWorksheetQuestion($key)
    {
        $questions= DB::table('question')
            ->select('question.id as id',
                'question.hash as hash',
                'question.question_type_id as type_id',
                'question.question as question_text',
                'question.solution_path as solution_path',
                'question.difficulty as difficulty',
                'question.image_path as question_image_path',
                'answer.answer as answer')
            ->join('answer','answer.question_id','=','question.id')
//            ->where('question.hash',$key)
            ->orderBy(DB::raw('RAND()'))
            ->take(8)
            ->get();

        foreach ($questions as $question )
        {
            if($question->type_id==1)
                $question->option=$this->getOption($question->id);
        }
        return $questions;
    }

    private function getChapterQuestion($key)
    {
        $questions= DB::table('question')
            ->select('question.id as id',
                'question.hash as hash',
                'question.question_type_id as type_id',
                'question.question as question_text',
                'question.solution_path as solution_path',
                'question.difficulty as difficulty',
                'question.image_path as question_image_path',
                'answer.answer as answer')
            ->join('answer','answer.question_id','=','question.id')
//            ->where('question.hash',"LIKE",$key."%")
            ->orderBy(DB::raw('RAND()'))
            ->take(30)
            ->get();

        foreach ($questions as $question )
        {
            if($question->type_id==1)
                $question->option=$this->getOption($question->id);
        }
        return $questions;
    }

    private function getPracticeQuestion($key, $user)
        {
        $noq = DB::table('exam_pattern_map')
                ->where('exam_id',$key)
                ->get();
        $tags = DB::table('exam_state_year_rest_map')
            ->select('exam_state_year_rest_map.id as id')
            ->join('exam_state_year_map','exam_state_year_rest_map.exam_state_year_id','=','exam_state_year_map.id')
            ->join('exam_state_map','exam_state_year_map.exam_state_id','=','exam_state_map.id')
            ->where('exam_state_year_rest_map.is_active',"1")
            ->where('exam_state_map.exam_id',$key)
            ->get();

        $subs = DB::table('order')
            ->leftjoin('orderpackmap','order.id','=','orderpackmap.order_id')
            ->leftjoin('exampackmap','orderpackmap.pack_id','=','exampackmap.pack_id')
            ->where('order.user_id',$user->id)
            ->where('exampackmap.exam_id',$key)
            ->first();
        $data = array();
        if (count($tags)<=0){
            return $data;
        }
        foreach ($noq as $item){
            $temp = DB::table('question')
                ->select('question.id as id',
                    'question.hash as hash',
                    'question.question_type_id as type_id',
                    'question.question as question_text',
                    'question.solution_path as solution_path',
                    'question.difficulty as difficulty',
                    'question.image_path as question_image_path',
                    'answer.answer as answer')
                ->join("questiontags","question.id","=","questiontags.question_id")
                ->join('answer','answer.question_id','=','question.id')
                ->where('question.hash',"LIKE",$item->subject_id."%")
                ->where(function($query) use ($tags){
                    for ($i=0;$i<count($tags);$i++){
                        $query->orWhere('questiontags.tag_id',$tags[$i]->id);
                    }
                });
                if (isset($subs)){
                    $temp->orderBy(DB::raw('RAND()'));
                }else{
                    $temp->orderBy('question.id','ASC');
                }

                $temp = $temp->take($item->no_of_questions)
                ->get();
            

            foreach ($temp as $question )
            {
                if($question->type_id==1)
                    $question->option=$this->getOption($question->id);
            }

            $data = array_merge($data, $temp);
        }
        return $data;
    }


}

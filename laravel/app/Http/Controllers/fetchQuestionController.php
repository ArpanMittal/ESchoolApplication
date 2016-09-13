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
            ->where('question.hash',$key)
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
            ->where('question.hash',"LIKE",$key."%")
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


}

<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Input;

class QuestionController extends Controller
{
    //

    public function getAllQuestionList(Request $request)
    {
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;

        $question = Input::get('Question');
        $subject = Input::get('SubjectId');
        $data['selected_subject'] = $subject;

        if ($user->role_id == 1){
            $query = DB::table('question');
        }else {
            $query = DB::table('question');
            $query->where('created_by','=',$id);
        }


        if(isset($question) && $question!= '') {
            $query->where('question','LIKE','%'.$question.'%');
        }
        if(isset($subject) && $subject != '') {
            $query->where('hash','LIKE',$subject.'%');
        }

        $data['question_list'] = $query->select('id','hash','question','question_type_id')
                                        ->get();

        for($i=0;$i<count($data['question_list']); $i++){
            $hash = $data['question_list'][$i]->hash;
            $class_id = substr($hash,0,3);
            $subject_id = substr($hash,3,2);
            $chapter_id = substr($hash,7,3);
            $topic_id = substr($hash,10,3);
            $question_type_id = $data['question_list'][$i]->question_type_id;

            $class = DB::table('class')->whereId($class_id)->first();
            $data['question_list'][$i]->class_name = $class->class_name;

            $subject = DB::table('subject')->whereId($subject_id)->first();
            $data['question_list'][$i]->subject_name = $subject->subject_name;

            $chapter = DB::table('chapter')->whereId($chapter_id)->first();
            $data['question_list'][$i]->chapter_name = $chapter->chapter_name;

            $topic = DB::table('topic')->whereId($topic_id)->first();
            $data['question_list'][$i]->topic_name = $topic->topic_name;

            $questiontype = DB::table('questiontype')->whereId($question_type_id)->first();
            $data['question_list'][$i]->question_type = $questiontype->question_type;
        }
        $data['subjects'] = DB::table('classsubjectmap')
            ->join('class', 'classsubjectmap.class_id', '=', 'class.id')
            ->join('subject', 'classsubjectmap.subject_id', '=', 'subject.id')
            ->get();
        return view('question.list',$data);
    }
}

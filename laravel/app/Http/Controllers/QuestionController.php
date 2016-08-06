<?php

namespace App\Http\Controllers;

use Exception;
use Faker\Provider\Image;
use Illuminate\Contracts\Validation\ValidationException;
use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Input;
use PDOException;

class QuestionController extends Controller
{
    //
    public function getNewQuestion(Request $request)
    {
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;

        $data['subjects'] = DB::table('classsubjectmap')
            ->join('class', 'classsubjectmap.class_id', '=', 'class.id')
            ->join('subject', 'classsubjectmap.subject_id', '=', 'subject.id')
            ->get();

        $data['types'] = DB::table('questiontype')->get();

        return view('question.new',$data);
    }

    public function addQuestion(Request $request)
    {
        $teacherId = Input::get('TeacherId');
        $hash = Input::get("TopicId");
        $question_type = Input::get("QuesType");
        $question= Input::get("question");
        $options = Input::get("Opt");
        $correct_option = Input::get("optionsRadioG");
        $ideal_time = Input::get('ideal_time');
        $level = Input::get("Level");
        $question_image = "";
        $solution_image = "";
        try{
            DB::beginTransaction();
            $questionId = DB::table('question')->insertGetId(
                ['hash'=>$hash,'question' =>$question, 'image_path' => $question_image,'question_type_id' => $question_type,'created_by' => $teacherId,'solution_path' => $solution_image,'difficulty' => $level, 'ideal_attempt_time' => $ideal_time]
            );
            if (!$questionId>0){
                DB::rollback();
                $result['success'] = 'false';
                $result['error'] = 'Error in filling question';
                return json_encode($result);
            }
            if(Input::hasFile('question_diagram'))
            {
                $image = Input::file('question_diagram');
                $question_image  = $questionId . '.' . $image->getClientOriginalExtension();
                $directory = 'content/'.$hash.'/question';
                $path = public_path($directory);

                Input::file('question_diagram')->move($path,$question_image);

                DB::table('question')->where('id',$questionId)
                                    ->update(['image_path' => $directory."/".$question_image]);
            }
            if(Input::hasFile('solution'))
            {

                $image = Input::file('solution');
                $solution_image  = $questionId . '.' . $image->getClientOriginalExtension();
                $directory = 'content/' .$hash.'/solution';
                $path = public_path($directory);

                Input::file('solution')->move($path,$solution_image);
                DB::table('question')->whereId($questionId)
                        ->update(['solution_path' => $directory."/".$solution_image]);
            }
            $correct_option_id=0;
            for ($i=0;$i<count($options);$i++){
                if ($i == $correct_option-1){
                    $correct_option_id = DB::table('option')->insertGetId(
                        ['question_id' => $questionId, 'opt' => $options[$i]]
                    );
                }else{
                    DB::table('option')->insert(
                        ['question_id' => $questionId, 'opt' => $options[$i]]
                    );
                }
            }
            if (!$correct_option>0){
                // Back to form with errors
                DB::rollback();
                $result['success'] = 'false';
                $result['error'] = 'Error in filling options';
                return json_encode($result);
            }

            $answerId= DB::table('answer')->insert(
                ['question_id' => $questionId, 'answer'=> $correct_option_id]
            );
            if (!$answerId>0){
                DB::rollback();
                $result['success'] = 'false';
                $result['error'] = 'Error in filling correct option';
                return json_encode($result);
            }
            DB::commit();
        } catch(Exception $e)
        {
            // Back to form with errors
            DB::rollback();
            $result['success'] = 'false';
            $result['error'] = $e->getTraceAsString();
            return json_encode($result);
        }


        $result['success'] = 'true';
        return json_encode($result);
    }

    public function getChapters(Request $request)
    {
        $subject = Input::get('SubjectId');
        return DB::table('streamchaptermap')
            ->join('chapter', 'streamchaptermap.chapter_id', '=', 'chapter.id')
            ->where('streamchaptermap.cl_su_st_id','LIKE' ,$subject.'%')
            ->get();
    }

    public function getTopics(Request $request)
    {
        $chapter = Input::get('ChapterId');
        return DB::table('chaptertopicmap')
            ->join('topic', 'chaptertopicmap.topic_id', '=', 'topic.id')
            ->where('chaptertopicmap.cl_su_st_ch_id' ,'LIKE',$chapter.'%')
            ->get();
    }

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

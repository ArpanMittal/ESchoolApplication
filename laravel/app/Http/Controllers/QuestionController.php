<?php

namespace App\Http\Controllers;

use Exception;
use Faker\Provider\Image;
use Illuminate\Contracts\Validation\ValidationException;
use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Redirect;
use PDOException;

class QuestionController extends Controller
{
    //
    public function getNewQuestion(Request $request)
    {
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;

        $data['subjects'] = DB::table('operatorsubjectmap')
            ->join('classsubjectmap', 'operatorsubjectmap.subject_id', '=', 'classsubjectmap.subject_id')
            ->join('subject', 'operatorsubjectmap.subject_id', '=', 'subject.id')
            ->join('class', 'classsubjectmap.class_id', '=', 'class.id')
            ->where('operatorsubjectmap.user_id','=',$id)
            ->get();

        $data['types'] = DB::table('questiontype')->get();

        $data['tags'] = DB::table('exam_state_year_rest_map')
            ->select('exam_state_year_rest_map.id as id',
                DB::raw('CONCAT(examtag.exam_name,\' \',state.state_name, \' \', year.year_name, \' \', rest_part.rest) as exam_name'))
            ->join('exam_state_year_map','exam_state_year_rest_map.exam_state_year_id','=','exam_state_year_map.id')
            ->join('exam_state_map','exam_state_year_map.exam_state_id','=','exam_state_map.id')
            ->join('examtag','exam_state_map.exam_id','=','examtag.id')
            ->join('state','exam_state_map.state_id','=','state.id')
            ->join('year','exam_state_year_map.year_id','=','year.id')
            ->join('rest_part','exam_state_year_rest_map.rest_id','=','rest_part.id')
            ->get();
        
        return view('question.new',$data);
    }

    public function nextQuestion(Request $request)
    {
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $questionId = Input::get('QuestionId');
        
        if ($user->role_id == 1){
            $query = DB::table('question');
        }else {
            $query = DB::table('question');
            $query->where('created_by','=',$id);
        }
        
        if (isset($questionId)){
            $query->where('id','>',$questionId);
        }

        $nextId = $query->select('id')
            ->orderBy('id', 'asc')
            ->first();
        if (isset($nextId)){
            return Redirect::to('/question/'.$nextId->id);
        }
        return Redirect::to('/question/list');
    }

    public function editQuestion(Request $request,$id){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;

        $data['question'] = DB::table('question')->where('id',$id)->first();
        $data['options'] = DB::table('option')->where('question_id',$id)->get();
        $data['answer'] = DB::table('answer')->where('question_id',$id)->get();
        $data['seleted_tags'] = DB::table('questiontags')->where('question_id',$id)->get();
        
        $data['tags'] = DB::table('exam_state_year_rest_map')
            ->select('exam_state_year_rest_map.id as id',
                DB::raw('CONCAT(examtag.exam_name,\' \',state.state_name, \' \', year.year_name, \' \', rest_part.rest) as exam_name'))
            ->join('exam_state_year_map','exam_state_year_rest_map.exam_state_year_id','=','exam_state_year_map.id')
            ->join('exam_state_map','exam_state_year_map.exam_state_id','=','exam_state_map.id')
            ->join('examtag','exam_state_map.exam_id','=','examtag.id')
            ->join('state','exam_state_map.state_id','=','state.id')
            ->join('year','exam_state_year_map.year_id','=','year.id')
            ->join('rest_part','exam_state_year_rest_map.rest_id','=','rest_part.id')
            ->get();

        $data['subjects'] = DB::table('classsubjectmap')
            ->join('class', 'classsubjectmap.class_id', '=', 'class.id')
            ->join('subject', 'classsubjectmap.subject_id', '=', 'subject.id')
            ->get();

        $hash = $data['question']->hash;

        $subject_id = substr($hash,0,5);
        $chapter_id = substr($hash,0,10);

        $data['chapters'] = DB::table('streamchaptermap')
            ->join('chapter', 'streamchaptermap.chapter_id', '=', 'chapter.id')
            ->join('subjectstreammap', 'streamchaptermap.cl_su_st_id', '=', 'subjectstreammap.cl_su_st_id')
            ->join('stream', 'subjectstreammap.stream_id', '=', 'stream.id')
            ->where('streamchaptermap.cl_su_st_id','LIKE' ,$subject_id.'%')
            ->get();

        $data['topics'] = DB::table('chaptertopicmap')
            ->join('topic', 'chaptertopicmap.topic_id', '=', 'topic.id')
            ->where('chaptertopicmap.cl_su_st_ch_id' ,'LIKE',$chapter_id.'%')
            ->get();
        $data['selected_subject'] = $subject_id;
        $data['selected_chapter'] = $chapter_id;
        $data['types'] = DB::table('questiontype')->get();
        return view('question.new',$data);
    }

    public function viewQuestion(Request $request,$id){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;

        $data['question'] = DB::table('question')->where('id',$id)->first();
        $data['options'] = DB::table('option')->where('question_id',$id)->get();
        $data['answer'] = DB::table('answer')->where('question_id',$id)->get();
        $data['tags'] = DB::table('questiontags')
            ->select('exam_state_year_rest_map.id as id',
                DB::raw('CONCAT(examtag.exam_name,\' \',state.state_name, \' \', year.year_name, \' \', rest_part.rest) as exam_name'))
            ->join('exam_state_year_rest_map','questiontags.tag_id','=','exam_state_year_rest_map.id')
            ->join('exam_state_year_map','exam_state_year_rest_map.exam_state_year_id','=','exam_state_year_map.id')
            ->join('exam_state_map','exam_state_year_map.exam_state_id','=','exam_state_map.id')
            ->join('examtag','exam_state_map.exam_id','=','examtag.id')
            ->join('state','exam_state_map.state_id','=','state.id')
            ->join('year','exam_state_year_map.year_id','=','year.id')
            ->join('rest_part','exam_state_year_rest_map.rest_id','=','rest_part.id')
            ->where('questiontags.question_id',$id)
            ->get();

        $hash = $data['question']->hash;

        $subject_id = substr($hash,3,2);
        $stream_id = substr($hash,5,2);
        $chapter_id = substr($hash,7,3);
        $topic_id = substr($hash,10,3);

        $data['subject'] = DB::table('subject')
            ->where('id', $subject_id)
            ->first();

        $data['stream'] = DB::table('stream')
            ->where('id', $stream_id)
            ->first();

        $data['chapter'] = DB::table('chapter')
            ->where('id', '=', $chapter_id)
            ->first();

        $data['topic'] = DB::table('topic')
            ->where('id', '=', $topic_id)
            ->first();
        $data['questype'] = DB::table('questiontype')
            ->where('id', $data['question']->question_type_id)
            ->first();

        return view('question.view',$data);
    }

    public function viewNextQuestion(Request $request) {

        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $questionId = Input::get('QuestionId');

        if ($user->role_id == 1){
            $query = DB::table('question');
        }else {
            $query = DB::table('question');
            $query->where('created_by','=',$id);
        }

        if (isset($questionId)){
            $query->where('id','>',$questionId);
        }

        $nextId = $query->select('id')
            ->orderBy('id', 'asc')
            ->first();
        if (isset($nextId)){
            return Redirect::to('/question/view/'.$nextId->id);
        }
        return Redirect::to('/question/list');
    }

    public function addQuestion(Request $request)
    {
        $teacherId = Input::get('TeacherId');
        $hash = Input::get("TopicId");
        $question_type = Input::get("QuesType");
        $question= Input::get("question");
        $options = Input::get("Opt");
        $tags = Input::get("TagIds");
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
                $error = 'Error in filling question';
                return $this->gotoView($request,$questionId,$hash,$error);

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
            for ($i=0;$i<count($tags);$i++){
                $status = DB::table('questiontags')
                    ->insert([
                        'question_id' => $questionId,
                        'tag_id' => $tags[$i]
                    ]);
                if ($status==false){
                    // Back to form with errors
                    DB::rollback();
                    $error = 'Error in filling tags'.$i;
                    return $this->gotoView($request,$questionId,$hash,$error);

                }
            }
            if (!$correct_option>0){
                // Back to form with errors
                DB::rollback();
                $error = 'Error in filling options';
                return $this->gotoView($request,$questionId,$hash,$error);
            }

            $answerId= DB::table('answer')->insert(
                ['question_id' => $questionId, 'answer'=> $correct_option_id]
            );
            if (!$answerId>0){
                DB::rollback();
                $error= 'Error in filling correct option';
                return $this->gotoView($request,$questionId,$hash,$error);
            }
            DB::commit();
        } catch(Exception $e)
        {
            // Back to form with errors
            DB::rollback();
            return $this->gotoView($request,$questionId,$hash,$e->getTraceAsString());
        }


        return $this->gotoView($request,$questionId,$hash,"Done");

        
    }
    
    private function gotoView($request,$questionId,$hash,$result){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;
        if(isset($questionId)){
            $data['seleted_tags'] = DB::table('questiontags')->where('question_id',$questionId)->get();
        }

        $data['tags'] = DB::table('exam_state_year_rest_map')
            ->select('exam_state_year_rest_map.id as id',
                DB::raw('CONCAT(examtag.exam_name,\' \',state.state_name, \' \', year.year_name, \' \', rest_part.rest) as exam_name'))
            ->join('exam_state_year_map','exam_state_year_rest_map.exam_state_year_id','=','exam_state_year_map.id')
            ->join('exam_state_map','exam_state_year_map.exam_state_id','=','exam_state_map.id')
            ->join('examtag','exam_state_map.exam_id','=','examtag.id')
            ->join('state','exam_state_map.state_id','=','state.id')
            ->join('year','exam_state_year_map.year_id','=','year.id')
            ->join('rest_part','exam_state_year_rest_map.rest_id','=','rest_part.id')
            ->get();

        $data['subjects'] = DB::table('classsubjectmap')
            ->join('class', 'classsubjectmap.class_id', '=', 'class.id')
            ->join('subject', 'classsubjectmap.subject_id', '=', 'subject.id')
            ->get();

        $data['hash'] = $hash;
        $subject_id = substr($hash,0,5);
        $chapter_id = substr($hash,0,10);

        $data['chapters'] = DB::table('streamchaptermap')
            ->join('chapter', 'streamchaptermap.chapter_id', '=', 'chapter.id')
            ->join('subjectstreammap', 'streamchaptermap.cl_su_st_id', '=', 'subjectstreammap.cl_su_st_id')
            ->join('stream', 'subjectstreammap.stream_id', '=', 'stream.id')
            ->where('streamchaptermap.cl_su_st_id','LIKE' ,$subject_id.'%')
            ->get();

        $data['topics'] = DB::table('chaptertopicmap')
            ->join('topic', 'chaptertopicmap.topic_id', '=', 'topic.id')
            ->where('chaptertopicmap.cl_su_st_ch_id' ,'LIKE',$chapter_id.'%')
            ->get();
        $data['selected_subject'] = $subject_id;
        $data['selected_chapter'] = $chapter_id;
        $data['types'] = DB::table('questiontype')->get();
        $data['result'] = $result;
        return view('question.new',$data);
    }

    public function updateQuestion(Request $request)
    {
        $questionId = Input::get('QuestionId');
        $optionid = Input::get('OptId');
        $teacherId = Input::get('TeacherId');
        $hash = Input::get("TopicId");
        $question_type = Input::get("QuesType");
        $question= Input::get("question");
        $tags = Input::get("TagIds");
        $options = Input::get("Opt");
        $correct_option = Input::get("optionsRadioG");
        $ideal_time = Input::get('ideal_time');
        $level = Input::get("Level");
        if (!isset($hash) || $hash ==null || $hash==""){
            $error = 'Error in selecting topic';
            return redirect('question/'.$questionId)->with('status', $error);
        }
        try{
            DB::beginTransaction();
            $status =0;
            $status += DB::table('question')->where('id',$questionId)
                ->update([
                    'hash'              =>  $hash,
                    'question'          =>  $question,
                    'question_type_id'  =>  $question_type,
                    'modified_by'        =>  $teacherId,
                    'difficulty'        =>  $level,
                    'ideal_attempt_time'=> $ideal_time
                ]);
            if(Input::hasFile('question_diagram'))
            {
                $image = Input::file('question_diagram');
                $question_image  = $questionId . '.' . $image->getClientOriginalExtension();
                $directory = 'content/'.$hash.'/question';
                $path = public_path($directory);

                Input::file('question_diagram')->move($path,$question_image);

                $status +=DB::table('question')->where('id',$questionId)
                    ->update(['image_path' => $directory."/".$question_image]);
            }
            if(Input::hasFile('solution'))
            {

                $image = Input::file('solution');
                $solution_image  = $questionId . '.' . $image->getClientOriginalExtension();
                $directory = 'content/' .$hash.'/solution';
                $path = public_path($directory);

                Input::file('solution')->move($path,$solution_image);
                $status +=DB::table('question')->whereId($questionId)
                    ->update(['solution_path' => $directory."/".$solution_image]);
            }
            for ($i=0;$i<count($options);$i++){
                $status += DB::table('option')->where('id',$optionid[$i])
                    ->update([
                        'opt' => $options[$i]
                    ]);
            }
            DB::table('questiontags')->where('question_id','=',$questionId)->delete();
            for ($i=0;$i<count($tags);$i++){
                $status += DB::table('questiontags')
                    ->insert([
                        'question_id' => $questionId,
                        'tag_id' => $tags[$i]
                    ]);
            }
            if ($status <1){
                DB::rollback();
                $error = 'error';
                return redirect('question/'.$questionId)->with('status', $error);
            }
            DB::table('answer')->where('question_id','=', $questionId)->delete();
            $answerId= DB::table('answer')->insert(
                ['question_id' => $questionId, 'answer'=> $optionid[$correct_option-1]]
            );
            if ($answerId == 0){
                DB::rollback();
                $error = 'Error in filling correct option';
                return redirect('question/'.$questionId)->with('status', $error);
            }
            DB::commit();
        } catch(Exception $e)
        {
            // Back to form with errors
            DB::rollback();
            return redirect('question/'.$questionId)->with('status', $e->getTraceAsString());
        }

        return redirect('question/'.$questionId)->with('status', 'Done');
    }

    public function getChapters(Request $request)
    {
        $subject = Input::get('SubjectId');
        return DB::table('streamchaptermap')
            ->join('chapter', 'streamchaptermap.chapter_id', '=', 'chapter.id')
            ->join('subjectstreammap', 'streamchaptermap.cl_su_st_id', '=', 'subjectstreammap.cl_su_st_id')
            ->join('stream', 'subjectstreammap.stream_id', '=', 'stream.id')
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
        $data['selected_question'] = $question;
        $subject = Input::get('SubjectId');
        $data['selected_subject'] = $subject;
        $topic = Input::get('TopicId');
        $data['selected_topic'] = $topic;
        $tag = Input::get('TagId');
        $data['selected_tag'] = $tag;

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
        if(isset($topic) && $topic != '') {
            $query->where('hash','LIKE',$topic);
        }
        if(isset($tag) && $tag != '') {
            $query->join('questiontags','question.id','=','questiontags.question_id');
            //$query->join('exam_state_year_rest_map','questiontags.tag_id','=','exam_state_year_rest_map.question_id');
            $query->where('questiontags.tag_id','LIKE',$tag);
        }

        $data['question_list'] = $query->select('id','hash','question','question_type_id')
                                        ->orderBy('id', 'asc')
                                        ->get();

        for($i=0;$i<count($data['question_list']); $i++){
            $hash = $data['question_list'][$i]->hash;
            $class_id = substr($hash,0,3);
            $subject_id = substr($hash,3,2);
            $stream_id = substr($hash,5,2);
            $chapter_id = substr($hash,7,3);
            $topic_id = substr($hash,10,3);
            $question_type_id = $data['question_list'][$i]->question_type_id;

            $class = DB::table('class')->whereId($class_id)->first();
            $data['question_list'][$i]->class_name = $class->class_name;

            $subject = DB::table('subject')->whereId($subject_id)->first();
            $data['question_list'][$i]->subject_name = $subject->subject_name;

            $stream = DB::table('stream')->whereId($stream_id)->first();
            $data['question_list'][$i]->stream_name = $stream->stream_name;

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
        $data['topics'] = DB::table('chaptertopicmap')
            ->join('streamchaptermap', 'chaptertopicmap.cl_su_st_ch_id', '=', 'streamchaptermap.cl_su_st_ch_id')
            ->join('chapter', 'streamchaptermap.chapter_id', '=', 'chapter.id')
            ->join('topic', 'chaptertopicmap.topic_id', '=', 'topic.id')
            ->get();
        $data['tags'] = DB::table('exam_state_year_rest_map')
            ->select('exam_state_year_rest_map.id as id',
                DB::raw('CONCAT(examtag.exam_name,\' \',state.state_name, \' \', year.year_name, \' \', rest_part.rest) as exam_name'))
            ->join('exam_state_year_map','exam_state_year_rest_map.exam_state_year_id','=','exam_state_year_map.id')
            ->join('exam_state_map','exam_state_year_map.exam_state_id','=','exam_state_map.id')
            ->join('examtag','exam_state_map.exam_id','=','examtag.id')
            ->join('state','exam_state_map.state_id','=','state.id')
            ->join('year','exam_state_year_map.year_id','=','year.id')
            ->join('rest_part','exam_state_year_rest_map.rest_id','=','rest_part.id')
            ->get();
        return view('question.list',$data);
    }

    public function difference(Request $request)
    {
        $pre = microtime(true);
        $data['topic_list'] = DB::table('chaptertopicmap')
            ->select('hash')
//            ->where('chaptertopicmap.hash','=','C02AcHyOhkAfp')
            ->get();

        for($i=0;$i<count($data['topic_list']); $i++){
            $hash = $data['topic_list'][$i]->hash;
            $class_id = substr($hash,0,3);
            $subject_id = substr($hash,3,2);
            $stream_id = substr($hash,5,2);
            $chapter_id = substr($hash,7,3);
            $topic_id = substr($hash,10,3);

            $class = DB::table('class')->whereId($class_id)->first();
            $data['topic_list'][$i]->class_name = $class->class_name;

            $subject = DB::table('subject')->whereId($subject_id)->first();
            $data['topic_list'][$i]->subject_name = $subject->subject_name;

            $stream = DB::table('stream')->whereId($stream_id)->first();
            $data['topic_list'][$i]->chapter_name = $stream->stream_name;

            $chapter = DB::table('chapter')->whereId($chapter_id)->first();
            $data['topic_list'][$i]->chapter_name = $chapter->chapter_name;

            $topic = DB::table('topic')->whereId($topic_id)->first();
            $data['topic_list'][$i]->topic_name = $topic->topic_name;

        }
        $now = microtime(true);
        $diff1 = $now - $pre;
        echo "Hashins time (micro sec)".$diff1.'<br />';

        $pre = microtime(true);
        $data['topic_list'] = DB::table('chaptertopicmap')
            ->join('streamchaptermap','chaptertopicmap.cl_su_st_ch_id','=','streamchaptermap.cl_su_st_ch_id')
            ->join('subjectstreammap','streamchaptermap.cl_su_st_id','=','subjectstreammap.cl_su_st_id')
            ->join('classsubjectmap','subjectstreammap.cl_su_id','=','classsubjectmap.cl_su_id')
            ->join('class','classsubjectmap.class_id','=','class.id')
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('stream','subjectstreammap.stream_id','=','stream.id')
            ->join('chapter','streamchaptermap.chapter_id','=','chapter.id')
            ->join('topic','chaptertopicmap.topic_id','=','topic.id')
            ->select('hash','class.*','subject.*','stream.*','chapter.*','topic.*')
//            ->where('chaptertopicmap.hash','=','C02AcHyOhkAfp')
            ->get();

        $now = microtime(true);
        $diff2 = $now - $pre;
        echo "Joining time (micro sec)".$diff2.'<br />';
        
        $diff = $diff2 - $diff1;
        echo 'Difference in time taken ::'.$diff.'<br />';
        return ;
    }
    
    
}

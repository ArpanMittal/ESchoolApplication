<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Redirect;

class ContentController extends Controller
{
    //
    public function getAllClassList(Request $request){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;

        $data['content_class_list'] = $this->getClasses();
        return view('content.list',$data);
    }

    public function getSubjectList(Request $request,$id){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;
        $data['selected']=DB::table('class')->whereId($id)->first();

        $data['content_class_subject_list'] = $this->getSubject($id);
        return view('content.sublist',$data);
    }

    public function getChapterList(Request $request,$id){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;
        $data['selected']=DB::table('classsubjectmap')->where('cl_su_id',$id)
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('class','classsubjectmap.class_id','=','class.id')
            ->first();

        $data['content_cl_su_st_ch_list'] = $this->getChapter($id);
        return view('content.chplist',$data);
    }

    public function getUploadPage(Request $request,$id){
        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;
        $data['selected']=DB::table('streamchaptermap')->where('cl_su_st_ch_id',$id)
            ->join('subjectstreammap','streamchaptermap.cl_su_st_id','=','subjectstreammap.cl_su_st_id')
            ->join('classsubjectmap','subjectstreammap.cl_su_id','=','classsubjectmap.cl_su_id')
            ->join('chapter','streamchaptermap.chapter_id','=','chapter.id')
            ->join('stream','subjectstreammap.stream_id','=','stream.id')
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('class','classsubjectmap.class_id','=','class.id')
            ->first();

        $data['contents'] = $this->getTopic($id);
        return view('content.upload',$data);
    }

    public function receive(Request $request){
        $base64 =  substr(Input::get('data'),13);
        $binary=base64_decode($base64);
        $filepath = 'content/temp/' . Input::get('fname');
        if(Input::get("start") == 0  && file_exists ( $filepath)){
            unlink($filepath);
        }
        $file = fopen($filepath, 'ab');
        if ($file !== false) {

            if(filesize($filepath)!=Input::get("start")){
                echo "error not same size";
            }
            fwrite($file, $binary);
            fclose($file);
            echo Input::get("start");
        } else {
            echo "error in creating file";
        }
    }

    public function insert(Request $request,$id){

        $topics = DB::table('chaptertopicmap')
            ->where('chaptertopicmap.cl_su_st_ch_id',$id)
            ->get();
        foreach ($topics as $topic){
            if ($request->has($topic->hash."_pdf")){
                $filename = str_replace("C:\\fakepath\\","",$request->get($topic->hash."_pdf"));
                $filepath = 'content/temp';
                $destination = 'content/'.$topic->hash.'/document';
                if (!file_exists($destination)) {
                    mkdir($destination, 0777, true);
                }
                rename ( $filepath."/".$filename , $destination."/".$filename );
                $pdf_check = md5_file($destination."/".$filename);
                $status = DB::table('content')
                    ->where('hash',$topic->hash)
                    ->first();
                if (!$status){
                    DB::table('content')->insert([
                            'hash' => $topic->hash,
                            'pdf_path' => $destination."/".$filename,
                            'pdf_hash' => $pdf_check
                        ]);
                }else{
                    if( file_exists ( $status->pdf_path)){
                        unlink($status->pdf_path);
                    }
                    DB::table('content')->where('id',$status->id)
                        ->update([
                        'hash' => $topic->hash,
                        'pdf_path' => $destination."/".$filename,
                        'pdf_hash' => $pdf_check
                        ]);
                }

            }

            if ($request->has($topic->hash."_vid")){
                $filename = str_replace("C:\\fakepath\\","",$request->get($topic->hash."_vid"));
                $filepath = 'content/temp';
                $destination = 'content/'.$topic->hash.'/video';
                if (!file_exists($destination)) {
                    mkdir($destination, 0777, true);
                }
                rename ( $filepath."/".$filename , $destination."/".$filename );
                $status = DB::table('content')
                    ->where('hash',$topic->hash)
                    ->first();
                if (!$status){
                    DB::table('content')->insert([
                        'hash' => $topic->hash,
                        'video_path' => $destination."/".$filename
                    ]);
                }else{
                    if( file_exists ( $status->video_path)){
                        unlink($status->video_path);
                    }
                    DB::table('content')->where('id',$status->id)
                        ->update([
                            'hash' => $topic->hash,
                            'video_path' => $destination."/".$filename
                        ]);
                }
            }
        }
        return Redirect::to("content/add/".$id);
    }

    private function getClasses(){
        return DB::table('class')
            ->select('class.id as id',
                DB::raw('CONCAT(\'class \',class.class_name) as name'),
                DB::raw('CONCAT(\'No. of subjects \',count(DISTINCT classsubjectmap.cl_su_id)) as summery'))
            ->leftjoin('classsubjectmap','class.id','=','classsubjectmap.class_id')
            ->groupBy('class.id')
            ->get();
    }

    private function getSubject($id){
        return DB::table('classsubjectmap')
            ->select('classsubjectmap.cl_su_id as id',
                'subject.subject_name as name',
                DB::raw('CONCAT(\'No. of chapters \',count(DISTINCT streamchaptermap.cl_su_st_ch_id)) as summery'))
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('subjectstreammap','classsubjectmap.cl_su_id','=','subjectstreammap.cl_su_id')
            ->join('streamchaptermap','subjectstreammap.cl_su_st_id','=','streamchaptermap.cl_su_st_id')
            ->where('classsubjectmap.class_id',$id)
            ->groupBy('classsubjectmap.subject_id')
            ->get();
    }

    private function getChapter($id){
        $dm =  DB::table('streamchaptermap')
            ->select('streamchaptermap.cl_su_st_ch_id as id',
                'chapter.chapter_name as name',
                'stream.stream_name as stream_name',
                DB::raw('CONCAT(\'No. of Topics \',count(DISTINCT chaptertopicmap.hash)) as summery'))
            ->join('chapter','streamchaptermap.chapter_id','=','chapter.id')
            ->join('chaptertopicmap','streamchaptermap.cl_su_st_ch_id','=','chaptertopicmap.cl_su_st_ch_id')
            ->join('subjectstreammap','streamchaptermap.cl_su_st_id','=','subjectstreammap.cl_su_st_id')
            ->join('stream','subjectstreammap.stream_id','=','stream.id')
            ->where('streamchaptermap.cl_su_st_id','LIKE',$id."%")
            ->groupBy('streamchaptermap.chapter_id')
            ->get();
        return $dm;
    }

    private function getTopic($id){
        $dm =  DB::table('chaptertopicmap')
            ->select('chaptertopicmap.hash as hash',
                'topic.topic_name', 'content.pdf_path', 'content.video_path')
            ->join('topic','chaptertopicmap.topic_id','=','topic.id')
            ->leftjoin('content','chaptertopicmap.hash','=','content.hash')
            ->where('chaptertopicmap.cl_su_st_ch_id',$id)
            ->get();
        return $dm;
    }
}

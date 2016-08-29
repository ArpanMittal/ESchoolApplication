<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;

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
            ->join('topic','chaptertopicmap.topic_id','=','topic.id')
            ->join('content','chaptertopicmap.hash','=','content.hash')
            ->where('chaptertopicmap.cl_su_st_ch_id',$id)
            ->get();
        return $dm;
    }
}

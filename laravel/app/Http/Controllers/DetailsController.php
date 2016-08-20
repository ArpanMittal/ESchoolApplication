<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class DetailsController extends Controller
{
    //
    public function getDashBoard(Request $request){
        $class = $this->getClasses();
        $stream = $this->getStream();
        $exam = $this->getExam();

        $row1['title'] = 'Classes';
        $row1['list'] = $class;

        $row2['title'] = 'Streams';
        $row2['list'] = $stream;

        $row3['title'] = 'Exams';
        $row3['list'] = $exam;

        if (!$class && !$stream && !$exam){
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        $rows = array($row1, $row2, $row3);
        return Response::json([
            'success' => true,
            'code' => 200,
            'data' => $rows
        ]);
    }


    public function getDetails(Request $request, $tag, $id){
        switch ($tag){
            case 'Classes':
                $data = $this->getSubject($id);
                break;
            case 'Streams':
                $data = $this->getStreams($id);
                break;
            case 'Exams':
                $data = $this->getExamSubjects($id);
        }

        if (!$data){
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        return Response::json([
            'success' => true,
            'code' => 200,
            'data' => $data
        ]);
    }

    private function getClasses(){
        return DB::table('class')
            ->select('class.id as id',
                'class.class_name as name',
                DB::raw('count(DISTINCT classsubjectmap.cl_su_id) as count'))
            ->leftjoin('classsubjectmap','class.id','=','classsubjectmap.class_id')
            ->groupBy('class.id')
            ->get();
    }

    private function getStream()
    {
        return DB::table('subjectstreammap')
            ->select('subject.id as id',
                'subject.subject_name as name',
                DB::raw('count(DISTINCT subjectstreammap.cl_su_st_id) as count'))
            ->leftjoin('classsubjectmap','subjectstreammap.cl_su_id','=','classsubjectmap.cl_su_id')
            ->leftjoin('subject','classsubjectmap.subject_id','=','subject.id')
            ->groupBy('subject.id')
            ->get();
    }

    private function getExam()
    {
        return DB::table('examtag')
            ->select('examtag.id as id',
                'examtag.exam_name as name',
                DB::raw('"empty" as count'))
            ->where('exam_name', 'NOT LIKE', 'CBSE')
            ->get();
    }

    private function getChapter($id){
        return DB::table('streamchaptermap')
            ->select('streamchaptermap.cl_su_st_ch_id as id',
                'chapter.chapter_name as name',
                DB::raw('count(DISTINCT chaptertopicmap.hash) as count'))
            ->join('chapter','streamchaptermap.chapter_id','=','chapter.id')
            ->join('chaptertopicmap','streamchaptermap.cl_su_st_ch_id','=','chaptertopicmap.cl_su_st_ch_id')
            ->where('streamchaptermap.cl_su_st_id',$id)
            ->groupBy('streamchaptermap.chapter_id')
            ->get();
    }
    private function getSubject($id){
        return DB::table('classsubjectmap')
            ->select('classsubjectmap.cl_su_id as id',
                'subject.subject_name as name',
                DB::raw('count(DISTINCT streamchaptermap.cl_su_st_ch_id) as count'))
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('subjectstreammap','classsubjectmap.cl_su_id','=','subjectstreammap.cl_su_id')
            ->join('streamchaptermap','subjectstreammap.cl_su_st_id','=','streamchaptermap.cl_su_st_id')
            ->where('classsubjectmap.class_id',$id)
            ->groupBy('classsubjectmap.subject_id')
            ->get();
    }

    private function getStreams($id)
    {
        return DB::table('subjectstreammap')
            ->select('subjectstreammap.cl_su_st_id as id',
                'stream.stream_name as name',
                DB::raw('count(DISTINCT streamchaptermap.cl_su_st_ch_id) as count'))
            ->join('stream','subjectstreammap.stream_id','=','stream.id')
            ->join('streamchaptermap','subjectstreammap.cl_su_st_id','=','streamchaptermap.cl_su_st_id')
            ->where('subjectstreammap.cl_su_id','LIKE',"%".$id)
            ->groupBy('subjectstreammap.stream_id')
            ->get();
    }

    private function getExamSubjects($id)
    {
        return;
    }
}

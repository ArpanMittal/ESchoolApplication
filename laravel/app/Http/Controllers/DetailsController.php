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
        $stream = $this->getSubject();
        $exam = $this->getExam();

        $row1['title'] = 'Classes';
        $row1['list'] = $class;

        $row2['title'] = 'Subjects';
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

    public function getDashBoardImage(Request $request)
    {
        $data= DB::table('dashboard_image')
            ->select('dashboard_image.image_path as image', 'dashboard_image.text as text')
            ->get();
        if($data!=null)
        {
            return Response::json([
                'success' => true,
                'code' => 200,
                'data' => $data
            ]);
        }
        else
        {
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
    }


    public function getDetails(Request $request, $tag, $id){
        switch ($tag){
            case 'Classes':
                $title = 'ClassSubject';
                //$data = $this->getSubject($id);
                $data = $this->getSubjectListDetail($id);
                break;
            case 'Subjects':
                $title = 'SubjectStream';
                $data = $this->getStreamDetail($id);
                break;
            case 'Exams':
                $title = 'ExamSubject';
                $data = $this->getExamPrepareSubjects($id);

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
            'title' => $title,

            'data' => $data
            
        ]);
    }
    
    public function getCharts(Request $request,$id){
        $data  = $this->getChapter($id);
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
            'title' => "Subject",
            'data' => $data
        ]);
    }

    private function getClasses(){
        return DB::table('class')
            ->select('class.id as id',
                DB::raw('CONCAT(\'class \',class.class_name) as name'),
                DB::raw('CONCAT(\'No. of subjects \',count(DISTINCT classsubjectmap.cl_su_id)) as count'),
                'class.image as image')
            ->leftjoin('classsubjectmap','class.id','=','classsubjectmap.class_id')
            ->groupBy('class.id')
            ->get();
    }

    private function getSubject()
    {
        return DB::table('subjectstreammap')
            ->select('subject.id as id', 'subject.subject_name as name', DB::raw('CONCAT(\'No. of streams \',count(DISTINCT subjectstreammap.stream_id)) as count'),
                'subject.image as image')
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
                DB::raw('"" as count'),
                'examtag.image as image')
            ->where('exam_name', 'NOT LIKE', 'CBSE')
            ->get();
    }

    private function getChapter($id){
        $dm =  DB::table('streamchaptermap')
            ->select('streamchaptermap.cl_su_st_ch_id as id',
                'chapter.chapter_name as name')
            ->leftjoin('chapter','streamchaptermap.chapter_id','=','chapter.id')
            ->leftjoin('chaptertopicmap','streamchaptermap.cl_su_st_ch_id','=','chaptertopicmap.cl_su_st_ch_id')
            ->where('streamchaptermap.cl_su_st_id','LIKE',$id."%")
            ->groupBy('streamchaptermap.chapter_id')
            ->get();
        return $dm;
    }
    private function getClassSubjects($id){
        $arr= DB::table('classsubjectmap')
            ->select('classsubjectmap.cl_su_id as id','subject.subject_name as name')
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->where('classsubjectmap.class_id',$id)->get();
        return $arr;
    }

    private function getSubjectListDetail($id){
        //
        $subject=$this->getClassSubjects($id);
        for($i=0;$i<count($subject);$i++)
        {
            $chapter=$this->getChapter($subject[$i]->id);
            $subject[$i]->innerList=$chapter;
        }
        return $subject;
    }

    private function getStreams($id)
    {
        return DB::table('subjectstreammap')
            ->select('subjectstreammap.cl_su_st_id as id',
                'stream.stream_name as name')
               // DB::raw('CONCAT(\'No. of chapters \',count(DISTINCT streamchaptermap.cl_su_st_ch_id)) as count'))
            ->join('stream','subjectstreammap.stream_id','=','stream.id')
            ->join('streamchaptermap','subjectstreammap.cl_su_st_id','=','streamchaptermap.cl_su_st_id')
            ->where('subjectstreammap.cl_su_id','LIKE',"%".$id)
            ->groupBy('subjectstreammap.stream_id')
            ->get();
    }

    private function getStreamDetail($id)
    {

        $stream=$this->getStreams($id);
        for($i=0;$i<count($stream);$i++)
        {
            $chapter=$this->getChapter($stream[$i]->id);
            $stream[$i]->innerList=$chapter;
        }
        return $stream;
    }

    private function getExamPrepareSubjects($id){


        $prepare=DB::table('exampackmap')
            ->select('package.cost as cost','package.duration as duration','package.id as id')
            ->join('package','exampackmap.pack_id','=','package.id')
            ->where('exampackmap.exam_id',$id)->get();

        for($i=0;$i<count($prepare);$i++)
        {
            $subject=$this->getExamSubject($prepare[$i]->id);

            for($j=0;$j<count($subject);$j++)
            {
                $subject[$j]->chapter=$this->getIndividualChapter($subject[$j]->mainId);
            }
            $prepare[$i]->name="Package";
            $prepare[$i]->subject=$subject;
            $prepare[$i]->samplepaper=$this->getSamplePaper($id);


        }
//
        return $prepare;

    }
    
    private function getIndividualChapter($id){
        return DB::table('pack_subject_chapter_map')
            ->select("streamchaptermap.cl_su_st_ch_id as id",
                "chapter.chapter_name as name")
            ->join("streamchaptermap","pack_subject_chapter_map.chapter_id","=","streamchaptermap.cl_su_st_ch_id")
            ->join("chapter","chapter.id","=","streamchaptermap.chapter_id")
            ->where("pack_subject_chapter_map.pack_subject_id",$id)->get();
    }

    

    private function getExamSubject($id)
    {
        return DB::table('pack_subject_map')
            ->select('subject.subject_name as name','classsubjectmap.cl_su_id as id','pack_subject_map.id as mainId',
                DB::raw('CONCAT(class.class_name,":",subject.subject_name) as name'))
            ->join('classsubjectmap','pack_subject_map.subject_id',"=","classsubjectmap.cl_su_id")
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('class','classsubjectmap.class_id','=','class.id')
            ->where('pack_subject_map.pack_id',$id)->get();
    }

    private function getSamplePaper($id)
    {
        return DB::table('examtag')
            ->select('exam_state_year_rest_map.id as id', DB::raw('CONCAT(examtag.exam_name,\' \',state.state_name, \' \', year.year_name, \' \', rest_part.rest) as name'))
            ->join("exam_state_map","examtag.id","=","exam_state_map.exam_id")
            ->join("state","exam_state_map.state_id","=","state.id")
            ->join("exam_state_year_map","exam_state_map.id","=","exam_state_year_map.exam_state_id")
            ->join("year","exam_state_year_map.year_id","=","year.id")
            ->join("exam_state_year_rest_map","exam_state_year_map.id","=","exam_state_year_rest_map.exam_state_year_id")
            ->join("rest_part","exam_state_year_rest_map.rest_id","=","rest_part.id")
            ->where('examtag.id',$id)->get();
    }





}
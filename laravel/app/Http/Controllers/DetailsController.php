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
                DB::raw('CONCAT(\'No. of subjects \',count(DISTINCT classsubjectmap.cl_su_id)) as count'))
            ->leftjoin('classsubjectmap','class.id','=','classsubjectmap.class_id')
            ->groupBy('class.id')
            ->get();
    }

    private function getSubject()
    {
        return DB::table('subjectstreammap')
            ->select('subject.id as id', 'subject.subject_name as name', DB::raw('CONCAT(\'No. of streams \',count(DISTINCT subjectstreammap.stream_id)) as count'))
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
                DB::raw('"" as count'))
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
            ->select('package.cost as cost','subscription.item_id as id','subscription.type_id as type_id','subscription.duration as duration')
            ->leftjoin('package','exampackmap.pack_id','=','package.id')
            ->leftjoin('packagesubmap','package.id','=','packagesubmap.pack_id')
            ->leftjoin('subscription','packagesubmap.sub_id','=','subscription.id')
            ->where('exampackmap.exam_id',$id)->get();
        //return $prepare;
        //$k=0;


        for($i=0;$i<count($prepare);$i++)
        {
            if(!strcmp($prepare[$i]->type_id,"1")) {
                $subject = $this->getExamSubject($prepare[$i]->id);
                $chapter = $this->getChapter($prepare[$i]->id);

                $prepare[$i]->subject=$subject;
                for($j=0;$j<count($subject);$j++)
                $prepare[$i]->subject[$j]->chapter=$chapter;
            }
            else if(!strcmp($prepare[$i]->type_id,"2"))
            {
                //$subject
               // if($this->getChapterSubject($prepare[$i]->id,$prepare,$i=="false"))
                    $prepare[$i]->subject=$this->getChapterSubject($prepare[$i]->id,$prepare,$i);

            }
            else
            {

                $prepare[$i]->subject=$this->getTopicChapter($prepare[$i]->id,$prepare,$i);

            }


        }
        return $prepare;

    }

    private function getTopicChapter($id,$prepare,$length)
    {

        $stream=DB::table('chaptertopicmap')
            ->select('chaptertopicmap.cl_su_st_ch_id as id')
            ->where('chaptertopicmap.hash',$id)->get();
       
        for($i=0;$i<count($stream);$i++)
        {
            $subject=$this->getChapterSubject($stream[$i]->id,$prepare,$length);
        }
        return $subject;
    }

    private function getChapterSubject($id,$prepare,$length)
    {

        $subject1=DB::table('streamchaptermap')
            ->select('subjectstreammap.cl_su_id as id')
            ->join("subjectstreammap",'streamchaptermap.cl_su_st_id',"=","subjectstreammap.cl_su_st_id")
            ->where("streamchaptermap.cl_su_st_ch_id",$id)->get();

//        if($length>0)
//       return $prepare[$length-1]->subject[0]->id;
//        else
//            return $subject1;
        for($i=0;$i<count($subject1);$i++)
        {
            $subject=false;
            $flag=1;
            if($length>0) {
                for ($j = 0; $j < $length; $j++) {
                   //return $subject1[$i]->id;

                    if (!strcmp($prepare[$j]->subject[0]->id, $subject1[$i]->id)) {
                        $length=$this->getIndividualChapter($id);
                       // $prepare[$j]->subject[0]->chapter[$length]=$this->getIndividualChapter($id);
                        array_push($prepare[$j]->subject[0]->chapter,$length[0] );
                        $i++;
                        $flag = 0;
                        break;
                    }
                }
            }
            if($flag==1)
            {
                $subject = $this->getExamSubject($subject1[$i]->id);
                $chapter = $this->getIndividualChapter($id);
                for($j=0;$j<count($subject);$j++)
                {
                    $subject[$j]->chapter=$chapter;
                }
            }
        }
        return $subject;

    }
    private function getIndividualChapter($id){
        return DB::table('chapter')
            ->select("chapter.id as id",
                "chapter.chapter_name as name")
            ->join("streamchaptermap","chapter.id","=","streamchaptermap.chapter_id")
            ->where("streamchaptermap.cl_su_st_ch_id",$id)->get();
    }




//    private function getSubjectChapter($id)
//    {
//        return $this->getChapter($id);
//
////        $stream=DB::table('subjectstreammap')
////            ->select('subjectstreammap.cl_su_st_id as id')
////            ->where('subjectstreammap.cl_su.id',$id);
////
////        for($i=0;$i<count($stream);$i++)
////        {
////            $ch=$this->getChapter($stream[$i]->id);
////        }
////        return $ch;
//    }

    private function getExamSubject($id)
    {
        return DB::table('classsubjectmap')
            ->select('subject.subject_name as name','classsubjectmap.cl_su_id as id',
                DB::raw('CONCAT(class.class_name,":",subject.subject_name) as name'))
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('class','classsubjectmap.class_id','=','class.id')
            ->where('classsubjectmap.cl_su_id',$id)->get();
    }

//        $data = array(array('id'=>'practice','name'=>'Practice Test','count'=>''));
//        return $data;
    }
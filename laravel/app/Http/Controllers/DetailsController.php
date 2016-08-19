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
                'message' => 'Content not available'
            ]);
        }
        $rows = array($row1, $row2, $row3);
        return Response::json([
            'success' => true,
            'code' => 200,
            'data' => $rows
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
            ->groupBy('subjectstreammap.stream_id')
            ->get();
    }

    private function getExam()
    {
        return DB::table('examtag')
            ->select('examtag.id as id',
                'examtag.exam_name as name',
                DB::raw('count(DISTINCT examtag.id) as count'))
            ->where('exam_name', 'NOT LIKE', 'CBSE')
            ->get();
    }
}

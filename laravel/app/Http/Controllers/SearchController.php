<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class SearchController extends Controller
{
    //
    public function search(Request $request,$key){
        $res = DB::table('chaptertopicmap')
            ->join('streamchaptermap','chaptertopicmap.cl_su_st_ch_id','=','streamchaptermap.cl_su_st_ch_id')
            ->join('subjectstreammap','streamchaptermap.cl_su_st_id','=','subjectstreammap.cl_su_st_id')
            ->join('classsubjectmap','subjectstreammap.cl_su_id','=','classsubjectmap.cl_su_id')
            ->join('class','classsubjectmap.class_id','=','class.id')
            ->join('subject','classsubjectmap.subject_id','=','subject.id')
            ->join('stream','subjectstreammap.stream_id','=','stream.id')
            ->join('chapter','streamchaptermap.chapter_id','=','chapter.id')
            ->join('topic','chaptertopicmap.topic_id','=','topic.id')
            ->where('topic_name','LIKE','%'.$key."%")
            ->orWhere('chapter_name','LIKE','%'.$key."%")
            ->orWhere('stream_name','LIKE','%'.$key."%")
            ->orWhere('subject_name','LIKE','%'.$key."%")
            ->orWhere('class_name','LIKE','%'.$key."%")
            ->get();
        
        if (!$res){
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'No content found'
            ]);
        }

        return Response::json([
            'success' => true,
            'code' => 200,
            'count' => count($res),
            'data' => $res
        ]);
    }
    
    
}

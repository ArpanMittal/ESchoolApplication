<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class QuestController extends Controller
{
    public function freeDetails(Request $request, $id){
        $data = DB::table('chaptertopicmap')
            ->select('topic.topic_name',
                'chaptertopicmap.hash',
                'content.id as content_id',
                'content.pdf_path',
                'content.video_path',
                'content.pdf_hash',
                DB::raw('\'false\' as is_subscribed'),
                DB::raw('\'true\' as is_locked'),
                DB::raw('\'0\' as progress'))
            ->leftjoin('topic','chaptertopicmap.topic_id','=','topic.id')
            ->leftjoin('content','chaptertopicmap.hash','=','content.hash')
            ->where('cl_su_st_ch_id',$id)
            ->orderBy('chaptertopicmap.order', 'asc')
            ->get();

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
}

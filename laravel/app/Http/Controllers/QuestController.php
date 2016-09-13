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
                DB::raw('\'false\' as pdf_path'),
                DB::raw('\'false\' as video_path'),
                DB::raw('\'false\' as pdf_hash'),
                DB::raw('\'false\' as is_subscribed'),
                DB::raw('\'true\' as is_locked'),
                DB::raw('\'0\' as progress'))
            ->leftjoin('topic','chaptertopicmap.topic_id','=','topic.id')
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

    public function details(Request $request, $id){
        $user = DB::table('user')
            ->where('user.email',$request->input('user_id'))
            ->first();


        $data = DB::table('chaptertopicmap')
            ->select('topic.topic_name',
                'chaptertopicmap.hash',
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
        
        $subs = DB::table('order')
            ->leftjoin('orderpackmap','order.id','=','orderpackmap.order_id')
            ->leftjoin('pack_subject_map','orderpackmap.pack_id','=','pack_subject_map.pack_id')
            ->leftjoin('pack_subject_chapter_map','pack_subject_map.id','=','pack_subject_chapter_map.pack_subject_id')
            ->where('order.user_id',$user->id)
            ->where('pack_subject_chapter_map.chapter_id',$id)
            ->first();
        for ($i=0;$i<count($data);$i++){
            if (isset($subs)){
                $data[$i]->is_subscribed = "true";
            }

            $hash = $data[$i]->hash;

            $attempt = DB::table('user_attempt')
                ->select(DB::raw('count(DISTINCT user_attempt_response.id) as count'))
                ->leftjoin('user_attempt_response','user_attempt.id',"=",'user_attempt_response.user_attempt_id')
                ->where('user_attempt.user_id',$user->id)
                ->where('user_attempt.included_id',$hash)
                ->where('user_attempt_response.response','true')
                ->groupBy('user_attempt.id')
                ->get();

            $flag = false;
            if(isset($attempt) && count($attempt)>0){
                $max = 0;
                foreach ($attempt as $atmpt){
                    if ($max < $atmpt->count){
                        $max = $atmpt->count;
                    }
                }
                $percent = ($max/8)*100;
                $data[$i]->is_locked = "false";
                $data[$i]->progress = $percent;
                $flag = true;
            }else if($i==0 || $flag){
                $data[$i]->is_locked = "false";
                $data[$i]->progress = 0;
                $flag = false;
            }else{
                $data[$i]->video_path = null;
                $data[$i]->pdf_path = null;
                $data[$i]->pdf_hash = null;
                $data[$i]->is_locked = "true";
                $data[$i]->progress = 0;
            }
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
}

<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;

class ExamResultController extends Controller
{
    //
    public function show(Request $request){
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;
        
        $schools = DB::table('school')->get();

        $school_list = array();
        foreach($schools as $school){
            $school_list[$school->id] = $school->school_name;
        }
        $data['schools'] = $school_list;
        return view('question.result',$data);
    }

    public function result(Request $request){
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;

        $schools = DB::table('school')->get();
        $school_list = array();
        foreach($schools as $school){
            $school_list[$school->id] = $school->school_name;
        }
        $data['schools'] = $school_list;

        $validator = Validator::make($request->all(), [
            'school' => 'required|max:255',
            'date' => 'required',
        ]);
        if ($validator->fails()) {
            $data["error"] = $validator->errors();
            return view('question.result',$data);
        }

        $school = $request->get("school");
        $date=date_create($request->get("date"));
        $start_date = date_format($date,"Y-m-d H:i:s");
        $stop_date = date('Y-m-d H:i:s', strtotime($start_date . ' +1 day'));

        $results = DB::table('user_attempt')
            ->select('userdetail.name as name',
                DB::raw('user.email as email'),
                DB::raw('sum(case when user_attempt_response.response = "true" then 1 else 0 end) as correct'),
                DB::raw('sum(case when user_attempt_response.response = "false" then 1 else 0 end) as incorrect'),
                DB::raw('COUNT(user_attempt_response.id) as total'))
            ->leftjoin('user', 'user_attempt.user_id', '=', 'user.id')
            ->leftjoin('userdetail', 'user.id', '=', 'userdetail.id')
            ->leftjoin('user_attempt_response', 'user_attempt.id', '=', 'user_attempt_response.user_attempt_id')
            ->where("userdetail.school_id",$school)
            ->where("user_attempt.started_at",">=",$start_date)
            ->where("user_attempt.started_at","<",$stop_date)
            ->groupBy('user_attempt.id')
            ->orderBy('correct', 'DESC')
            ->get();
        $data['selected_school'] = $school;
        $data['selected_date'] = $request->get("date");
        $data["results"] = $results;
        return view('question.result',$data);
    }
}

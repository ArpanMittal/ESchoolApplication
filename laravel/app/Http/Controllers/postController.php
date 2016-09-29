<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;

class postController extends Controller
{
    //
    public function getAllPost(Request $request)
    {
        $user = DB::table('user')
            ->leftjoin('userdetail','user.id','=','userdetail.id')
            ->leftjoin('school','userdetail.school_id','=','school.id')
            ->where('user.email',$request->input('user_id'))
            ->first();
        if (!$user){
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        else {
            return Response::json([
                'success' => true,
                'code' => 200,
                'data' => $user

            ]);
        }
    }

    public  function getUserAttempt(Request $request)
    {
        $email=$request->input('user_id');
        
        $attempt=DB::table('user_attempt')
            ->select('user_attempt.id')
            ->join('user','user_attempt.user_id',"=","user.id")
            ->where('user.email',$email)->get();
        
        if(!$attempt==0)
        {
            return Response::json([
                'success' => false,
                'code' => 401,
                'message' => 'Content is not available'
            ]);
        }
        else
        {
            return Response::json([
                'success' => true,
                'code' => 200,
                'title' => "Attempt",
                'data' => $attempt
            ]);
        }
        

    }

    public function getProfileData(Request $request)
    {
        $state = DB::table('userdetail')
            ->select(DB::raw('DISTINCT userdetail.state as state_name'))
            ->get();
        $city = DB::table('userdetail')
            ->select(DB::raw('DISTINCT userdetail.city as city_name'))
            ->get();
        $school = DB::table('school')
            ->get();
        $data = array(
            "state" => $state,
            "city" => $city,
            "school" => $school
        );
            return Response::json([
                'success' => true,
                'code' => 200,
                'data' => $data

            ]);
    }

    public function saveProfileData(Request $request)
    {
        $user = DB::table('user')
            ->where('user.email',$request->input('user_id'))
            ->first();
        $status = 0;
        $status += DB::table('userdetail')->where('id',$user->id)
            ->update([
                'name'              =>  $request->get("name"),
                'date_of_birth'     =>  $request->get("date_of_birth"),
                'country'           =>  $request->get("country"),
                'state'             =>  $request->get("state"),
                'city'              =>  $request->get("city"),
                'phone_number'      =>  $request->get("phone_number")
            ]);

        if ($request->exists("profile_pic")){
            $data = $request->get("profile_pic");
            $directory = 'profile/';
            if (!file_exists($directory)) {
                mkdir($directory, 0777, true);
            }
            $filePath = $directory .$user->id.".jpg";
            $path = public_path($filePath);
            if (file_exists($path)) {
                unlink($path);
            }
            $file = $this->base64_to_jpeg($data,$path);
            $url = asset($filePath);
            $status += DB::table('userdetail')->where('id',$user->id)
                ->update([
                    'photo_path'              =>  $url
                ]);
            $status+=1;
        }
        if ($request->get("school")!=""){
            $temp = DB::table("school")->where('school_name',$request->get("school"))->first();
            if (!isset($temp)){
                $temp = DB::table('school')->insertGetId([
                            'school_name'              =>  $request->get("school")
                        ]);
            }else{
                $temp = $temp->id;
            }
            $status += DB::table('userdetail')->where('id',$user->id)
                ->update([
                    'school_id'              =>  $temp
                ]);
        }else{
            $status += DB::table('userdetail')->where('id',$user->id)
                ->update([
                    'school_id'              =>  null
                ]);
        }

        if ($status <=0){
            return Response::json([
                'success' => false,
                'code' => 417
            ]);
        }
        return Response::json([
            'success' => true,
            'code' => 200
        ]);
    }

    private function base64_to_jpeg($base64_string, $output_file) {
        $ifp = fopen($output_file, "wb");



        fwrite($ifp, base64_decode($base64_string));
        fclose($ifp);

        return $output_file;
    }
}

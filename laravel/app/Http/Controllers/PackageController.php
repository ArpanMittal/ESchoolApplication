<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Http\Requests;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Redirect;

class PackageController extends Controller
{
    //Controller to create a new package

    public function showPackageCreator(Request $request) {

        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;

        $data['subjects'] = DB::table('operatorsubjectmap')
            ->join('classsubjectmap', 'operatorsubjectmap.subject_id', '=', 'classsubjectmap.subject_id')
            ->join('subject', 'operatorsubjectmap.subject_id', '=', 'subject.id')
            ->join('class', 'classsubjectmap.class_id', '=', 'class.id')
            ->get();

        $data['chapters'] = DB::table('streamchaptermap')
            ->join('chapter', 'streamchaptermap.chapter_id', '=', 'chapter.id')
            ->join('subjectstreammap', 'streamchaptermap.cl_su_st_id', '=', 'subjectstreammap.cl_su_st_id')
            ->join('stream', 'subjectstreammap.stream_id', '=', 'stream.id')
            ->get();

        $data['topics'] = DB::table('chaptertopicmap')
            ->join('topic', 'chaptertopicmap.topic_id', '=', 'topic.id')
            ->get();

        return view('package.create',$data);
    }

    public function addPackage(Request $request) {

        $userId = $request->session()->get('id');
        $user = DB::table('user')->whereId($userId)->first();
        $data['user'] = $user;

        //Insert the package into the db

        $packageDetails['name'] = Input::get('PackageName');
        $packageDetails['subjects'] = Input::get('subjects');
        $packageDetails['chapters'] = Input::get('chapters');
        $packageDetails['topics'] = Input::get('topics');
        $topicCount = count($packageDetails['topics']);
        if ($topicCount <= 100) { $packageDetails['cost'] = 10*$topicCount; }
        elseif ($topicCount > 100 && $topicCount <= 200) { $packageDetails['cost'] = (10*$topicCount)-$topicCount; }
        elseif ($topicCount > 200) { $packageDetails['cost'] = (10 * $topicCount) - (2.5*$topicCount); }
        $subIds = array();

        try {
            DB::beginTransaction();

            if (count($packageDetails['subjects']) != 0) {

                foreach ($packageDetails['subjects'] as $subject) {

                    $temp = DB::table('subscription')->insertGetId(
                        ['type_id' => 1, 'item_id' => $subject]
                    );
                    if (!$temp>0){
                        DB::rollback();
                        $result['success'] = 'false';
                        $result['error'] = 'Error adding subject';
                        $data['result'] = $result;
                        return Redirect::to('package/list')->with('data', $data);
                    }
                    array_push($subIds, $temp);
                    foreach ($packageDetails['chapters'] as $chapter) {

                        if (substr_compare($subject, $chapter, 0, 5) == 0) {

                            foreach ($packageDetails['topics'] as $topic) {

                                if (substr_compare($chapter, $topic, 0, 10) == 0) {
                                    $key = array_search($topic, $packageDetails['topics']);
                                    unset($packageDetails['topics'][$key]);
                                }
                            }
                            $key = array_search($chapter, $packageDetails['chapters']);
                            unset($packageDetails['chapters'][$key]);
                        }
                    }
                }
            }
            if (count($packageDetails['chapters']) != 0) {

                foreach ($packageDetails['chapters'] as $chapter) {

                    $temp = DB::table('subscription')->insertGetId(
                        ['type_id' => 2, 'item_id' => $chapter]
                    );
                    if (!$temp>0){
                        DB::rollback();
                        $result['success'] = 'false';
                        $result['error'] = 'Error adding chapter';
                        $data['result'] = $result;
                        return Redirect::to('package/list')->with('data', $data);
                    }
                    array_push($subIds, $temp);
                    foreach ($packageDetails['topics'] as $topic) {

                        if (substr_compare($chapter, $topic, 0, 10) == 0) {
                            $key = array_search($topic, $packageDetails['topics']);
                            unset($packageDetails['topics'][$key]);
                        }
                    }
                }
            }
            if (count($packageDetails['topics']) != 0) {

                foreach ($packageDetails['topics'] as $topic) {

                    $temp = DB::table('subscription')->insertGetId(
                        ['type_id' => 3, 'item_id' => $topic]
                    );
                    if (!$temp>0){
                        DB::rollback();
                        $result['success'] = 'false';
                        $result['error'] = 'Error adding topic';
                        $data['result'] = $result;
                        return Redirect::to('package/list')->with('data', $data);
                    }
                    array_push($subIds, $temp);
                }
            }

            $packageId = DB::table('package')->insertGetId(
                ['package_name' => $packageDetails['name'], 'cost' => $packageDetails['cost']]
            );
            if (!$packageId>0){
                DB::rollback();
                $result['success'] = 'false';
                $result['error'] = 'Error creating package';
                $data['result'] = $result;
                return Redirect::to('package/list')->with('data', $data);
            }

            foreach ($subIds as $subId) {

                DB::table('packagesubmap')->insert(
                    ['pack_id'=> $packageId, 'sub_id' => $subId]
                );
            }

            DB::commit();

        } catch(Exception $e)
        {
            // Back to form with errors
            DB::rollback();
            $result['success'] = 'false';
            $result['error'] = $e->getTraceAsString();
            $data['result'] = $result;
            return Redirect::to('package/list')->with('data', $data);
        }

        $result['success'] = 'true';
        $data['result'] = $result;

        return Redirect::to('package/list')->with('data', $data);
    }

    public function getAllPackages(Request $request)
    {
        $id = $request->session()->get('id');
        $user = DB::table('user')->whereId($id)->first();
        $data['user'] = $user;

        $data['packages'] = DB::table('package')
            ->get();
        $data['submap'] = DB::table('packagesubmap')
            ->join('subscription', 'packagesubmap.sub_id', '=', 'subscription.id')
            ->get();
        return view('package.list',$data);
    }
}

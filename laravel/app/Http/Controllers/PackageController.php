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

        $data['subjects'] = DB::table('classsubjectmap')
            ->join('subject', 'classsubjectmap.subject_id', '=', 'subject.id')
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

        $data['exams'] = DB::table('examtag')->get();

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
        $packageDetails['packagetype'] = Input::get('PackageType');
        $pack_sub_id = '';
        $pack_sub_chap_id = '';
        if ($packageDetails['packagetype'] == 1) {
            $packageDetails['exam'] = Input::get('ExamTag');
        }
        $topicCount = count($packageDetails['topics']);
        if ($topicCount <= 100) { $packageDetails['cost'] = 10*$topicCount; }
        elseif ($topicCount > 100 && $topicCount <= 200) { $packageDetails['cost'] = (10*$topicCount)-$topicCount; }
        elseif ($topicCount > 200) { $packageDetails['cost'] = (10 * $topicCount) - (2.5*$topicCount); }

        try {
            DB::beginTransaction();

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

            if (isset($packageDetails['topics'])) {

                foreach ($packageDetails['topics'] as $topic) {

                    $query = DB::table('pack_subject_map')
                        ->where('pack_id', $packageId)
                        ->where('subject_id', substr($topic, 0, 5))
                        ->first();
                    if ($query == null) {
                        $pack_sub_id = DB::table('pack_subject_map')->insertGetId(
                            ['pack_id' => $packageId, 'subject_id' => substr($topic, 0, 5)]
                        );
                        if (!$pack_sub_id > 0) {
                            DB::rollback();
                            $result['success'] = 'false';
                            $result['error'] = 'Error adding subject';
                            $data['result'] = $result;
                            return Redirect::to('package/list')->with('data', $data);
                        }

                        $query = DB::table('pack_subject_chapter_map')
                            ->where('pack_subject_id', $pack_sub_id)
                            ->where('chapter_id', substr($topic, 0, 10))
                            ->first();
                        if ($query == null) {

                            $pack_sub_chap_id = DB::table('pack_subject_chapter_map')->insertGetId(
                                ['pack_subject_id' => $pack_sub_id, 'chapter_id' => substr($topic, 0, 10)]
                            );
                            if (!$pack_sub_chap_id > 0) {
                                DB::rollback();
                                $result['success'] = 'false';
                                $result['error'] = 'Error adding subject';
                                $data['result'] = $result;
                                return Redirect::to('package/list')->with('data', $data);
                            }

                            $query = DB::table('pack_subject_chapter_topic_map')->insert(
                                ['pack_subject_chapter_id' => $pack_sub_chap_id, 'topic_id' => $topic]
                            );
                            if (!$query > 0) {
                                DB::rollback();
                                $result['success'] = 'false';
                                $result['error'] = 'Error adding topic';
                                $data['result'] = $result;
                                return Redirect::to('package/list')->with('data', $data);
                            }
                        } else {

                            $query = DB::table('pack_subject_chapter_topic_map')->insert(
                                ['pack_subject_chapter_id' => $pack_sub_chap_id, 'topic_id' => $topic]
                            );
                            if (!$query > 0) {
                                DB::rollback();
                                $result['success'] = 'false';
                                $result['error'] = 'Error adding topic';
                                $data['result'] = $result;
                                return Redirect::to('package/list')->with('data', $data);
                            }
                        }
                    } else {

                        $query = DB::table('pack_subject_chapter_map')
                            ->where('pack_subject_id', $pack_sub_id)
                            ->where('chapter_id', substr($topic, 0, 10))
                            ->first();
                        if ($query == null) {

                            $pack_sub_chap_id = DB::table('pack_subject_chapter_map')->insertGetId(
                                ['pack_subject_id' => $pack_sub_id, 'chapter_id' => substr($topic, 0, 10)]
                            );
                            if (!$pack_sub_chap_id > 0) {
                                DB::rollback();
                                $result['success'] = 'false';
                                $result['error'] = 'Error adding subject';
                                $data['result'] = $result;
                                return Redirect::to('package/list')->with('data', $data);
                            }

                            $query = DB::table('pack_subject_chapter_topic_map')->insert(
                                ['pack_subject_chapter_id' => $pack_sub_chap_id, 'topic_id' => $topic]
                            );
                            if (!$query > 0) {
                                DB::rollback();
                                $result['success'] = 'false';
                                $result['error'] = 'Error adding topic';
                                $data['result'] = $result;
                                return Redirect::to('package/list')->with('data', $data);
                            }
                        } else {

                            $query = DB::table('pack_subject_chapter_topic_map')->insert(
                                ['pack_subject_chapter_id' => $pack_sub_chap_id, 'topic_id' => $topic]
                            );
                            if (!$query > 0) {
                                DB::rollback();
                                $result['success'] = 'false';
                                $result['error'] = 'Error adding topic';
                                $data['result'] = $result;
                                return Redirect::to('package/list')->with('data', $data);
                            }
                        }
                    }
                }
            }

            if ($packageDetails['packagetype'] == 1) {

                DB::table('exampackmap')->insert(
                    ['pack_id'=> $packageId, 'exam_id' => $packageDetails['exam']]
                );
            }

            DB::commit();

        } catch(Exception $e) {

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
//        $data['submap'] = DB::table('pack_subject_map')
//            ->join('pack_subject_chapter_map', 'pack_subject_map.id', '=', 'pack_subject_chapter_map.subject_map_id')
//            ->join('pack_subject_chapter_topic_map', 'pack_subject_chapter_map.id', '=', 'pack_subject_chapter_topic_map.subject_chapter_map_id')
//            ->get();
        return view('package.list',$data);
    }
}

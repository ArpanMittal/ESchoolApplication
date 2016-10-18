<?php

namespace App\Http\Controllers;

use Illuminate\Http\nequest;

use App\Http\nequests;
use App\DatabaseModel;
//to bulk insert data into databse initial setup
class insert extends Controller
{
    //
    //$path="C:/Users/arpan/Desktop/";
    public function doinsert()
    {
    	//$path="C:/Users/arpan/Desktop/";
      // C:\Users\arpan\Downloads\Compressed\Code Sheets_2\Code Sheets
    	$path="C:/Users/arpan/Downloads/Compressed/Code Sheets_2/Code Sheets/";
		//return "inserted";
    	$filename="chaptertopicmap.csv";
    	$filename1="insert_class.csv";
    	$csv = $path . $filename;
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE class FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `class_name`)", addslashes($csv1));
    	\DB::connection()->getpdo()->exec($query);
    	return $this->insertsubject($path);

    	//$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE classsubjectmap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`class_id`, `subject_id`,cl_su_id)", addslashes($csv));
    	//return \DB::connection()->getpdo()->exec($query);
    	//$datbase=new DatabaseModel;
    	//$datbase->topicname="hello";
    	//$datbase->save();
    }
    public function insertsubject($path)
    {
    	//$path="C:/Users/arpan/Desktop/";
    	$filename1="insert_subject.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE subject FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `subject_name`)", addslashes($csv1));
    	 \DB::connection()->getpdo()->exec($query);
    	return $this->insertStream($path);
    }
    public function insertStream($path)
    {
    	$filename1="insert_stream.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE stream FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `stream_name`)", addslashes($csv1));
    	 \DB::connection()->getpdo()->exec($query);
    	return $this->insertChapter($path);
    }
    public function insertChapter($path)
    {
    	$filename1="insert_chapter.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE chapter FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `chapter_name`)", addslashes($csv1));
    	 \DB::connection()->getpdo()->exec($query);
    	 return $this->insertTopic($path);
    }
    public function insertTopic($path)
    {
    	$filename1="insert_topic.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE topic FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `topic_name`)", addslashes($csv1));
    	\DB::connection()->getpdo()->exec($query);
    	//echo "hello";
    	return $this->classsubjectmap($path);
    }
    public function classsubjectmap($path)
    {

    	$filename1="classsubject1.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE classsubjectmap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`class_id`, `subject_id`,`cl_su_id`)", addslashes($csv1));
    	//echo $query;
    	\DB::connection()->getpdo()->exec($query);
    	return $this->subjectstreammap($path);
    }
    public function subjectstreammap($path)
    {
    	$filename1="subjectstream.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE subjectstreammap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`cl_su_id`, `stream_id`,`cl_su_st_id`)", addslashes($csv1));
    	 \DB::connection()->getpdo()->exec($query);
    	 return $this->streamchapter($path);
    }
    public function streamchapter($path)
    {
    	$filename1="streamchaptermap.csv";
        //$filename1="Book1.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE streamchaptermap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`cl_su_st_id`,`chapter_id`,`cl_su_st_ch_id`)", addslashes($csv1));
    	 \DB::connection()->getpdo()->exec($query);
    	 return $this->chaptertopic($path);

    }
    public function chaptertopic($path)
    {
    	$filename1="chaptertopicmap.csv";
    	$csv1=$path.$filename1;
    	$query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE chaptertopicmap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`cl_su_st_ch_id`,`topic_id`,`order`,`hash`)", addslashes($csv1));
    	 \DB::connection()->getpdo()->exec($query);
    	 return $this->chaptertopic($path);
    }


}

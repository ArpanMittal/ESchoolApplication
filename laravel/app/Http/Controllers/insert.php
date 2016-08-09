<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
/*
 * insert data in databse by extracting from csv files 
 * @input csv files
 * @work insert data into database
 * @output number of rows inserted
 */
class insert extends Controller
{
    public function doinsert()
    {
        /*
         * insert class value in the database
         */
        $path="../csv files/";
        $filename="chaptertopicmap.csv";
        $filename1="insert_class.csv";
        $csv = $path . $filename;
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE class FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `class_name`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->insertsubject($path);
        
    }
    public function insertsubject($path)
    {
        /*
         * insert subject value into database
         */
        $filename1="insert_subject.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE subject FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `subject_name`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->insertStream($path);
    }
    public function insertStream($path)
    {
        /*
         * insert stream values into databse
         */
        $filename1="insert_stream.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE stream FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `stream_name`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->insertChapter($path);
    }
    public function insertChapter($path)
    {
        /*
         * insert chapter values into database
         */
        $filename1="insert_chapter.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE chapter FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `chapter_name`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->insertTopic($path);
    }
    public function insertTopic($path)
    {
        /*
         * insert topic value into the database
         */
        $filename1="insert_topic.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE topic FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`id`, `topic_name`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->classsubjectmap($path);
    }
    public function classsubjectmap($path)
    {
        /*
         * mapping class and subject inserting into database 
         */
        $filename1="classsubject1.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE classsubjectmap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`class_id`, `subject_id`,`cl_su_id`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->subjectstreammap($path);
    }
    public function subjectstreammap($path)
    {
        /*
         * mapping subject stream inserting into database
         */
        $filename1="subjectstream.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE subjectstreammap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`cl_su_id`, `stream_id`,`cl_su_st_id`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->streamchapter($path);
    }
    public function streamchapter($path)
    {
        /*
         * mapping stream chapter map inserting them into database
         */
        $filename1="streamchaptermap.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE streamchaptermap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`cl_su_st_id`,`chapter_id`,`cl_su_st_ch_id`)", addslashes($csv1));
        \DB::connection()->getpdo()->exec($query);
        return $this->chaptertopic($path);

    }
    public function chaptertopic($path)
    {
        /*
         * mapping chapter topic and insert them into database
         */
        $filename1="chaptertopicmap.csv";
        $csv1=$path.$filename1;
        $query = sprintf("LOAD DATA local INFILE '%s' INTO TABLE chaptertopicmap FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' ESCAPED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (`cl_su_st_ch_id`,`topic_id`,`order`,`hash`)", addslashes($csv1));
       return \DB::connection()->getpdo()->exec($query);
        

    }

}

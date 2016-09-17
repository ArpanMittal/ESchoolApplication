<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class ExamQuesNoTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('exam_pattern_map',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('exam_id')->unsigned();
            $table->string('subject_id');
            $table->integer('no_of_questions')->unsigned();
            $table->datetime('created_at');
            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
            $table->foreign('exam_id')->references('id')->on('examtag')->onDelete('cascade');
            $table->foreign('subject_id')->references('cl_su_id')->on('classsubjectmap')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
        drop('exam_pattern_map');
    }
}

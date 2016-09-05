<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Packageoptimization extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //

        Schema::table('subscription', function ($table) {
            $table->dropColumn('item_id');
        });
        
        
        Schema::create('sub_subject_map',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('sub_id')->unsigned();
            $table->string('subject_id');
            $table->Foreign('sub_id')->references('id')->on('subscription')->onDelete('cascade');
            $table->Foreign('subject_id')->references('id')->on('subject')->onDelete('cascade');;
        });

        Schema::create('sub_subject_chapter_map',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('sub_subject_id')->unsigned();
            $table->string('chapter_id');
            $table->Foreign('sub_subject_id')->references('id')->on('sub_subject_map')->onDelete('cascade');
            $table->Foreign('chapter_id')->references('id')->on('chapter')->onDelete('cascade');;
        });

        Schema::create('sub_subject_chapter_topic_map',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('sub_subject_chapter_id')->unsigned();
            $table->string('topic_id');
            $table->Foreign('sub_subject_chapter_id')->references('id')->on('sub_subject_chapter_map')->onDelete('cascade');
            $table->Foreign('topic_id')->references('id')->on('topic')->onDelete('cascade');;
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
        Schema::drop('subscription_subject_chapter_topic_map');
        Schema::drop('subscription_subject_chapter_map');
        Schema::drop('subscription_subject_map');
        Schema::table('subscription', function ($table) {
            $table->string('item_id');
        });
        
    }
}

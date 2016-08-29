<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Samplepaper extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('state', function ($table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('state_name');
            $table->timestamp('timestamp')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
        });

        Schema::create('year', function ($table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('year_name');
            $table->timestamp('timestamp')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
        });

        Schema::create('rest_part', function ($table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('rest');
            $table->timestamp('timestamp')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
        });

        Schema::create('exam_state_map',function ($table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('exam_id')->unsigned();
            $table->integer('state_id')->unsigned();
            $table->foreign('exam_id')->references('id')->on('examtag')->onDelete('cascade');
            $table->foreign('state_id')->references('id')->on('state')->onDelete('cascade');
        });

        Schema::create('exam_state_year_map',function ($table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('exam_state_id')->unsigned();
            $table->integer('year_id')->unsigned();
            $table->foreign('exam_state_id')->references('id')->on('exam_state_map')->onDelete('cascade');
            $table->foreign('year_id')->references('id')->on('year')->onDelete('cascade');
        });

        Schema::create('exam_state_year_rest_map',function ($table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('exam_state_year_id')->unsigned();
            $table->integer('rest_id')->unsigned();
            $table->foreign('exam_state_year_id')->references('id')->on('exam_state_year_map')->onDelete('cascade');
            $table->foreign('rest_id')->references('id')->on('rest_part')->onDelete('cascade');
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
        Schema::drop('state');
        Schema::drop('year');
        Schema::drop('rest_part');
        Schema::drop('exam_state_map');
        Schema::drop('exam_state_year_map');
        Schema::drop('exam_state_year_rest_map');
    }
}

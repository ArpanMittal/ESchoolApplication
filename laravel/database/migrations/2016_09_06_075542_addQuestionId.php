<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddQuestionId extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::table('user_attempt_response', function ($table) {
            $table->integer('question_id')->unsigned();
            $table->foreign('question_id')->references('id')->on('question')->onDelete('cascade');
        });
        Schema::table('question',function($table){
            $table->integer('users_difficulty')->nullable();
            $table->time('users_ideal_attempt_time')->nullable();
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
        
    }
}

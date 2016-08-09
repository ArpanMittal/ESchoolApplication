<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Additionaltables extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('operatorsubjectmap',function(Blueprint$table){
            $table->engine='InnoDB';
            
            $table->string('subject_id',3);
            $table->integer('user_id')->unsigned();
            $table->foreign('subject_id')->references('id')->on('subject')->onDelete('cascade');
            $table->foreign('user_id')->references('id')->on('user')->onDelete('cascade');
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
        drop('operatorsubjectmap');
    }
}

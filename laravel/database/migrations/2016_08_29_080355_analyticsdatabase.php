<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Analyticsdatabase extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('attempt_type',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('attempt_name');
        });

        Schema::create('user_attempt',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('user_id')->unsigned();
            $table->integer('attempt_type_id')->unsigned();
            $table->string('included_id');
            $table->timestamp('started_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
            $table->foreign('user_id')->references('id')->on('user')->onDelete('cascade');
            $table->foreign('attempt_type_id')->references('id')->on('attempt_type')->onDelete('cascade');
        });

        Schema::create('user_attempt_response',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('user_attempt_id')->unsigned();
            $table->time('time_taken');
            $table->boolean('response')->nullable();
            $table->timestamp('timestamp')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));

        });

        Schema::table('content', function ($table) {
            $table->string('pdf_hash');
        });

        

        Schema::table('user',function($table){
            $table->time('time_spend')->nullable();
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
        Schema::drop('attempt_type');
        Schema::drop('user_attempt');
        Schmea::drop('user_attempt_response');
        Schema::table('content', function($table)
        {
            $table->dropColumn('pdf_hash');
        });
       

        Schema::table('user',function($table){
            $table->dropColumn('time_spend');
        });
    }
}

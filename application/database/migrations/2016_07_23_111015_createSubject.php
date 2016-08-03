<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSubject extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {

        Schema::create('class',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('class_name');
            $table->timestamps('created_at');
            $table->primary('id');
        });
        Schema::create('stream',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('stream_name');
            $table->timestamps('created_at');
            $table->primary('id');
        });
         Schema::create('subject',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('subject_name');
            $table->timestamps('created_at');
            $table->primary('id');
        });


        Schema::create('chapter',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('chapter_name');
            $table->timestamps('created_at');
            $table->primary('id');
        });
        Schema::create('topic',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('topic_name');
            $table->timestamps('created_at');
            $table->primary('id');
        });
       
        Schema::create('classsubjectmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('class_id',3);
            $table->string('subject_id',3);
            $table->string('cl_su_id',6)->primary();
            $table->timestamps('created_at');
            $table->foreign('class_id')->references('id')->on('class')->onDelete('cascade');
            $table->foreign('subject_id')->references('id')->on('subject')->onDelete('cascade');

        });
        Schema::create('subjectstreammap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('cl_su_id',6);
            $table->string('stream_id',3);
            $table->string('cl_su_st_id',9)->primary();
            $table->timestamps('created_at');
            $table->foreign('cl_su_id')->references('cl_su_id')->on('classsubjectmap')->onDelete('cascade');
            $table->foreign('stream_id')->references('id')->on('stream')->onDelete('cascade');

        });

         Schema::create('streamchaptermap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('cl_su_st_id',9);

            $table->string('chapter_id',3);
            $table->string('cl_su_st_ch_id',12)->primary();
            $table->timestamps('created_at');
            $table->foreign('chapter_id')->references('id')->on('chapter')->onDelete('cascade');
            $table->foreign('cl_su_st_id')->references('cl_su_st_id')->on('subjectstreammap')->onDelete('cascade');

        });
         Schema::create('chaptertopicmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('cl_su_st_ch_id',12);
            $table->string('topic_id',3);
            $table->String('order',3);
            $table->string('hash',15)->primary();

            $table->timestamps('created_at');
            $table->foreign('cl_su_st_ch_id')->references('cl_su_st_ch_id')->on('streamchaptermap')->onDelete('cascade');
            $table->foreign('topic_id')->references('id')->on('topic')->onDelete('cascade');

        });
       
         Schema::create('role',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->enum('role',array('admin','student','operator','supervisor'));
            $table->timestamps('created_at');
        });
         Schema::create('user',function(Blueprint $table){
            $table->engine='InnoDB';
            $table->increments('id');
             $table->string('email');
             $table->string('password');
             $table->boolean('is_active');
             $table->integer('role_id')->unsigned();
             $table->string('token_id')->unique();
              $table->foreign('role_id')->references('id')->on('role')->onDelete('cascade');
         });
        Schema::create('userdetail', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            //$table->string('Email');
            $table->string('name');
            $table->boolean('verified');
            $table->string('photo_path');
            $table->date('date_of_birth');
            $table->string('country')->nullable();
            $table->string('state')->nullable();
            $table->string('city')->nullable();
            $table->integer('phone_number')->nullable()->unsigned();
            $table->timestamps('created_at');
            
        });
       
        Schema::create('questiontype',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('question_type');
            $table->timestamps('created_at');
        });
        Schema::create('question', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('hash',100)->nullable();
            $table->longtext('question');
            $table->string('image_path')->nullable();
            $table->integer('question_type_id')->unsigned();
            //$table->dateTime('TimeCreated');
            //$table->timestamps('TimeModified');
            $table->timestamps('created_at');
            $table->integer('created_by')->nullable()->unsigned();
            $table->integer('modified_by')->nullable()->unsigned();
            $table->string('solution_path');
            $table->integer('difficulty');
            $table->time('ideal_attempt_time');
            $table->foreign('question_type_id')->references('id')->on('questiontype')->onDelete('cascade');
            $table->foreign('created_by')->references('id')->on('user')->onDelete('set null');
            $table->foreign('modified_by')->references('id')->on('user')->onDelete('set  null');
            $table->foreign('hash')->references('hash')->on('chaptertopicmap')->onDelete('set null');

        });
        Schema::create('examtag',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('exam_name');
            $table->timestamps('created_at');
        });

        Schema::create('questiontags',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->integer('question_id')->unsigned();
            $table->integer('tag_id')->unsigned();
            $table->foreign('question_id')->references('id')->on('question')->onDelete('cascade');
            $table->foreign('tag_id')->references('id')->on('examtag')->onDelete('cascade');
        });

       Schema::create('option', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('question_id')->unsigned();
            $table->longtext('opt');
            $table->boolean('is_left')->nullable();
            $table->timestamps('created_at');
            $table->foreign('question_id')->references('id')->on('question')->onDelete('cascade');

        });
         Schema::create('answer', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->integer('question_id')->unsigned();
            $table->longtext('answer');
            $table->timestamps('created_at');
            $table->foreign('question_id')->references('id')->on('question')->onDelete('cascade');

        });

         Schema::create('content', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('hash',100)->nullable();
            $table->string('video_path')->nullable();
            $table->string('pdf_path')->nullable();
            $table->integer('created_by')->unsigned()->nullable();
            $table->integer('modified_by')->unsigned()->nullable();
            //$table->dateTime('CreatedTime');
            $table->timestamps('created_at');
            $table->foreign('hash')->references('hash')->on('chaptertopicmap')->onDelete('set null');
            $table->foreign('created_by')->references('id')->on('user')->onDelete('set null');
            $table->foreign('modified_by')->references('id')->on('user')->onDelete('set  null');

        });




         Schema::create('subscriptiontype', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('sub_type');
            $table->boolean('active');
            $table->timestamps('created_at');
        });
         Schema::create('order',function(Blueprint $table)
         {
             $table->engine='InnoDB';
             $table->increments('id');
             $table->integer('user_id');
             $table->timestamps('created_at');
            // $table->integer('PaymentType');
             $table->decimal('amount',10,2);

         });

        Schema::create('subscription', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->timestamps('created_at');
           // $table->integer('By')->unsigned();
            $table->integer('type_id')->unsigned();
            $table->integer('duration')->default(12);
            $table->integer('item_id');
            $table->foreign('type_id')->references('id')->on('subscriptiontype')->onDelete('cascade');
        }); 
         Schema::create('ordersubscriptionmap',function(Blueprint $table){
            $table->engine='InnoDB';
            $table->integer('order_id')->unsigned();
            $table->integer('sub_id')->unsigned();
             $table->timestamps('created_at');
         });

       
        
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
         
    }
}

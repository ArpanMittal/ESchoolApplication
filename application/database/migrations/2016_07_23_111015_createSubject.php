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
            $table->string('classname');
            $table->timestamps('timestamp');
            $table->primary('id');
        });
        Schema::create('stream',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('streamname');
            $table->timestamps('timestamp');
            $table->primary('id');
        });
         Schema::create('subject',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('subjectname');
            $table->timestamps('timestamp');
            $table->primary('id');
        });


        Schema::create('chapter',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('chaptername');
            $table->timestamps('timestamp');
            $table->primary('id');
        });
        Schema::create('topic',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('id',3);
            $table->string('topicname');
            $table->timestamps('timestamp');
            $table->primary('id');
        });
       
        Schema::create('classsubjectmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('classid',3);
            $table->string('subjectid',3);
            $table->string('clsuid',6)->primary();
            $table->timestamps('timestamp');
            $table->foreign('classid')->references('id')->on('class')->ondelete('cascade');
            $table->foreign('subjectid')->references('id')->on('subject')->ondelete('cascade');

        });
        Schema::create('subjectstreammap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('clsuid',6);
            $table->string('streamid',3);
            $table->string('clsustid',9)->primary();
            $table->timestamps('timestamp');
            $table->foreign('clsuid')->references('clsuid')->on('classsubjectmap')->ondelete('cascade');
            $table->foreign('streamid')->references('id')->on('stream')->ondelete('cascade');

        });

         Schema::create('streamchaptermap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('clsustid',9);

            $table->string('chapterid',3);
            $table->string('clsustchid',12)->primary();
            $table->timestamps('timestamp');
            $table->foreign('chapterid')->references('id')->on('chapter')->ondelete('cascade');
            $table->foreign('clsustid')->references('clsustid')->on('subjectstreammap')->ondelete('cascade');

        });
         Schema::create('chaptertopicmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('clsustchid',12);
            $table->string('topicid',3);
            $table->string('order',3);
            $table->string('hash',15)->primary();

            $table->timestamps('timestamp');
            $table->foreign('clsustchid')->references('clsustchid')->on('streamchaptermap')->ondelete('cascade');
            $table->foreign('topicid')->references('id')->on('topic')->ondelete('cascade');

        });
       
         Schema::create('role',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->enum('role',array('admin','student','operator','supervisor'));
            $table->timestamps();
        });
         Schema::create('user',function(Blueprint $table){
            $table->engine='InnoDB';
            $table->increments('id');
             $table->string('email');
             $table->string('password');
             $table->boolean('isactive');
             $table->integer('roleid')->unsigned();
              $table->foreign('roleid')->references('id')->on('role')->ondelete('cascade');
         });
        Schema::create('userdetail', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            //$table->string('email');
            $table->string('name');
            $table->boolean('verified');
            $table->string('photopath');
            $table->date('dob');
            $table->string('country')->nullable();
            $table->string('state')->nullable();
            $table->string('city')->nullable();
            $table->integer('phno')->nullable()->unsigned();
            $table->timestamps();
            
        });
       
        Schema::create('questiontype',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('questiontype');
            $table->timestamps();
        });
        Schema::create('question', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('hash',100)->nullable();
            $table->longtext('question');
            $table->string('imagepath')->nullable();
            $table->integer('questiontypeid')->unsigned();
            //$table->datetime('timecreated');
            //$table->timestamps('timemodified');
            $table->integer('createdby')->nullable()->unsigned();
            $table->integer('modifiedby')->nullable()->unsigned();
            $table->string('solutionpath');
            $table->integer('difficulty');
            $table->time('idealattempttime');
            $table->foreign('questiontypeid')->references('id')->on('questiontype')->ondelete('cascade');
            $table->foreign('createdby')->references('id')->on('user')->ondelete('set null');
            $table->foreign('modifiedby')->references('id')->on('user')->ondelete('set  null');
            $table->foreign('hash')->references('hash')->on('chaptertopicmap')->ondelete('set null');

        });
        Schema::create('examtag',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('examname');
            $table->timestamps();
        });

        Schema::create('questiontags',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->integer('questionid')->unsigned();
            $table->integer('tagid')->unsigned();
            $table->foreign('questionid')->references('id')->on('question')->ondelete('cascade');
            $table->foreign('tagid')->references('id')->on('examtag')->ondelete('cascade');
        });

       Schema::create('option', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('questionid')->unsigned();
            $table->longtext('opt');
            $table->boolean('isleft')->nullable();
            $table->timestamps();
            $table->foreign('questionid')->references('id')->on('question')->ondelete('cascade');

        });
         Schema::create('answer', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->integer('questionid')->unsigned();
            $table->longtext('answer');
            $table->timestamps();
            $table->foreign('questionid')->references('id')->on('question')->ondelete('cascade');

        });

         Schema::create('content', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('hash',100)->nullable();
            $table->string('videopath')->nullable();
            $table->string('pdfpath')->nullable();
            $table->integer('createdby')->unsigned()->nullable();
            $table->integer('modifiedby')->unsigned()->nullable;
            //$table->datetime('createdtime');
            $table->timestamps();
            $table->foreign('hash')->references('hash')->on('chaptertopicmap')->ondelete('set null');
            $table->foreign('createdby')->references('id')->on('user')->ondelete('set null');
            $table->foreign('modifiedby')->references('id')->on('user')->ondelete('set  null');

        });




         Schema::create('subscriptiontype', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('subtype');
            $table->boolean('active');
            $table->timestamps();
        });
         Schema::create('order',function(Blueprint $table)
         {
             $table->engine='InnoDB';
             $table->increments('id');
             $table->integer('userid');
             $table->timestamps();
            // $table->integer('paymenttype');
             $table->decimal('amount',10,2);

         });

        Schema::create('subscription', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');

           // $table->integer('by')->unsigned();
            $table->integer('typeid')->unsigned();
            $table->integer('duration')->default(12);
            $table->integer('itemid');
            $table->foreign('typeid')->references('id')->on('subscriptiontype')->ondelete('cascade');
        }); 
         Schema::create('ordersubscriptionmap',function(Blueprint $table){
            $table->engine='InnoDB';
            $table->integer('orderid')->unsigned();
            $table->integer('subid')->unsigned();
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

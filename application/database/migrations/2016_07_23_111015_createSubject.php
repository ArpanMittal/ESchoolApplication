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
            $table->string('Id',3);
            $table->string('ClassName');
            $table->timestamps('timestamp');
            $table->primary('Id');
        });
        Schema::create('stream',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('Id',3);
            $table->string('StreamName');
            $table->timestamps('timestamp');
            $table->primary('Id');
        });
         Schema::create('subject',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('Id',3);
            $table->string('SubjectName');
            $table->timestamps('timestamp');
            $table->primary('Id');
        });


        Schema::create('chapter',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('Id',3);
            $table->string('ChapterName');
            $table->timestamps('timestamp');
            $table->primary('Id');
        });
        Schema::create('topic',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('Id',3);
            $table->string('TopicName');
            $table->timestamps('timestamp');
            $table->primary('Id');
        });
       
        Schema::create('classsubjectmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ClassId',3);
            $table->string('SubjectId',3);
            $table->string('ClSuId',6)->primary();
            $table->timestamps('timestamp');
            $table->foreign('ClassId')->references('Id')->on('class')->onDelete('cascade');
            $table->foreign('SubjectId')->references('Id')->on('subject')->onDelete('cascade');

        });
        Schema::create('subjectstreammap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ClSuId',6);
            $table->string('StreamId',3);
            $table->string('ClSuStId',9)->primary();
            $table->timestamps('timestamp');
            $table->foreign('ClSuId')->references('ClSuId')->on('classsubjectmap')->onDelete('cascade');
            $table->foreign('StreamId')->references('Id')->on('stream')->onDelete('cascade');

        });

         Schema::create('streamchaptermap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ClSuStId',9);

            $table->string('ChapterId',3);
            $table->string('ClSuStChId',12)->primary();
            $table->timestamps('timestamp');
            $table->foreign('ChapterId')->references('Id')->on('chapter')->onDelete('cascade');
            $table->foreign('ClSuStId')->references('ClSuStId')->on('subjectstreammap')->onDelete('cascade');

        });
         Schema::create('chaptertopicmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ClSuStChId',12);
            $table->string('TopicId',3);
            $table->String('Order',3);
            $table->string('Hash',15)->primary();

            $table->timestamps('timestamp');
            $table->foreign('ClSuStChId')->references('ClSuStChId')->on('streamchaptermap')->onDelete('cascade');
            $table->foreign('TopicId')->references('Id')->on('topic')->onDelete('cascade');

        });
       
         Schema::create('role',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->enum('Role',array('Admin','Student','Operator','Supervisor'));
            $table->timestamps();
        });
         Schema::create('user',function(Blueprint $table){
            $table->engine='InnoDB';
            $table->increments('Id');
             $table->string('Email');
             $table->string('Password');
             $table->boolean('IsActive');
             $table->integer('RoleId')->unsigned();
              $table->foreign('RoleId')->references('Id')->on('role')->onDelete('cascade');
         });
        Schema::create('userdetail', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            //$table->string('Email');
            $table->string('Name');
            $table->boolean('Verified');
            $table->string('PhotoPath');
            $table->date('DOB');
            $table->string('Country')->nullable();
            $table->string('State')->nullable();
            $table->string('City')->nullable();
            $table->integer('Phno')->nullable()->unsigned();
            $table->timestamps();
            
        });
       
        Schema::create('questiontype',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('QuestionType');
            $table->timestamps();
        });
        Schema::create('question', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('Hash',100)->nullable();
            $table->longtext('Question');
            $table->string('ImagePath')->nullable();
            $table->integer('QuestionTypeId')->unsigned();
            //$table->dateTime('TimeCreated');
            //$table->timestamps('TimeModified');
            $table->integer('CreatedBy')->nullable()->unsigned();
            $table->integer('ModifiedBy')->nullable()->unsigned();
            $table->string('SolutionPath');
            $table->integer('Difficulty');
            $table->time('IdealAttemptTime');
            $table->foreign('QuestionTypeId')->references('Id')->on('questiontype')->onDelete('cascade');
            $table->foreign('CreatedBy')->references('Id')->on('User')->onDelete('set null');
            $table->foreign('ModifiedBy')->references('Id')->on('User')->onDelete('set  null');
            $table->foreign('Hash')->references('Hash')->on('chaptertopicmap')->onDelete('set null');

        });
        Schema::create('examtag',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('ExamName');
            $table->timestamps();
        });

        Schema::create('questiontags',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->integer('QuestionId')->unsigned();
            $table->integer('TagId')->unsigned();
            $table->foreign('QuestionId')->references('Id')->on('question')->onDelete('cascade');
            $table->foreign('TagId')->references('Id')->on('examtag')->onDelete('cascade');
        });

       Schema::create('option', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->integer('QuestionId')->unsigned();
            $table->longtext('Opt');
            $table->boolean('IsLeft')->nullable();
            $table->timestamps();
            $table->foreign('QuestionId')->references('Id')->on('question')->onDelete('cascade');

        });
         Schema::create('answer', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->integer('QuestionId')->unsigned();
            $table->longtext('Answer');
            $table->timestamps();
            $table->foreign('QuestionId')->references('Id')->on('question')->onDelete('cascade');

        });

         Schema::create('content', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('Hash',100)->nullable();
            $table->string('VideoPath')->nullable();
            $table->string('PdfPath')->nullable();
            $table->integer('CreatedBy')->unsigned()->nullable();
            $table->integer('ModifiedBy')->unsigned()->nullable();
            //$table->dateTime('CreatedTime');
            $table->timestamps();
            $table->foreign('Hash')->references('Hash')->on('chaptertopicmap')->onDelete('set null');
            $table->foreign('CreatedBy')->references('Id')->on('User')->onDelete('set null');
            $table->foreign('ModifiedBy')->references('Id')->on('User')->onDelete('set  null');

        });




         Schema::create('subscriptiontype', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('SubType');
            $table->boolean('Active');
            $table->timestamps();
        });
         Schema::create('order',function(Blueprint $table)
         {
             $table->engine='InnoDB';
             $table->increments('Id');
             $table->integer('UserId');
             $table->timestamps();
            // $table->integer('PaymentType');
             $table->decimal('Amount',10,2);

         });

        Schema::create('subscription', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');

           // $table->integer('By')->unsigned();
            $table->integer('TypeId')->unsigned();
            $table->integer('Duration')->default(12);
            $table->integer('ItemId');
            $table->foreign('TypeId')->references('Id')->on('subscriptiontype')->onDelete('cascade');
        }); 
         Schema::create('ordersubscriptionmap',function(Blueprint $table){
            $table->engine='InnoDB';
            $table->integer('OrderId')->unsigned();
            $table->integer('SubId')->unsigned();
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

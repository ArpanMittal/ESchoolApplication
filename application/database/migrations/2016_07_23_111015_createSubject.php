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
         Schema::create('subject',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('SubjectId',100);
            $table->string('SubjectName');
            $table->timestamps();
            $table->primary('SubjectId');
        });


        Schema::create('chapter',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ChapterId',100);
            $table->string('ChapterName');
            $table->timestamps();
            $table->primary('ChapterId');
        });
        Schema::create('topic',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('TopicId',100);
            $table->string('TopicName');
            $table->timestamps();
            $table->primary('TopicId');
        });
        Schema::create('subtopic',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('SubTopicId',100);
            $table->string('SubTopicName');
            $table->timestamps();
            $table->primary('SubTopicId');
        });
        Schema::create('class',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ClassId',100);
            $table->string('ClassName');
            $table->timestamps();
            $table->primary('ClassId');
        });
        Schema::create('classsubjectmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ClassId',100);
            $table->string('SubjectId',100);
            $table->timestamps();
            $table->foreign('ClassId')->references('ClassId')->on('class')->onDelete('cascade');
            $table->foreign('SubjectId')->references('SubjectId')->on('subject')->onDelete('cascade');

        });
         Schema::create('subjectchaptermap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('SubjectId',100);
            $table->string('ChapterId',100);
            $table->timestamps();
            $table->foreign('ChapterId')->references('ChapterId')->on('chapter')->onDelete('cascade');
            $table->foreign('SubjectId')->references('SubjectId')->on('subject')->onDelete('cascade');

        });
         Schema::create('chaptertopicmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('ChapterId',100);
            $table->string('TopicId',100);
            $table->timestamps();
            $table->foreign('ChapterId')->references('ChapterId')->on('chapter')->onDelete('cascade');
            $table->foreign('TopicId')->references('TopicId')->on('topic')->onDelete('cascade');

        });
        Schema::create('topicsubtopicmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->string('TopicId',100);
            $table->string('SubTopicId',100);
            $table->timestamps();
            $table->foreign('SubTopicId')->references('SubTopicId')->on('subtopic')->onDelete('cascade');
            $table->foreign('TopicId')->references('TopicId')->on('topic')->onDelete('cascade');

        });
         Schema::create('role',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('RoleId');
            $table->string('Role');
            $table->timestamps();
        });
        Schema::create('users', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('Email');
            $table->string('Name');
            $table->boolean('Verified');
            $table->integer('RoleId')->unsigned();
            $table->integer('Phno')->nullable()->unsigned();
            $table->string('Country')->nullable();
            $table->string('State')->nullable();
            $table->string('City')->nullable();
            $table->timestamps();
            $table->unique('Email');
             $table->foreign('RoleId')->references('RoleId')->on('role')->onDelete('cascade');
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
            $table->string('ContentPath',100);
            $table->longtext('Question');
            $table->string('ImagePath')->nullable();
            $table->integer('QuestionTypeId')->unsigned();
            $table->dateTime('TimeCreated');
            $table->timestamps('TimeModified');
            $table->integer('CreatedBy')->nullable()->unsigned();
            $table->integer('ModifiedBy')->nullable()->unsigned();
            $table->string('SolutionPath');
            $table->foreign('QuestionTypeId')->references('Id')->on('questiontype')->onDelete('cascade');
            $table->foreign('CreatedBy')->references('Id')->on('Users')->onDelete('set null');
            $table->foreign('ModifiedBy')->references('Id')->on('Users')->onDelete('set null');

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
            $table->string('ContentPath',100);
            $table->string('VideoPath')->nullable();
            $table->string('PdfPath')->nullable();
            $table->integer('CreatedBy')->unsigned();
            $table->dateTime('CreatedTime');
            $table->timestamps();

        });

         Schema::create('subscriptiontype', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('Id');
            $table->string('SubType');
            $table->boolean('Active');
            $table->timestamps();
        });

         Schema::create('subscription', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->integer('Id')->unsigned();
            $table->integer('By')->unsigned();
            $table->integer('TypeId')->unsigned();
            $table->integer('To')->unsigned();
            $table->dateTime('Date');
            $table->integer('Duration')->default(12);
            $table->decimal('Amount',10,2);
            $table->timestamps();
            $table->foreign('By')->references('Id')->on('users');
            $table->foreign('TypeId')->references('Id')->on('subscriptiontype')->onDelete('cascade');
        }); 

         Schema::create('usersubjectmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->integer('UserId',100)->unsigned();
            $table->string('SubjectId',100);
            $table->timestamps();
            $table->foreign('UserId')->references('Id')->on('users')->onDelete('cascade');
            $table->foreign('SubjectId')->references('SubjectId')->on('subject')->onDelete('cascade');

        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
         Schema::drop('users');
        Schema::drop('usersubjectmap');
        Schema::drop('subscription');
        Schema::drop('subscriptiontype');
        Schema::drop('answer');
        Schema::drop('chapter');
        Schema::drop('chaptertopicmap');
        Schema::drop('class');
        Schema::drop('classsubjectmap');
        Schema::drop('content');
        Schema::drop('option');
        Schema::drop('question');
        Schema::drop('questiontype');
        Schema::drop('role');
        Schema::drop('subject');
        Schema::drop('subjectchaptermap');
        Schema::drop('subtopic');
        Schema::drop('topic');
        Schema::drop('topicsubtopicmap');
    }
}

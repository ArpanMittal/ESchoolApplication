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
        Schema::table('package', function ($table) {
            $table->integer('duration')->default('12');
        });

        Schema::table('subscription', function ($table) {
            $table->dropColumn('item_id');
        });
        
        
        Schema::create('pack_subject_map',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('pack_id')->unsigned();
            $table->string('subject_id');
            $table->Foreign('pack_id')->references('id')->on('package')->onDelete('cascade');
            $table->Foreign('subject_id')->references('cl_su_id')->on('classsubjectmap')->onDelete('cascade');;
        });

        Schema::create('pack_subject_chapter_map',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('pack_subject_id')->unsigned();
            $table->string('chapter_id');
            $table->Foreign('pack_subject_id')->references('id')->on('pack_subject_map')->onDelete('cascade');
            $table->Foreign('chapter_id')->references('cl_su_st_ch_id')->on('streamchaptermap')->onDelete('cascade');;
        });

        Schema::create('pack_subject_chapter_topic_map',function(Blueprint $table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('pack_subject_chapter_id')->unsigned();
            $table->string('topic_id');
            $table->Foreign('pack_subject_chapter_id')->references('id')->on('pack_subject_chapter_map')->onDelete('cascade');
            $table->Foreign('topic_id')->references('hash')->on('chaptertopicmap')->onDelete('cascade');;
        });
        Schema::table('subscription', function ($table) {
            $table->dropForeign('subscription_type_id_foreign');
            $table->dropColumn('type_id');
        });
        Schema::dropIfExists('subscriptiontype');
        Schema::dropIfExists('packagesubmap');
        Schema::dropIfExists('subscription');

        
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

        Schema::create('subscription', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->datetime('created_at');


            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
            // $table->integer('By')->unsigned();
            $table->integer('type_id')->unsigned();
            $table->integer('duration')->default(12);
            $table->integer('item_id');
            $table->foreign('type_id')->references('id')->on('subscriptiontype')->onDelete('cascade');
        });

        Schema::create('packagesubmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('pack_id')->unsigned();
            $table->integer('sub_id')->unsigned();
            $table->foreign('pack_id')->references('id')->on('package')->onDelete('cascade');
            $table->foreign('sub_id')->references('id')->on('subscription')->onDelete('cascade');

        });

        Schema::create('subscriptiontype', function (Blueprint $table) {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('sub_type');
            $table->boolean('active');
            $table->datetime('created_at');


            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
        });
        Schema::table('subscription', function ($table) {
            $table->string('item_id');
            $table->integer('type_id');
            $table->foreign('type_id')->references('id')->on('subscriptiontype')->onDelete('cascade');
        });

        Schema::table('package', function ($table) {
            $table->dropColumn('duration');
        });
    }
}

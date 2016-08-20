<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class PackageTables extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::drop('ordersubscriptionmap');

        Schema::create('package',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('package_name');
            $table->float('cost');
            $table->datetime('created_at');
            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));

        });

        Schema::create('packagesubmap',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('pack_id')->unsigned();
            $table->integer('sub_id')->unsigned();
            $table->foreign('pack_id')->references('id')->on('package')->onDelete('cascade');
            $table->foreign('sub_id')->references('id')->on('subscription')->onDelete('cascade');

        });

        Schema::create('ordersubmap',function(Blueprint$table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('order_id')->unsigned();
            $table->integer('pack_id')->unsigned();
            $table->foreign('order_id')->references('id')->on('order')->onDelete('cascade');
            $table->foreign('pack_id')->references('id')->on('package')->onDelete('cascade');

        });

        Schema::create('exampackmap',function(Blueprint$table)
        {
            $table->engine='InnoDB';
            $table->increments('id');
            $table->integer('pack_id')->unsigned();
            $table->integer('exam_id')->unsigned();
            $table->foreign('pack_id')->references('id')->on('package')->onDelete('cascade');
            $table->foreign('exam_id')->references('id')->on('examtag')->onDelete('cascade');

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
        Schema::drop('package');
        Schema::drop('packagesubmap');
        Schema::drop('ordersubmap');
        Schema::drop('exampackmap');
    }
}

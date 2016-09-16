<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class SchoolTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('school',function(Blueprint$table){
            $table->engine='InnoDB';
            $table->increments('id');
            $table->string('school_name');
            $table->datetime('created_at');
            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));

        });

        Schema::table('userdetail', function ($table) {
            $table->integer('school_id')->unsigned()->nullable();;
            $table->foreign('school_id')->references('id')->on('school')->onDelete('SET NULL');
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

        Schema::table('userdetail', function ($table) {
            $table->dropColumn('school_id');
        });
        drop('school');
    }
}

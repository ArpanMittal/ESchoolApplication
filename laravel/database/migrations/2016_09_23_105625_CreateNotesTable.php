<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateNotesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('notes',function(Blueprint$table){
            $table->engine='InnoDB';

            $table->integer('student_id')->unsigned();
            $table->integer('note_id')->unsigned();
            $table->string('title');
            $table->string('body');
            $table->date('date');
            $table->integer('type')->unsigned();
            $table->foreign('student_id')->references('id')->on('user')->onDelete('cascade');
            $table->timestamp('updated_at')->default(DB::raw('CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP'));
        });
        DB::statement("ALTER TABLE notes ADD image MEDIUMBLOB");
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
        drop('notes');
    }
}

<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Addquestionoption extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('user_attempt_response', function ($table) {
            $table->integer('option_id')->unsigned();
            $table->foreign('option_id')->references('id')->on('option')->onDelete('cascade');
            $table->string('response')->default("empty")->change();
        });


        //
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}

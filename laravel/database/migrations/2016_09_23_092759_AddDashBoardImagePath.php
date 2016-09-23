<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\Schema;

class AddDashBoardImagePath extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::table('class', function($table)
        {
            $table->string('image');
        });

        Schema::table('subject', function($table)
        {
            $table->string('image');
        });

        Schema::table('examtag', function($table)
        {
            $table->string('image');
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
        Schema::table('class', function($table)
        {
            $table->dropColumn('image');
        });
        Schema::table('subject', function($table)
        {
            $table->dropColumn('image');
        });
        Schema::table('examtag', function($table)
        {
            $table->dropColumn('image');
        });
    }
}

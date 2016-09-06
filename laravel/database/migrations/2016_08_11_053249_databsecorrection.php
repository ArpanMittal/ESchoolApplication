<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;
use \Illuminate\Support\Facades\Schema;
class Databsecorrection extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::table('user', function ($table) {
            $table->dropColumn(['token_id']);
            $table->boolean('is_active')->default(true)->change();
        });
        Schema::table('userdetail', function ($table) {
            $table->dropColumn(['verified']);
            $table->string('photo_path')->nullable()->change();
            $table->date('date_of_birth')->nullable()->change();
        });

       /* Schema::table('user', function ($table) {

        });

        Schema::table('userdetail', function ($table) {

        });*/
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

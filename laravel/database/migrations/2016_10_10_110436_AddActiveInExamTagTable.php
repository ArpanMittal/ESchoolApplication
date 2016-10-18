<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddActiveInExamTagTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::table('exam_state_year_rest_map', function ($table) {
            $table->boolean('is_active',1)->default(false)->unsigned();
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
        Schema::table('exam_state_year_rest_map', function ($table) {
            $table->dropColumn('is_active');
        });
    }
}

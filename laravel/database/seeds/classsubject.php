<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class classsubject extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        //

        $faker = Faker::create();
        $classids = array('C14','C16','C17','C22');
        $subjectids = DB::table('subject')->pluck('id');
        foreach (range(0,100) as $index){
            $cl = $faker->randomElement($classids);
            $su = $faker->unique()->randomElement($subjectids);
            DB::table('classsubjectmap')->insert([
                'class_id' =>  $cl,
                'subject_id' =>  $su,
                'cl_su_id' => $cl.$su
            ]);
        }
    }
}

<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class subjectstream extends Seeder
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
        $streamids = DB::table('stream')->pluck('id');
        $clsuids = DB::table('classsubjectmap')->pluck('cl_su_id');
        foreach (range(0,1000) as $index){
            $st = $faker->randomElement($streamids);
            $cl_su_id = $faker->randomElement($clsuids);
            DB::table('subjectstreammap')
                ->where('cl_su_st_id','=',$cl_su_id.$st)
                ->updateOrInsert([
                'stream_id' =>  $st,
                'cl_su_id' =>  $cl_su_id,
                'cl_su_st_id' => $cl_su_id.$st
            ]);
        }
    }
}

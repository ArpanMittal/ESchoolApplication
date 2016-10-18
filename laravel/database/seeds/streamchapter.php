<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class streamchapter extends Seeder
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
        $streamids = DB::table('chapter')->pluck('id');
        $clsuids = DB::table('subjectstreammap')->pluck('cl_su_st_id');
        foreach (range(0,2000) as $index){
            $st = $faker->unique()->randomElement($streamids);
            $cl_su_id = $faker->randomElement($clsuids);
            DB::table('streamchaptermap')->where('cl_su_st_ch_id','=',$cl_su_id.$st)
                ->updateOrInsert([
                'chapter_id' =>  $st,
                'cl_su_st_id' =>  $cl_su_id,
                'cl_su_st_ch_id' => $cl_su_id.$st
            ]);
        }
    }
}

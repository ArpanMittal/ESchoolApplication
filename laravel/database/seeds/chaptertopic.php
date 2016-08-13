<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class chaptertopic extends Seeder
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
        $streamids = DB::table('topic')->pluck('id');
        $clsuids = DB::table('streamchaptermap')->pluck('cl_su_st_ch_id');
        foreach (range(0,10000) as $index){
            $st = $faker->unique()->randomElement($streamids);
            $cl_su_id = $faker->randomElement($clsuids);
            DB::table('chaptertopicmap')->where('hash','=',$cl_su_id.$st)
                ->updateOrInsert([
                'topic_id' =>  $st,
                'cl_su_st_ch_id' =>  $cl_su_id,
                'hash' => $cl_su_id.$st,
                'order' => $index
            ]);
        }
    }
}

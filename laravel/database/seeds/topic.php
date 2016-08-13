<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class topic extends Seeder
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

        foreach (range(0,10000) as $index){
            $id = $faker->unique()->regexify('[A-Z][a-z][a-z]');
            DB::table('topic')
                ->where('id','=',$id)
                ->updateOrInsert([
                'id' => $id,
                'topic_name' => $faker->sentence(5)
            ]);
        }
    }
}

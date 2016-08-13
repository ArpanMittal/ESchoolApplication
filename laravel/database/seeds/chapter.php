<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class chapter extends Seeder
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
        foreach (range(0,2000) as $index){
            $id = $faker->unique()->regexify('[A-Z][a-z][a-z]');
            DB::table('chapter')->where('id','=',$id)
                ->updateOrInsert([
                'id' => $id,
                'chapter_name' => $faker->sentence(5)
            ]);
        }
    }
}

<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class subject extends Seeder
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
        foreach (range(0,100) as $index){
            $id = $faker->unique()->regexify('[A-Z][a-z]');
            DB::table('subject')->where('id','=',$id)->updateOrInsert([
                'id' => $id,
                'subject_name' => $faker->sentence(5)
            ]);
        }
    }
}

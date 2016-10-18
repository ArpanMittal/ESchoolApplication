<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class classs extends Seeder
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

        foreach (range(0,20) as $index){
            $id = $faker->unique()->regexify('[C][0-9][0-9]');
            DB::table('class')->where('id','=',$id)->updateOrInsert([
                'id' => $id,
                'class_name' => $faker->sentence(5)
            ]);
        }
    }
}

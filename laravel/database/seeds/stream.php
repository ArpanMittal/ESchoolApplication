<?php

use Illuminate\Database\Seeder;
use Faker\Factory as Faker;
use Illuminate\Support\Facades\DB;

class stream extends Seeder
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
        
        foreach (range(0,500) as $index){
            $id = $faker->unique()->regexify('[A-Z][a-z]');
            DB::table('stream')->where('id','=',$id)->updateOrInsert([
                'id' => $id,
                'stream_name' => $faker->sentence(5)
            ]);
        }
    }
}

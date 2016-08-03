<?php

use Illuminate\Database\Seeder;
use Illuminate\Database\Eloquent\Model;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
    	//Eloquent::unguard();
    	//Eloquent::unguard();
    	/*DB::table('role')->insertGetId{[
        	'Role'=>str_random(4),
        	'created_at' => \Carbon\Carbon::now()->toDateTimeString(),
        	'updated_at' => \Carbon\Carbon::now()->toDateTimeString()
        	]};*/

        // $this->call(UsersTableSeeder::class);
        $this->call(CheckSeeder::class);
    }
}

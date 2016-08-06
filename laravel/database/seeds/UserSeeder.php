<?php

use Illuminate\Database\Seeder;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        //
        //DB::table('user')->truncate();
        DB::table('user')->insert(array(
        "email"=>"arpanmittal@123.gmail.com",
        "password"=>'hello',
        "is_active"=>false,
        "role_id"=>1,
        "token_id"=>"khfdjjkdsfkjjn",
        ));

    }
}

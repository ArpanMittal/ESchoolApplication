<?php

use Illuminate\Database\Seeder;

class OAuthUsersSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        //
        DB::table('oauth_users')->truncate();
        DB::table('oauth_users')->insert(array(
            'username' => "bshaffer",
            'password' => sha1("brent123"),
            'first_name' => "Brent",
            'last_name' => "Shaffer",
        ));
    }
}

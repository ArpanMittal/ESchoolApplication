<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
         $this->call(UsersTableSeeder::class);
        $this->call('OAuthUsersSeeder');
        $this->call('OAuthClientsSeeder');
        $this->call('UserSeeder');
        $this->call('classs');
        $this->call('subject');
        $this->call('stream');
        $this->call('chapter');
        $this->call('topic');
        $this->call('classsubject');
        $this->call('subjectstream');
        $this->call('streamchapter');
        $this->call('chaptertopic');
    }
}

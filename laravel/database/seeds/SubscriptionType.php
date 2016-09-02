<?php

use Illuminate\Database\Seeder;

class SubscriptionType extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        //
        DB::table('subscriptiontype')->insert(array(
            "sub_type"=>"Subject",
            "active"=>1,
        ));
        DB::table('subscriptiontype')->insert(array(
            "sub_type"=>"Chapter",
            "active"=>1,
        ));
        DB::table('subscriptiontype')->insert(array(
            "sub_type"=>"Topic",
            "active"=>1,
        ));
    }
}

<?php
namespace App\Http\Controllers;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;
use OAuth2\Storage\Pdo;
use Illuminate\Support\Facades\DB;

/**
 * Created by PhpStorm.
 * User: arpan
 * Date: 8/5/2016
 * Time: 6:06 PM
 */
class MyPdo extends Pdo{
    function __construct($connection,$config=array())
    {
        parent::__construct($connection,$config);
        $this->config['user_table']='user';

    }

    public function checkUserCredentials($username, $password)
    {
        //file_put_contents("a.txt",$username.$password.$this->config['user_table']);
        if(DB::table($this->config['user_table'])->where('email',$username)->where('password',$password)->get())
        {

            return true;
        }

        return false;
    }

    public function getUser($username)
    {

        $stmt = $this->db->prepare($sql = sprintf("SELECT * from %s where email=:username", $this->config['user_table']));
        $stmt->execute(array('username' => $username));

        if (!$userInfo = $stmt->fetch(\PDO::FETCH_ASSOC)) {

            return false;
        }

        // the default behavior is to use "username" as the user_id
        return array_merge(array(
            'user_id' => $username
        ), $userInfo);
    }


    public function setAccessToken($access_token, $client_id, $user_id, $expires, $scope = null)
    {
        // convert expires to datestring
        $expires = date('Y-m-d H:i:s', $expires);

        /*if(!is_numeric($user_id))
        {

           $userData=DB::table('user')->where('email', $user_id)->get();
            $user_id=$userData->id;

        }*/

        // if it exists, update it.
        if ($this->getAccessToken($access_token)) {
            $stmt = $this->db->prepare(sprintf('UPDATE %s SET client_id=:client_id, expires=:expires, user_id=:user_id, scope=:scope where access_token=:access_token', $this->config['access_token_table']));
        } else {
            $stmt = $this->db->prepare(sprintf('INSERT INTO %s (access_token, client_id, expires, user_id, scope) VALUES (:access_token, :client_id, :expires, :user_id, :scope)', $this->config['access_token_table']));
        }

        return $stmt->execute(compact('access_token', 'client_id', 'user_id', 'expires', 'scope'));
    }

    public function setRefreshToken($refresh_token, $client_id, $user_id, $expires, $scope = null)
    {
        /*if(!is_numeric($user_id))
        {
            $userData=DB::table('user')->where('email', $user_id)->get();
            $user_id=$userData->id;
            //$user_id=1;
        }*/
        // convert expires to datestring
        $expires = date('Y-m-d H:i:s', $expires);

        $stmt = $this->db->prepare(sprintf('INSERT INTO %s (refresh_token, client_id, user_id, expires, scope) VALUES (:refresh_token, :client_id, :user_id, :expires, :scope)', $this->config['refresh_token_table']));

        return $stmt->execute(compact('refresh_token', 'client_id', 'user_id', 'expires', 'scope'));
    }
}
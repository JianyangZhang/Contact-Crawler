<?php
// include ('searchController.php');







        $id="";
        if(isset($_GET['searchId'])) $id=$_GET['searchId'];
        

        // $serverName = '54.69.239.219';
        // $userName = 'lyihan';
        // $password= '900225';
        // $database = 'EmailCrawlerDB';

        $serverName = 'localhost';
        $userName = 'root';
        $password= '';
        $database = 'EmailCrawlerDB';

        $con = mysqli_connect($serverName,$userName,$password,$database);
        if (!$con)
        {
            echo("fail");
            die('Could not connect: ' . mysql_error());
        }
        

        

        $sql1='select customer_linkedin_url from Result where search_id="'.$id.'" ;';
                // $sql1='select * from Result where search_id="20170621234247153";';
        $result=mysqli_query($con, $sql1 );
        $response=array();
        while($row = mysqli_fetch_array($result,MYSQLI_NUM)){
            $response[]=$row[0];

        }

         $response2=array();
        foreach ($response as $url) {
            $sql2='select customer_name from Customer where customer_linkedin_url="'.$url.'";';

            $result2=mysqli_query($con, $sql2 );
            $response2[]= mysqli_fetch_row($result2);
        }
        $response2plus=array();
        foreach ($response2 as $res) {
            $response2plus[]=utf8_encode($res[0]);
        }

        $response3=array();
        foreach ($response as $url) {
            $sql3='select customer_title from Customer where customer_linkedin_url="'.$url.'";';

            $result3=mysqli_query($con, $sql3 );
            $response3[]=mysqli_fetch_row($result3);
        }
         $response3plus=array();
        foreach ($response3 as $res) {
            $response3plus[]=utf8_encode($res[0]);
        }


        $response4=array();
        foreach ($response as $url) {
            $sql4='select email_address from Email where customer_linkedin_url="'.$url.'";';
            $email=array();     
            $result4=mysqli_query($con, $sql4 );
            while($row = mysqli_fetch_row($result4)){
                $email[]=utf8_encode($row[0]);
                // echo $row[0]."\n";
            }
            
            array_push($response4,$email);

            
            
        }



// var_dump( $response4);
        

        $result=array($response,$response2plus,$response3plus,$response4);
// var_dump($result);

        echo json_encode($result);
         // echo json_last_error_msg();

        mysqli_close($con);


    
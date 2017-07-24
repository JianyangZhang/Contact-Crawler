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
        

        

        $sql1='select sg_person_name, sg_phone_number, sg_company_name from Result_SG where search_id="'.$id.'" ;';
                // $sql1='select sg_person_name, sg_phone_number, sg_company_name from Result_SG where search_id="20170711184107615";';
        $result=mysqli_query($con, $sql1 );
        $response=array();
        while($row = mysqli_fetch_array($result,MYSQLI_NUM)){
            $response[]=$row;

        }

         $response2=array();
        foreach ($response as $url) {
            $sql2='select sg_title, sg_city, sg_state from SalesGenie where sg_person_name="'.$url[0].'"and sg_phone_number ="'.$url[1].'" and sg_company_name="'.$url[2].'";';

            $result2=mysqli_query($con, $sql2 );
            $response2[]= mysqli_fetch_row($result2);
        }
       

        
            
            
        




        

        $result=array($response,$response2);


        echo json_encode($result);
         // echo json_last_error_msg();

        mysqli_close($con);


    
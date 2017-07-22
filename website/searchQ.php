<?php
// include ('searchController.php');




		



		$title="";
		$city ="";
		$count="";
		$searchFrom="linkedin";
		$lkdemail="";
		$lkdpassword="";

		if(isset($_GET['title'])) $title=$_GET['title'];
		if(isset($_GET['city'])) $city=$_GET['city'];
		if(isset($_GET['count'])) $count=$_GET['count'];
		if($_GET['linkedin_email']!='') $lkdemail=$_GET['linkedin_email'];
		if($_GET['linkedin_password']!='') $lkdpassword=$_GET['linkedin_password'];

		if($_GET['count']==''|| $_GET['count']=='0') $count="10";

		if($_GET['searchFrom']=='0') $searchFrom = 'linkedin';
		if($_GET['searchFrom']=='1') $searchFrom = 'salegenie';
        // return response()->json([ $title,$city,$count]);

		// $serverName = '54.69.239.219';
		// $userName = 'lyihan';
		// $password= '900225';
		// $database = 'EmailCrawlerDB';


		$serverName = 'kiwispider.com';
		$userName = 'root';
		$password= '';
		$database = 'EmailCrawlerDB';
		$con = mysqli_connect($serverName,$userName,$password,$database);
		if (!$con)
		{
			echo("fail");
			die('Could not connect: ' . mysql_error());
		}
		$id=date("YmdHis");
		function get_millisecond()  
		{  
			list($usec, $sec) = explode(" ", microtime());  
			$msec=round($usec*1000);  
			return $msec;               
		} 
		$ms=get_millisecond(); 
		$id=$id.$ms;

		session_start();
		$userId=$_SESSION['userId'];
		$internalCompany=$_SESSION['internalCompany'];

		$sql1='insert into Search values("'.$id.'","'.$count.'","'.$title.'","'.$city.'","150","'.$searchFrom.'","pending","'.$userId.'","'.$internalCompany.'",0,"'.$lkdemail.'","'.$lkdpassword.'");';
		$result=mysqli_query($con, $sql1 );

		$sql2='select search_id,search_keywords,search_location,search_from,search_progress, has_deleted from Search where user_id="'.$userId.'" and internal_company_id="'.$internalCompany.'";';

		// $sql2='select search_id,search_keywords,search_progress from Search where user_id="ryan" and internal_company_id="BHC";';

		$result2=mysqli_query($con,$sql2);
		$response=array();
		while($row = mysqli_fetch_array($result2,MYSQLI_NUM)){
			$response[]=$row;

		}
		$response2=array();
		foreach ($response as $searchid){
			$sql4='select search_from from Search where search_id="'.$searchid[0].'";';
			$fromresult=mysqli_query($con,$sql4);
			$from=mysqli_fetch_array($fromresult,MYSQLI_NUM);
			
			if($from[0]=='linkedin'){
				$sql3='select count(customer_linkedin_url) from Result where search_id="'.$searchid[0].'";';
		$result3=mysqli_query($con,$sql3);
		$count=mysqli_fetch_array($result3,MYSQLI_NUM);
		$response2[]=$count[0];
		
	}else{
		$sql3='select count(sg_person_name) from Result_SG where search_id ="'.$searchid[0].'";';
		$result3=mysqli_query($con,$sql3);
		$count=mysqli_fetch_array($result3,MYSQLI_NUM);
		$response2[]=$count[0];

	}
			
		}
		// echo json_encode($response2);

		$response3=array();
		$response3=[$response,$response2];	
  	
		
		
  		echo json_encode($response3);
  		

		mysqli_close($con);


	
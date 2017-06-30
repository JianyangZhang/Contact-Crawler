<?php
// include ('searchController.php');




		



		$title="";
		$city ="";
		$count="";

		if(isset($_GET['title'])) $title=$_GET['title'];
		if(isset($_GET['city'])) $city=$_GET['city'];
		if(isset($_GET['count'])) $count=$_GET['count'];
        // return response()->json([ $title,$city,$count]);

		$serverName = '54.69.239.219';
		$userName = 'lyihan';
		$password= '900225';
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

		$sql1='insert into Search values("'.$id.'","'.$count.'","'.$title.'","'.$city.'","150","pending","'.$userId.'","'.$internalCompany.'",0);';
		$result=mysqli_query($con, $sql1 );

		$sql2='select search_id,search_keywords,search_progress, has_deleted from Search where user_id="'.$userId.'" and internal_company_id="'.$internalCompany.'";';

		// $sql2='select search_id,search_keywords,search_progress from Search where user_id="ryan" and internal_company_id="BHC";';

		$result2=mysqli_query($con,$sql2);
		$response=array();
		while($row = mysqli_fetch_array($result2,MYSQLI_NUM)){
			$response[]=$row;

		}
  		echo json_encode($response);

		mysqli_close($con);


	
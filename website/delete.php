<?php
// include ('searchController.php');
// namespace App\Http\Controllers;
// use Illuminate\Http\Request;






		
		$id="";
		session_start();
		$userId=$_SESSION['userId'];
		$internalCompany=$_SESSION['internalCompany'];

		if(isset($_GET['id'])) $id=$_GET['id'];
		

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
		

		$sql1='update Search set has_deleted= 1 where search_id="'.$id.'";';
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


	
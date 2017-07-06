<?php
// include ('searchController.php');




		



		


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
		

		session_start();
		$userId=$_SESSION['userId'];
		$internalCompany=$_SESSION['internalCompany'];

		

		$sql2='select search_id,search_keywords,search_progress, has_deleted from Search where user_id="'.$userId.'" and internal_company_id="'.$internalCompany.'";';

		// $sql2='select search_id,search_keywords,search_progress from Search where user_id="ryan" and internal_company_id="BHC";';

		$result2=mysqli_query($con,$sql2);
		$response=array();
		while($row = mysqli_fetch_array($result2,MYSQLI_NUM)){
			$response[]=$row;

		}
		$response2=array();
		foreach ($response as $searchid){
			$sql3='select count(customer_linkedin_url) from Result where search_id="'.$searchid[0].'";';
		$result3=mysqli_query($con,$sql3);
		$count=mysqli_fetch_array($result3,MYSQLI_NUM);
		$response2[]=$count[0];
		}
		// echo json_encode($response2);

		$response3=array();
		$response3=[$response,$response2];	
  	
		
		
  		echo json_encode($response3);
  		

		mysqli_close($con);


	
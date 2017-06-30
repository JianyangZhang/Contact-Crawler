<?php






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

			// some code
			  // echo("succ");
		$userName="";
		$password="";
		$internalCompany;
		if(isset($_POST['userName'])) $userName=$_POST['userName'];
		if(isset($_POST['password'])) $password=$_POST['password'];
		// echo($_POST['internalCompany']);
		if($_POST['internalCompany']=="0") $internalCompany='CT';
		if($_POST['internalCompany']=="1") $internalCompany='BHC';
		$userId=$_POST['userName'];

		session_start();
		$_SESSION['internalCompany']=$internalCompany;
		$_SESSION['userId']=$userId;

		$sql='select user_id from User where user_name="'.$userName.'" and user_password="'.$password.'";';
		$result=mysqli_query($con,$sql);
		if($userName=mysqli_fetch_row($result)){
			header("Location: search.html");
			exit;
		}
		// var_dump( mysqli_fetch_row($result));

		// var_dump($_SESSION);

		
		// $sql2='insert into Linkedin_Account values("'.$lkinid.'","'.$email.'","'.$password.'");';
		// $result=mysqli_query($con,$sql2);
		// mysqli_close($con);
		else{


		// echo "<script language=\"JavaScript\">alert(\"Wrong username or password\");</script>";
		
		header("refresh:0;url=http://localhost/~wentao/findemail/welcome.html");

		echo "<script language=\"JavaScript\">alert(\"Wrong username or password\");</script>";
		
			
	
}
	


	// public function searchQ(Request $request){
	// 	if ($request->isMethod('post')){    
	// 		return response()->json(['response' => 'This is post method']); 
	// 	}        
	// }






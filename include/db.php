<?php
	$connect = mysqli_connect($DB_HOST . ':' . $DB_PORT, $DB_USERNAME, $DB_PASSWORD);	
	mysqli_select_db($connect, $DB_NAME);
?>

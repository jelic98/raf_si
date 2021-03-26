<?php
	$service = get_service();
	$service_path = get_service_path($service);

	$cmd = "SELECT host, port 
		FROM `services` 
		WHERE `name`='$service';";
	$result = query_mysql($cmd);
	$row = mysqli_fetch_assoc($result);

	if(is_null($row)) {
		error(404);
	}

	$host = $row['host'];
	$port = $row['port'];

	$curl = curl_init();

	curl_setopt_array($curl, [
		CURLOPT_URL => "$host:$port/$service_path",
		CURLOPT_CUSTOMREQUEST => $_SERVER['REQUEST_METHOD'],
		CURLOPT_POSTFIELDS => $_REQUEST
	]);

	curl_exec($curl);
	curl_close($curl);
?>

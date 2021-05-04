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
	
	$method = $_SERVER['REQUEST_METHOD'];

	if($method == 'GET' || $method == 'POST') {
		$data = $_REQUEST;
	}else {
		$data = [];
		parse_str(file_get_contents("php://input"), $data);
	}

	$curl = curl_init();

	curl_setopt_array($curl, [
		CURLOPT_URL => "$host:$port/$service_path",
		CURLOPT_CUSTOMREQUEST => $method,
		CURLOPT_POSTFIELDS => $data,
		CURLOPT_RETURNTRANSFER => true,
		CURLOPT_FOLLOWLOCATION => true,
		CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
		CURLOPT_HTTPHEADER => [
			'Content-Length: ' . getallheaders()['Content-Length'],
			'Authorization: ' . getallheaders()['Authorization']
		]
	]);

	$response = curl_exec($curl);
	$code = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	
	curl_close($curl);

	http_response_code($code);
	echo $response;
?>

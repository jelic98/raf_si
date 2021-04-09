<?php
	function clean_request() {
		global $connect;

		foreach($_REQUEST as $key => &$value) {
			$value = htmlspecialchars(strip_tags(trim($value)));
			$value = mysqli_real_escape_string($connect, $value);
		}
	}

	function check_request($params, $path, $method) {
		global $SERVICE;

		$schema = file_get_contents('../' . $SERVICE . '/schema/' . $path . '_' . $method . '.json');
		
		foreach($params as $p) {
			if(!isset($_REQUEST[$p])) {
				error(400, $schema);
			}
		}
	}

	function get_route() {
		$route = $_SERVER['REQUEST_URI'];
		$sep = strpos($route, '/', 1);
		$route = substr($route, 0, $sep === false ? strlen($route) : $sep);

		return $route;
	}

	function get_path() {
		$path = $_SERVER['REQUEST_URI'];
		$sep = strpos($path, '/', 1);
		$path = $sep === false ? "" : substr($path, $sep + 1);

		return $path;
	}
	
	function get_service() {
		$service = $_SERVER['REQUEST_URI'];
		$sep = strpos($service, '/', 1);
		$service = substr($service, 1, $sep - 1);

		return $service;
	}

	function get_service_path($service) {
		$path = $_SERVER['REQUEST_URI'];
		$sep = strpos($path, $service);
		$path = substr($path, $sep + strlen($service) + 1);

		return $path;
	}

	function error_msg($code, $message, $schema=NULL) {
		$messages = [
			400 => 'Bad Request',
			404 => 'Not Found',
			405 => 'Method Not Allowed',
			500 => 'Internal Server Error'
		];
		
		$response = [];
		$response['code'] = $code;
		$response['message'] = is_null($message) ? $messages[$code] : $message;

		if($code == 400 && !is_null($schema)) {
			$response['schema'] = json_decode($schema);
		}

		http_response_code($code);
		die(json_encode($response));
	}

	function error($code, $schema=NULL) {
		error_msg($code, NULL, $schema);
	}

	function query_mysql($cmd) {
		global $connect;

		$result = mysqli_query($connect, $cmd);

		return $result ? $result : error_msg(500, mysqli_error($connect));
	}

	function json_mysql($result) {
		$json = [];
		
		while($row = mysqli_fetch_assoc($result)) {
			$json[] = $row;
		}

		return json_encode($json);
	}
?>

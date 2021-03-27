<?php
	switch($_SERVER['REQUEST_METHOD']) {
		case 'POST':
			check_request(['name', 'host', 'port']);

			$name = $_REQUEST['name'];
			$host = $_REQUEST['host'];
			$port = $_REQUEST['port'];

			$cmd = "INSERT INTO `services`
				(`name`, `host`, `port`) 
				VALUES ('$name', '$host', $port)
				ON DUPLICATE KEY UPDATE
				`host`='$host', `port`=$port;";
			query_mysql($cmd);

			break;
		case 'GET':
			$cmd = "SELECT * FROM `services`;";
			$result = query_mysql($cmd);

			echo(json_mysql($result));

			break;
		default:
			error(405);
			break;
	}
?>

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
			$services = [];
			
			$cmd = "SELECT * FROM `services`;";
			$result = query_mysql($cmd);

			while($row = mysqli_fetch_assoc($result)) {
				$services[] = $row;
			}

			echo(json_encode($services));

			break;
		default:
			error(405);
			break;
	}
?>

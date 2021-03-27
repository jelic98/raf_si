<?php		
	require('./secret/db.php');
	require('../include/autoload.php');
	
	clean_request();

	switch($_SERVER['REQUEST_METHOD']) {
		case 'POST':
			check_request(['host', 'port', 'service', 'message']);

			$host = $_REQUEST['host'];
			$port = $_REQUEST['port'];
			$service = $_REQUEST['service'];
			$message = $_REQUEST['message'];

			$cmd = "INSERT INTO `logs`
				(`host`, `port`, `service`, `message`) 
				VALUES ('$host', $port, '$service', '$message');";
			query_mysql($cmd);

			break;
		case 'GET':	
			$cmd = "SELECT * FROM `logs`;";
			$result = query_mysql($cmd);

			echo(json_mysql($result));

			break;
		default:
			error(405);
			break;
	}
?>

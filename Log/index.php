<?php		
	require('./secret/db.php');
	require('../include/autoload.php');

	header('Access-Control-Allow-Origin: *');
	header('Access-Control-Allow-Headers: *');
	header('Access-Control-Allow-Methods: *');

	$SERVICE = 'Log';

	clean_request();
	
	$route = get_route();
	$path = get_path();

	$path_routes = './routes/';

	switch($route) {
		case '/logs':
			require($path_routes . 'logs.php');
			break;
		default:
			error(404);
			break;
	}
?>

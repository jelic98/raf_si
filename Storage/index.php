<?php		
	require('./secret/db.php');
	require('../include/autoload.php');

	header('Access-Control-Allow-Origin: *');
	header('Access-Control-Allow-Headers: *');
	header('Access-Control-Allow-Methods: *');

	$SERVICE = 'Storage';

	clean_request();
	
	$route = get_route();
	$path = get_path();

	$path_routes = './routes/';

	switch($route) {
		case '/files':
			require($path_routes . 'files.php');
			break;
		default:
			error(404);
			break;
	}
?>

<?php
	require('./secret/db.php');
	require('../include/autoload.php');
	
	clean_request();

	$route = get_route();
	$path = get_path();

	$path_routes = './routes/';

	switch($route) {
		case '/services':
			require($path_routes . 'services.php');
			break;
		default:
			require($path_routes . 'api.php');
			break;
	}
?>

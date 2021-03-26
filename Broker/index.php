<?php	
	ini_set('display_errors', 'on');
	ini_set('session.gc_maxlifetime', 86400);
	session_set_cookie_params(86400);
	session_start();
	
	$path_include = './include/';
	$path_secret = './secret/';
	$path_routes = './routes/';

	require($path_secret . 'db.php');
	require($path_include . 'db.php');
	require($path_include . 'lib.php');
	
	clean_request();

	$route = get_route();
	$path = get_path();

	switch($route) {
		case '/services':
			require($path_routes . 'services.php');
			break;
		default:
			require($path_routes . 'api.php');
			break;
	}
?>

<?php
	ini_set('display_errors', 'on');
	ini_set('session.gc_maxlifetime', 86400);

	session_set_cookie_params(86400);
	session_start();
	
	$DIR = dirname(__FILE__);
		
	require($DIR . '/db.php');
	require($DIR . '/lib.php');
?>

<?php
	switch($_SERVER['REQUEST_METHOD']) {
		case 'POST':
			check_request(['hash', 'files'], 'files', 'post');

			$hash = $_REQUEST['hash'];
			$files = html_entity_decode($_REQUEST['files']);

			$files_arr = json_decode($files, true);

			$zip = new ZipArchive;

			if ($zip->open('gen/' . $hash . '.zip', ZipArchive::CREATE) === TRUE) {
				foreach($files_arr as $f) {
					$zip->addFromString($f['name'], $f['content']);
				}

				$zip->close();
			}else {
				error(500);
			}

			break;
		case 'GET':
			check_request(['hash'], 'files', 'get');

			$hash = $_REQUEST['hash'];

			$file = $hash . '.zip';

			header('Content-Type: application/zip');
			header('Content-Length: ' . filesize('gen/' . $file));
			header('Content-Disposition: attachment; filename="' . $file . '"');
			
			readfile('gen/' . $file);
			unlink('gen/' . $file);

			break;
		default:
			error(405);
			break;
	}
?>

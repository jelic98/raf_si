package rs.raf;

import rs.raf.service.Service;
import rs.raf.util.Log;

public class Main {

    public static void main(String[] args) {
        if(args.length == 0) {
            Log.error("No service port provided");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            new Service(port).start();
        }catch(NumberFormatException e) {
            Log.error("Invalid service port provided");
        }
    }
}

package rs.raf.generator;

import rs.raf.generator.util.Log;

public class Main {

    public static void main(String[] args) {
        try {
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
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

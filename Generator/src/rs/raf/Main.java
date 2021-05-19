package rs.raf;

import rs.raf.service.Service;

public class Main {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("No service port provided");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            new Service(port).start();
        }catch(NumberFormatException e) {
            System.err.println("Invalid service port provided");
        }
    }
}

package bgu.spl.net.impl.BidiMessaging;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.Server;

/**
 * Created by amirshkedy on 31/12/2018.
 */
public class BidiNetworkServer {
    public static void main(String[] args) {
//        Network mynetwork = new Network();
        Database mynetwork = new Database();

        Server.threadPerClient(
                7777,
                () -> new BidiMessageProtocol(mynetwork),
                BidiEncdec::new
        ).serve();

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777,
//                () -> new BidiMessageProtocol(mynetwork),
//                BidiEncdec::new
//        ).serve();
    }
}

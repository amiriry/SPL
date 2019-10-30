import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.impl.BidiMessaging.BidiEncdec;
import bgu.spl.net.impl.BidiMessaging.BidiMessageProtocol;
import bgu.spl.net.impl.BidiMessaging.Message;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * Created by amirshkedy on 28/12/2018.
 */
public class tests {
    public static void main(String[] args) {
        // test notification message encoding
        /*
        Message.Notification check = new Message.Notification('0',"amir", "this is my thing");
        BidiMessagingProtocol protcol = new BidiMessageProtocol();
        BidiEncdec encdec = new BidiEncdec();
        System.out.println("print something: " + check.getOpcode());
        byte[] returnedArray = encdec.encode(check);
        System.out.println("is this ok?");
        */

        //test bytes to short function
        /*
        byte[] myarray = new byte[2];
        myarray[0] = (byte)9;
        myarray[1] = (byte)3;
        ByteBuffer buffer = ByteBuffer.wrap(myarray);
        System.out.println("this is the short version: " + buffer.getShort());


        ShortBuffer sbuffer = ShortBuffer.wrap(new short[]{3});
        Short myshort = 8;
        System.out.println("this is another thing: " + myshort.byteValue());
        */

        //zero byte value
        /*
        byte itai;
        itai = (byte)'0';
        */

        //split strings
        /*
        String myString = "itaielbaz0kakimetumtam";
        String[] data = myString.split("0");
        System.out.println("what is going on here?");
        */

//        byte[] bytesArray = {'8', }
//        short result = (short) ((bytesArray[0] & 0xFF) << 8);
//        //check
//        System.out.println("first byte " + (short)(bytesArray[0] -'0'));
//        result += (short) (bytesArray[1] & 0xFF);
//        //check
//        System.out.println("second byte " + bytesArray[1]);
//        System.out.println("print this short: " + result);
//        return result;
        // take @
        String[] look = "amir amir @yael udi yarin @michael yoyo".split("@");
        boolean pastFirst = false;
        for(String one: look){
            if(pastFirst)
                System.out.println(one.substring(0,one.indexOf(' ')));
            pastFirst = true;
        }
        System.out.println(Arrays.toString(look));
    }
}

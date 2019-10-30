package bgu.spl.net.impl.BidiMessaging;

import bgu.spl.net.api.MessageEncoderDecoder;
//import bgu.spl.net.impl.BidiMessaging.Messages.Message;
import bgu.spl.net.impl.BidiMessaging.Messages.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the encoder decoder of Bidi Messaging protocol
 */
public class BidiEncdec implements MessageEncoderDecoder<Message> {
    byte[] bArrToRet = null;
    ArrayList<Byte> readerArray = new ArrayList<>();
    int bytesCounter = 0;
    short decodedOpcode = -1; // default value
    byte[] opcodeArray = new byte[2];
    // counts the number of zeros in message
    int zeroCounter = 0;

    // The message to return from decodeNextByte
    Message msgToReturn = null;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //just for check
        // If we are reading the opcode
        if(bytesCounter < 2){
            opcodeArray[bytesCounter] = nextByte;
            if(bytesCounter == 1){
                decodedOpcode = bytesToShort(opcodeArray);
                // message that contain only opcode
                if(decodedOpcode == 3){     //LOGOUT
                    bytesCounter = 0;
                    return new LOGOUTmessage();
                }
                if(decodedOpcode == 7){     //USERLIST
                    System.out.println("I got into userlist");
                    bytesCounter = 0;
                    return new USERLISTmessage();
                }
            }
        }
        //check
        // If we past reading the opcode and its not one of the messages cotain only opcode
        else{
            msgToReturn = null;
            switch(decodedOpcode){

                // in the right comment - the structure without opcode which is already read
                case 1:     // username0password0
                    if(nextByte ==(byte) '\0') {
                        //for every zero we encounter, first of all we check if it is the last zero of the message
                        //if it is, it means that we read all the bytes of LOGIN message.
                        //so, we want to create a string array, from the reader array. we know how to do that
                        //because we have zeros that separates the data
                        //if it is not the last zero. we want to increment the zero counter
                        //and after, read again all the bytes until we encounter zero again.
                        if(zeroCounter == 1) {

                            String[] alldatanew = new String(toBytesArray(readerArray),
                                                             StandardCharsets.UTF_8).split("\0");
                            readerArray.clear();
                            zeroCounter = 0;
                            bytesCounter = 0;
                            decodedOpcode = 0; // check - if this would fix the bug
                            return new REGISTERmessage(alldatanew[0], alldatanew[1]);
                        }
                        else {
                            zeroCounter++;
                        }
                    }
                    //when reading the data, every iteration will involve a write to the readerArray.
                    //the only iteration the will not involve it, its the iteration that we read \n and
                    //we saw 1 already one \n before. so we will construct the array and return it
                    readerArray.add(nextByte);
                    break;
                case 2: // LOGIN - username0password0
                    if(nextByte ==(byte) '\0') {
                        //for every zero we encounter, first of all we check if it is the last zero of the message
                        //if it is, it means that we read all the bytes of LOGIN message.
                        //so, we want to create a string array, from the reader array. we know how to do that
                        //because we have zeros that separates the data
                        //if it is not the last zero. we want to increment the zero counter
                        //and after, read again all the bytes until we encounter zero again.
                        if(zeroCounter == 1) {

                            String[] alldatanew = new String(toBytesArray(readerArray),
                                    StandardCharsets.UTF_8).split("\0");
                            readerArray.clear();
                            zeroCounter = 0;
                            bytesCounter = 0;
                            decodedOpcode = 0; // check - if this would fix the bug
                            return new LOGINmessage(alldatanew[0], alldatanew[1]);
                        }
                        else {
                            zeroCounter++;
                        }
                    }
                    //when reading the data, every iteration will involve a write to the readerArray.
                    //the only iteration the will not involve it, its the iteration that we read \n and
                    //we saw 1 already one \n before. so we will construct the array and return it
                    readerArray.add(nextByte);
                    break;
                case 4: // Follow - FollowopcodeNumofusersUsernamelist0
                    //check
                    System.out.println("got into case 4");
                    // If encountered 0 - we know the structure of the message So we will take the reader array
                    //  and take sublists in the right places that corresponds to Follow message arguments
                    if(nextByte == (byte)'\0' && bytesCounter > 4) {
                        bytesCounter = 0;
                        byte follow = readerArray.get(0);

                        // reading not including the last one - that's why 3 and size() are there
                        short numOfUsers = bytesToShort(toBytesArray(readerArray.subList(1,3)));
                        String userNameList = new String(toBytesArray(readerArray.subList(3,readerArray.size())),
                                                         StandardCharsets.UTF_8);
                        readerArray.clear();
                        bytesCounter = 0;
                        return new FOLLOWmessage(follow, numOfUsers, userNameList);

                    }
                    else
                        readerArray.add(nextByte);
                    break;
                case 5: // POST - PostopcodeConent0
                    //check
                    System.out.println("got into case 5");
                    if(nextByte == (byte)'\0'){
                        bytesCounter = 0;
                        // The reader array should contain only the Contents argument bytes
                        String content = new String(toBytesArray(readerArray), StandardCharsets.UTF_8);
                        return new POSTmessage(content);
                    }
                    else
                        readerArray.add(nextByte);
                    break;
                case 6: // PM - Privte Message - OpcodeUsername0Contents0
                    //check
                    System.out.println("got into case 6");
                    if(nextByte == (byte)'\0') {
                        if(zeroCounter == 1) {
                            String[] alldatanew = new String(toBytesArray(readerArray),
                                                             StandardCharsets.UTF_8).split("\0");
                            String username = alldatanew[0];
                            String content = alldatanew[1];
                            bytesCounter = 0;
                            readerArray.clear();
                            zeroCounter = 0;
                            return new PMmessage(username, content);

                        }
                        else
                            zeroCounter++;
                    }
                    readerArray.add(nextByte);
                    break;
                case 8: // STAT - OpcodeUsername0
                    //check
                    System.out.println("got into case 8");
                    if(nextByte == (byte)'\0'){
                        //check
                        System.out.println("get inside stat....");
                        String username = new String(toBytesArray(readerArray), StandardCharsets.UTF_8);
                        bytesCounter = 0;
                        readerArray.clear();
                        return new STATmessage(username);

                    }
                    else
                        readerArray.add(nextByte);
                    //check
                    System.out.println("before breaking");
//                    Arrays.toString(readerArray.toArray());
                    break;
                case 9: // NOTIFICATION - OpcodeNotificationtypePostinguser0Conent0
                    //check
                    System.out.println("got into case 9");
                    if(nextByte == (byte)'0'){
                        if(zeroCounter == 2){
                            String[] notificationData = new String(toBytesArray(readerArray)).split("0");
                            // first character of the string is the notification type
                            char notificationType = notificationData[0].toCharArray()[0];
                            // the user name is the substring starting from second place in the array
                            // to the end of first element in the notification array
                            String postingUser = notificationData[0].substring(1,notificationData[0].length()-1);
                            String contnet = notificationData[1];
                            bytesCounter = 0;
                            readerArray.clear();
                            zeroCounter = 0;
                            return new NOTIFICATIONmessage(notificationType, postingUser, contnet);

                        }
                        else
                            zeroCounter++;
                    }
                    if(zeroCounter < 2)
                        readerArray.add(nextByte);
                    break;
                case 10: // Acknowledge
                    //check
                    System.out.println("got into case 10");
                    readerArray.clear();
                    bytesCounter = 0;
                    break;
                case 11:
                    //check
                    System.out.println("got into case 11");
                    // Error only reads 2 bytes of the opcode it is for
                    //check
                    if(readerArray.size() == 2){
                        short errorOpCode = bytesToShort(toBytesArray(readerArray));
                        bytesCounter = 0;
                        readerArray.clear();
                        return new ERRORmessage(errorOpCode);

                    }
                    else
                        readerArray.add(nextByte);
                    break;
                //check
                default:
                    System.out.println("Command is not properly formatted");

            }
        }

        bytesCounter++;
        return msgToReturn;
    }

    @Override
    public byte[] encode(Message message) {

        // should check if the initialization is fine
        // a bytes array to return - will contain the message info in a bytes array
//        byte[] bArrToRet = null;
        byte[] opcodeBytes = shortToBytes(message.getOpcode());
        int opcodeBytesLength = opcodeBytes.length;
        int currentPlaceToPush;


        // all messages firt 2 bytes are opcode
        // The structure is after the first 2 bytes
        switch (message.getOpcode()){
            // NOTIFICATION - structure
            // 1 char  , String      , 1 byte,  String , 1 byte
            //NoteType   PostingUser     0      Content   0
            // Note Type - 0 - Pm Message, 1 - post
            case 9:
                NOTIFICATIONmessage notMessage = (NOTIFICATIONmessage) message;

                // turn the character of type to string - for easier usage
                // to use its length in bytes array definition
                // and to send it as bytes array to the function that pushes it to array
                String notificationTypeString = ((Character) notMessage.getNotificationType()).toString();
                bArrToRet = new byte[opcodeBytesLength +
                                     notificationTypeString.getBytes().length +
                                     notMessage.getPostingUser().getBytes().length + 1 +
                                     notMessage.getContent().getBytes().length + 1];

                // pushing opcode to byte array
                currentPlaceToPush = 0;
                pushBytesByPlace(opcodeBytes, currentPlaceToPush);
                currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                // push NoteType to byte array
                pushBytesByPlace(notificationTypeString.getBytes(), currentPlaceToPush);
                currentPlaceToPush = currentPlaceToPush + notificationTypeString.getBytes().length;

                // push posting user to array
                pushBytesByPlace(notMessage.getPostingUser().getBytes(), currentPlaceToPush);
                currentPlaceToPush = currentPlaceToPush + notMessage.getPostingUser().getBytes().length;
                pushZeroByte(currentPlaceToPush);
                currentPlaceToPush++;


                // push the conent of the post to array
                pushBytesByPlace(notMessage.getContent().getBytes(), currentPlaceToPush);
                currentPlaceToPush = currentPlaceToPush + notMessage.getContent().getBytes().length;
                pushZeroByte(currentPlaceToPush);

                break;

            // don't know what to do with it yet
            // Acknowledgement recievers:
            //8 STAT - number of posts the user posted (not including PM), number of follower, number
            //        of users the user is following - in the optional
            //7 USERLIST - number of users and list of usernames separated by zero bytes ending with
            //            a zero byte - in optional
            //4 FOLLOW - have the following form:
            // 2 bytes   , 2 bytes          , 2 bytes       , String        , 1 bytes
            // Ack Opcode   Follow opcode    numberOfUsers    UserNameLst       0

            //3 LOGOUT - nothing in potional
            //1 REGISTER - nothing in optional

            // ACK - structure
            // 2 byte        , don't know what that means yet
            // MessageOpCode   Optional
            case 10:
                ACKmessage ackMessage = (ACKmessage) message;
                // pushing opcode to byte array
                currentPlaceToPush = 0;
                short opcodeToAck = ackMessage.getMsgOpcode();
                switch (opcodeToAck){
                    case 8: // STAT
                        bArrToRet = new byte[10];
                        pushBytesByPlace(shortToBytes(message.getOpcode()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        pushBytesByPlace(shortToBytes(opcodeToAck), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        pushBytesByPlace(shortToBytes(ackMessage.getNumOfUsers()),currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + 2;

                        pushBytesByPlace(shortToBytes(ackMessage.getNumFollowing()), currentPlaceToPush);
                        break;
                    case 7: // USERLIST
                        bArrToRet = new byte[7+ackMessage.getUserNameList().length()];
                        pushBytesByPlace(shortToBytes(message.getOpcode()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        // push the userlist opcode (short - 2 bytes) to the bytes array
                        pushBytesByPlace(shortToBytes(opcodeToAck), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        // push to number of users (short 2 bytes) to the array bytes
                        //check
                        System.out.println("this is the number of users: " + ackMessage.getNumOfUsers() + "" +
                                "and this is their bytes array: " + shortToBytes(ackMessage.getNumOfUsers()));
                        pushBytesByPlace(shortToBytes(ackMessage.getNumOfUsers()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + 2;

                        pushBytesByPlace(ackMessage.getUserNameList().getBytes(), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + ackMessage.getUserNameList().length();

                        pushZeroByte(currentPlaceToPush);

                        break;
                    case 4: // FOLLOW
                        bArrToRet = new byte[7+ackMessage.getUserNameList().length()];

                        pushBytesByPlace(shortToBytes(message.getOpcode()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        pushBytesByPlace(shortToBytes(opcodeToAck), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        // check - not sure that it works
                        pushBytesByPlace(shortToBytes(ackMessage.getNumOfUsers()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + 2;

                        pushBytesByPlace(ackMessage.getUserNameList().getBytes(), currentPlaceToPush);
                        break;
                    case 3: // LOGOUT
                        bArrToRet = new byte[4];
                        pushBytesByPlace(shortToBytes(message.getOpcode()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                        pushBytesByPlace(shortToBytes(opcodeToAck), currentPlaceToPush);
                        break;
                    case 1: // REGISTER
                        bArrToRet = new byte[4];
                        pushBytesByPlace(shortToBytes(message.getOpcode()), currentPlaceToPush);
                        currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;
                        pushBytesByPlace(shortToBytes(opcodeToAck), currentPlaceToPush);
                        break;
                }
                break;

            // ERROR - structure
            // 2 bytes
            // MessageOpCode - the message opcode error was sent for
            case 11:
                ERRORmessage errMessage = (ERRORmessage) message;
                // importamt bug - there are 4 bytes here - 2 for opcode of Error and 2 for opcode of the message
                // that the error is about
                bArrToRet = new byte[4];

                // pushing opcode to byte array
                currentPlaceToPush = 0;
                pushBytesByPlace(opcodeBytes, currentPlaceToPush);
                currentPlaceToPush = currentPlaceToPush + opcodeBytesLength;

                // pushing opcode of message that the error is about
                pushBytesByPlace(shortToBytes(errMessage.getErrorOpcode()), currentPlaceToPush);
                break;
        }


        return bArrToRet;
    }

    private byte[] pushZeroByte(int placeToPush){
        bArrToRet[placeToPush] = (byte) '\0';
        return bArrToRet;
    }

    private void pushBytesByPlace(byte[] toPush, int startPush){
        for(int i = 0; i < toPush.length; i++){
            bArrToRet[startPush+i] = toPush[i];
        }
    }

    public static short bytesToShort(byte[] bytesArray){
        short result = (short) ((bytesArray[0] & 0xFF) << 8);
        result += (short) (bytesArray[1] & 0xFF);
        return result;
    }

    //convert Arraylist of bytes to bytes array
    private byte[] toBytesArray(List<Byte> toConvert){
        byte[] toReturn = new byte[toConvert.size()];
        for(int i =0; i < toConvert.size(); i++){
            toReturn[i] = toConvert.get(i);
        }

        return toReturn;
    }

    // The above does the same - should think who is better
    private byte[] shortToBytes(short num)
    {
        byte[] shortInBytes = new byte[2];
        shortInBytes[0] = (byte)((num >> 8) & 0xFF);
        shortInBytes[1] = (byte)(num & 0xFF);
        return shortInBytes;
    }

}

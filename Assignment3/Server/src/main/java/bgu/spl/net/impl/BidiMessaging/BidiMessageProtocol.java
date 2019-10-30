package bgu.spl.net.impl.BidiMessaging;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.Connections;
//import bgu.spl.net.impl.BidiMessaging.Messages.Message;
import bgu.spl.net.impl.BidiMessaging.Messages.*;

import java.util.*;

/**
 * This class will process all messages through the Bidi messaging protocol
 */
public class BidiMessageProtocol implements BidiMessagingProtocol<Message> {
    private int connectionId;
    private Connections<Message> connections;
    private Database networkDatabase;

    public BidiMessageProtocol(Database database) {
        this.networkDatabase = database;
    }


    // not fully understand why initialize the parameters here and not in the constructor
    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    // this class is actually doing what need to be done by the server side
    @Override
    public void process(Message message) {
        short opCode  = message.getOpcode();
        switch (opCode){
            // REGISTER - implement after
            case 1:
                REGISTERmessage regMsg = (REGISTERmessage) message;
                // if the user is not registered - put it in the map between name and password - the
                // registered map, If he is registered - returned an error - for opcode 1 which is the
                // opcode of REGISTER
                if(!networkDatabase.isRegistered(regMsg.getName())){
                    networkDatabase.registerUser(regMsg.getName(), regMsg.getPassword());
                    connections.send(this.connectionId, new ACKmessage((short)1));
                }
                else{
                    connections.send(this.connectionId, new ERRORmessage((short) 1));
                }


                break;
            //********************************************************************************
            // LOGING
            case 2:
                LOGINmessage loginMsg = (LOGINmessage) message;
                // checks
                // 1.Is he in registered users - if he's not can't connect
                // 2.Is he already connected - checked by id - if he is can't connect
                // 3.Is the password correspond to the registered password
                // If checks are ok - put connection Id in the loggedInUsers map
                //check
                if(networkDatabase.isRegistered(loginMsg.getName()) &&
                   !networkDatabase.isLoggedIn(this.connectionId) &&
                    networkDatabase.isPasswordCorrect(loginMsg.getName(), loginMsg.getPassword())){
                    networkDatabase.logInUser(this.connectionId, loginMsg.getName());
                }
                else{
                    connections.send(this.connectionId, new ERRORmessage((short) 2));
                }
                break;
            //********************************************************************************
            // LOGOUT
            case 3:
                LOGOUTmessage logoutMsg = (LOGOUTmessage) message;
                // Is the user logged in?
                if(networkDatabase.isLoggedIn(this.connectionId)) {
                    connections.send(this.connectionId, new ACKmessage((short) 3));
                    // remove from map by connectionId
                    networkDatabase.logoutUser(this.connectionId);
                    // Acknowledge the user that the disconnec was done
                    connections.disconnect(this.connectionId);


                }
                else{
                    connections.send(this.connectionId, new ERRORmessage((short) 3));
                }
                break;
            //********************************************************************************
            // I think that what I did here is wrong - its not suppose to be here - it suppose to
            // be in the server implementation - the data -
            // FOLLOW
            case 4:
                // If all users failed - Error
                FOLLOWmessage followMsg = (FOLLOWmessage) message;

                // String to put in Ack message
                String usersSucceedsString = new String();
                // number of succeeded actionss
                short numSucceeds = 0;


                if(networkDatabase.isLoggedIn(this.connectionId)) {
                    // List to follow/unfollow
                    ArrayList<String> userTodoList = convertStringToUserList(followMsg.getUserNameList());
                    // if the byte of follow is for following
                    if (followMsg.getFollowByte()== 0) {
                        // go over the users in list
                        for(String userToAdd: userTodoList){
                            // If the user is not already following them
                            if(!networkDatabase.doesFollow(this.connectionId, userToAdd)) {
                                // add the user to the list of users the user is following
                                networkDatabase.addFollowing(this.connectionId, userToAdd);

                                // add zero byte in the end
                                usersSucceedsString = usersSucceedsString.concat(userToAdd + "\0");
                                // increase the counter of operations succeeded by 1
                                numSucceeds++;
                            }
                        }
                    }
                    // If the byte of follow is for unfollow
                    else{
                        // go over userss in list
                        for(String userToRemove: userTodoList){
                            // If the user is following them
                            if(networkDatabase.doesFollow(this.connectionId, userToRemove)) {
                                // remove it from the list of followings
                                networkDatabase.unfollow(this.connectionId, userToRemove);
                                // add zero bytes in the end
                                usersSucceedsString.concat(userToRemove + "\0");
                                // increase the counter of operations succeeded by 1
                                numSucceeds++;
                            }
                        }
                    }
                    // add zero byte to the end of the string
//                    usersSucceedsString.concat("0");

                    // If number of operations succeeded is 0 than no operation succeeded - results in an Error
                    if(numSucceeds == 0){
                        connections.send(this.connectionId, new ERRORmessage((short) 4));
                    }
                    // If at leat one operation succeeded
                    else{
                        // There should be optional field in this acknowledge which
                        // I am not sure how to implement yet
                        connections.send(this.connectionId, new ACKmessage(followMsg.getFollowByte(), numSucceeds, usersSucceedsString));
                    }
                }
                // user is not logged in
                else{
                    connections.send(this.connectionId, new ERRORmessage((short) 4));
                }

                break;
            //********************************************************************************
            // POST
            case 5:
                POSTmessage postMsg = (POSTmessage) message;
                String post = postMsg.getContents();
                UserList userFollows = networkDatabase.getUserByConnId(this.connectionId).getFollowers();
                String[] splitByShtrudle = postMsg.getContents().split("@");

                String postingUser = networkDatabase.getUserByConnId(this.connectionId).getName();
                String postConent = postMsg.getContents();
                String username;
                // find all users mentioned with '@'
                boolean pastFirst = false;
                for(String oneline: splitByShtrudle){
                    if(pastFirst) {
                        if (oneline.indexOf(' ') == -1) {
                            username = oneline;
                        } else {
                            username = oneline.substring(0, oneline.indexOf(' '));
                        }
                        connections.send(networkDatabase.getConnIdByName(username).getConnId(),
                                new NOTIFICATIONmessage('1', postingUser, postConent));
                    }
                    pastFirst = true;
                }
                networkDatabase.addMessage(networkDatabase.getUserByConnId(this.connectionId).getName(), postMsg);
                break;
            //********************************************************************************
            // PM
            // problem: if there is more than one user with the name? In the protocol
            // specification there
            case 6:
                PMmessage pmMsg = (PMmessage) message;
                String userToSend = pmMsg.getUserName();
                String sendingUser = networkDatabase.getUserByConnId(this.connectionId).getName();
                String pmContent = pmMsg.getContent();
                // need to send notification
//                connections.send(Message.Notification)
                int toSendConnId = networkDatabase.getConnIdByName(userToSend).getConnId();
                connections.send(toSendConnId, new NOTIFICATIONmessage('0', sendingUser, pmContent));
                networkDatabase.addMessage(userToSend, pmMsg);
                break;
            //********************************************************************************
            // USERLIST
            case 7:
                //check
                System.out.println("got into user list in protocol");
                USERLISTmessage userLstMsg = (USERLISTmessage) message;
                String userlistLine = "";
                if(networkDatabase.isLoggedIn(this.connectionId)){
                    //check
                    System.out.println("user is logged in and trying to get user list");
                    if(networkDatabase.getFollowings(this.connectionId) != null) {
                        for (User current : networkDatabase.getFollowings(this.connectionId).getUserlist()) {
                            //check
                            System.out.println("going over the ones he follows");
                            userlistLine.concat(" " + current.getName());
                        }
                    }
                    //check
                    System.out.println("sending acknowledge with the userlist it follows " + networkDatabase.getNumFollowings(this.connectionId));
                    connections.send(connectionId, new ACKmessage((byte)networkDatabase.getNumFollowings(this.connectionId),
                                                                   userlistLine ));
                }
                else
                    connections.send(this.connectionId, new ERRORmessage((short) 7));

                break;
            //********************************************************************************
            // STAT
            case 8:
                STATmessage statMsg = (STATmessage) message;
                if(networkDatabase.isLoggedIn(this.connectionId)){
                    // I am not sure that this conversion is fine - and I did it few times
                    short numOfFollowing = (short)networkDatabase.getFollowings(this.connectionId).size();
                    short numOfPosts = (short)networkDatabase.getPostsByConnId(this.connectionId).size();
                    connections.send(this.connectionId, new ACKmessage(numOfPosts,numOfFollowing));
                }
                else
                    connections.send(this.connectionId, new ERRORmessage((short) 7));
                break;
            //********************************************************************************
            // I think that notification is not dealt by the protocol it is only sent after other messages
            // NOTIFICATION
            case 9:
                break;
            //********************************************************************************
            // ****************special treat************************************************
            // I think that acknowledge is like ERROR message - probably will be deleted in the future
            // ACK
            case 10:
                //for checks purpuses
                //checks
                System.out.println("I am inside acknowledge....");
                connections.send(this.connectionId, new ACKmessage((short) 1));

                break;
            // I think that the server doesn't get error message - it only sends them
            // for now I leave this here - probably will be deleted in the future
            // ERROR
            case 11:
                break;
        }
    }

    private ArrayList<String> convertStringToUserList(String userlist){
        ArrayList<String> usersToReturn = null;
        // the separating byte is 0 - not sure that this is the way to do this
        String[] users = userlist.split("0");
        List<String> listOfUsers = Arrays.asList(users);
        usersToReturn = new ArrayList<>(listOfUsers);
        return usersToReturn;
    }

    // maybe by reference and changes the content element - should be checked
    private String[] namesAppearInPost(String content){
        String[] appearInPost = null;
        while(content.indexOf('@') != -1){
//            content.substring()
        }
//        content.substring(content.indexOf('@'))
        return appearInPost;
    }
    @Override
    public boolean shouldTerminate() {
        return false;
    }
}

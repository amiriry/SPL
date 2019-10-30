package bgu.spl.net.impl.BidiMessaging;

import bgu.spl.net.impl.BidiMessaging.Messages.NOTIFICATIONmessage;
import bgu.spl.net.impl.BidiMessaging.Messages.PMmessage;
import bgu.spl.net.impl.BidiMessaging.Messages.POSTmessage;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private int connId = -1;
    private String password;
    private String name;
    private UserList followings = new UserList();
    private UserList followers = new UserList();
    private ConcurrentLinkedQueue<Message> allMessages = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<NOTIFICATIONmessage> notifications = new ConcurrentLinkedQueue<>();

    User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Message> getPosts() {
        LinkedList<Message> posts = new LinkedList<>();
        for(Message current: allMessages){
            if(current instanceof POSTmessage){
                posts.add(current);
            }
        }
        return posts;
    }

    public ConcurrentLinkedQueue<NOTIFICATIONmessage> getNotifications() {
        return notifications;
    }

    public int getConnId() {
        return connId;
    }

    boolean isLoggedIn() {
        if(connId == -1)
            return false;
        else
            return true;
    }

    // check is the current users password is correct
    boolean isPasswordCorrect(String password){
        boolean isCorrect = false;
        if(this.password.equals(password))
            isCorrect = true;
        return isCorrect;
    }

    // if connected than connectionId shold be something
    void logIn(int connId){
        this.connId = connId;
    }

    public UserList getFollowings() {
        return followings;
    }

    public UserList getFollowers() {
        return followers;
    }

    public void logout(){
        this.connId = -1;
    }

    public short followingSize(){
        return followings.size();
    }

    public short followersSize(){
        return followers.size();
    }

    public void addPosts(Message post){
        allMessages.add(post);
    }

    public void addFollowing(User toFollow){
        this.followings.addUserToFollowingList(toFollow);
    }

    public void unfollow(User toUnFollow){
        this.followings.removeUserFromFollowing(toUnFollow);
    }
}

package it.gdp.p2p.semanticSocialNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork {

    final private Peer peer;
    final private PeerDHT dht;

    User usr = new User();
    String profileKey;

    public SemanticHarmonySocialNetworkImpl(int idPeer, String masterPeer, final MessageListener listener)
            throws Exception {

        peer = new PeerBuilder(Number160.createHash(idPeer)).ports(Utils.MASTER_PORT_BASE + idPeer).start();
        dht = new PeerBuilderDHT(peer).start();

        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(masterPeer))
                .ports(Utils.MASTER_PORT_BASE).start();
        fb.awaitUninterruptibly();

        if (fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        } else {
            throw new Exception("Error in master peer bootstrap.");
        }
        creatPeerAddressList();
        peer.objectDataReply(new ObjectDataReply() {

            public Object reply(PeerAddress sender, Object request) throws Exception {
                return listener.parseMessage(request);
            }
        });
    }

    /**
     * Create a list of peer addresses
     * @return true if the list is created, false otherwise
     */
    public boolean creatPeerAddressList() {// changed from void to boolean
        try {
            FutureGet futureGet = dht.get(Number160.createHash("peerAddress")).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && futureGet.isEmpty()) {
                dht.put(Number160.createHash("peerAddress")).data(new Data(new HashMap<PeerAddress, String>())).start()
                        .awaitUninterruptibly();

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Must-to-have.
     * Get list of questions for user
     * @return list of Strings containing the questions
     */
    public List<String> getUserProfileQuestions() {
        Question questions = new Question();
        return questions.returnQuestion();
    }

    /**
     * Return user object in the p2p network.
     * @param nickName
     * @return User if found, null otherwise
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public User get(String nickName) throws ClassNotFoundException, IOException {
        FutureGet fg = dht.get(Number160.createHash(nickName)).start().awaitUninterruptibly();
        if(fg.isSuccess()) {
            return (User) fg.dataMap().values().iterator().next().object();
        }
        return null;
    }

    /**
     * It finds, thanks to peerAddress in the DHT and the associated hasmap, 
     * retrieves every object of type User within the network. 
     * @return peerList
     */
    public List<User> getObjPeers() {
        List<User> peersList = new ArrayList<User>();
        User user;
        try {
            FutureGet fg = dht.get(Number160.createHash("peerAddress")).start().awaitUninterruptibly();
            if(fg.isSuccess()) {
                if(fg.isEmpty()) {
                    return null;
                }
                HashMap<PeerAddress, String> connected_peers;
                connected_peers = (HashMap<PeerAddress, String>) fg.dataMap().values().iterator().next().object();
                for(String p : connected_peers.values()) {
                    user = get(p);
                    peersList.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return peersList;
    }


    /**
     * Check if nickName is already in the network.
     * @return true if nickName is already in the network, false otherwise
     */
    public boolean checkUnicNickname(String nickName) {
        List<User> userList = getObjPeers();
        for(User user : userList) {
            if(user.getnickName().equals(nickName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Must-to-have.
     * Create user profile key
     * @return profileKey string
     */
    public String createAuserProfileKey(List<Integer> answer){
        profileKey = Utils.genProfileKey(answer);
        return profileKey;
    }

    /**
     * Must-to-have.
     * Allows the peer created earlier to join the network. 
     * The parameters required are the profile key to identify the user and the nickname used to identify a user on the network. 
     * The User object is first inserted into the network and then all peers are notified that a new peer has joined.
     */
    public boolean join(String profileKeyString, String nickName) {
        try {
            FutureGet fg = dht.get(Number160.createHash(profileKeyString)).start().awaitUninterruptibly();
            if(fg.isSuccess() && fg.isEmpty())
            {
                usr.setProfileKey(profileKeyString);
                usr.setnickName(nickName);
                dht.put(Number160.createHash(profileKeyString)).data(new Data(usr)).start().awaitUninterruptibly();
            }
            FutureGet fg2 = dht.get(Number160.createHash("peerAddress")).start().awaitUninterruptibly();
            if (fg2.isSuccess()) {
                if(fg2.isEmpty()) {
                    return false;
                }
                HashMap<PeerAddress, String> connected_peers;
                connected_peers = (HashMap<PeerAddress, String>) fg2.dataMap().values().iterator().next().object();
                connected_peers.put(dht.peerAddress(), usr.getProfileKey());
                dht.put(Number160.createHash("peerAddress")).data(new Data(connected_peers)).start()
                .awaitUninterruptibly();
                notification(connected_peers, nickName + " Join in the network!!");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Send message to peers on the network. The message is sent to all peers except the sender.
     * @param hashmap addresss of peer
     * @param obj object to be sent
     * @return true if message is sent, false otherwise
     */
    public boolean notification(HashMap<PeerAddress, String> hashmap, Object obj) {
        try {
            for(PeerAddress peer : hashmap.keySet()) {
                if(dht.peerAddress() != peer) {
                    FutureDirect fd = dht.peer().sendDirect(peer).object(obj).start();
                    fd.awaitUninterruptibly();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrolls through the list of User objects, 
     * and returns information about a specific nickname
     * @return
     */
    public User getUser(String nickName) {
        List<User> userList = getObjPeers();
        for(User user : userList) {
            if(user.getnickName().equals(nickName.toLowerCase())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Return list of all user in the p2p network
     * @return userList, list of <User>
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        List<User> peersList = getObjPeers();
        if(peersList != null) {
            userList.addAll(peersList);
        }
        return userList;
    }

    /**
     * Allows exit from the p2p network. Annuncing to other peer that you are leaving.
     */
    public boolean exitFromNetwork() {
        try {
            FutureGet fg = dht.get(Number160.createHash("peerAddress")).start();
            fg.awaitUninterruptibly();
            if(fg.isSuccess()){
                if(fg.isEmpty()) {
                    return false;
                }
                HashMap<PeerAddress, String> connected_peers;
                connected_peers = (HashMap<PeerAddress, String>) fg.dataMap().values().iterator().next().object();
                connected_peers.remove(dht.peerAddress());
                dht.put(Number160.createHash("peerAddress")).data(new Data(connected_peers)).start().awaitUninterruptibly();
                dht.peer().shutdown().awaitUninterruptibly();

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Must-to-have. 
     * Provides a list of the user's friends on the network. 
     * Evaluates the hamming distance between two strings.
     * @return List<User>, list of friends of user.
     */
    public List<String> getFriends() {
        List<User> userList = getObjPeers();
        List<String> friends = new ArrayList<String>();
        if(!userList.isEmpty()) {
            for(User user : userList) {
                if(!usr.getnickName().equals(user.getnickName())) {
                    if(Utils.checkFriendship(usr, user)) {
                        friends.add(user.getnickName());
                    }
                }
            }
        }
        usr.setFriends(friends);
        return friends;
    }
}

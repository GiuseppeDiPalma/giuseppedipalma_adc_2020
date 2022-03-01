package it.gdp.p2p.semanticSocialNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
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

    public List<String> getUserProfileQuestions() {
        Question questions = new Question();
        return questions.returnQuestion();
    }

    public User get(String nickName) throws ClassNotFoundException, IOException {
        FutureGet fg = dht.get(Number160.createHash(nickName)).start().awaitUninterruptibly();
        if(fg.isSuccess()) {
            return (User) fg.dataMap().values().iterator().next().object();
        }
        return null;
    }

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

    public boolean checkUnicNickName(String nickName) {
        List<User> userList = getObjPeers();
        for(User user : userList) {
            if(user.getnickName().equals(nickName)) {
                return false;
            }
        }
        return true;
    }

    public String createAuserProfileKey(List<Integer> answer) throws NoSuchAlgorithmException {
        profileKey = Utils.genProfileKey(answer);
        return profileKey;
    }

    public boolean validateProfileKey(String profileKey) {
        List<User> userList = getObjPeers();
        for(User user : userList) {
            if(user.getProfileKey().equals(profileKey)) {
                return false;
            }
        }
        return true;
    }

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
                notification(connected_peers, nickName + "has the same interests as you!!");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean notification(HashMap<PeerAddress, String> hashmap, Object obj) {
        try {
            for( PeerAddress peer : hashmap.keySet()) {
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

    public static void getFriends(List<String> friends) {

    }

    public User getUser(String nickName) {
        List<User> userList = getObjPeers();
        for(User user : userList) {
            if(user.getnickName().equals(nickName.toLowerCase())) {
                return user;
            }
        }
        return null;
    }

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

    public List<String> getFriends() {
        System.out.println("Dentro getFriends");
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

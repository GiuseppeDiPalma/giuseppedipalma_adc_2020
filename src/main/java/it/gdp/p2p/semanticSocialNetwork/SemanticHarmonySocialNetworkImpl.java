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

        peer.objectDataReply(new ObjectDataReply() {

            public Object reply(PeerAddress sender, Object request) throws Exception {
                return listener.parseMessage(request);
            }
        });
    }

    public List<String> getUserProfileQuestions() {
        Question questions = new Question();
        return questions.returnQuestion();
    }

    public User get(String nickName) throws ClassNotFoundException, IOException {
        FutureGet fg = dht.get(Number160.createHash(nickName)).start();
        fg.awaitListenersUninterruptibly();
        if(fg.isSuccess()) {
            return (User) fg.dataMap().values().iterator().next().object();
        }
        return null;
    }

    public List<User> getObjPeers() {
        List<User> peersList = new ArrayList<User>();
        User user;
        try {
            FutureGet fg = dht.get(Number160.createHash("peerAddress")).start();
            fg.awaitUninterruptibly();
            if(fg.isSuccess()) {
                if(fg.isEmpty())
                    return null;
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

    public String createAuserProfileKey(List<Integer> _answer) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean join(String profileKeyString, String nickName)
    {
        try {
            FutureGet fg = dht.get(Number160.createHash(profileKeyString)).start();
            fg.awaitUninterruptibly();
            if(fg.isEmpty() && fg.isSuccess()) 
            {
                usr.setProfileKey(profileKeyString);
                usr.setnickName(nickName);
                dht.put(Number160.createHash(profileKeyString)).data(new Data(usr)).start().awaitUninterruptibly();
            }

            FutureGet fg_2 = dht.get(Number160.createHash("peerAaddress")).start();
            fg_2.awaitUninterruptibly();
            if (fg_2.isSuccess()) {
                if(fg.isEmpty())
                    return false;

                HashMap<PeerAddress, String> connected_peers;
                connected_peers = (HashMap<PeerAddress, String>) fg.dataMap().values().iterator().next().object();
                connected_peers.put(dht.peerAddress(), usr.getProfileKey());
                dht.put(Number160.createHash("peerAddress")).data(new Data(connected_peers)).start().awaitUninterruptibly();
                // TODO: notification(connected_peers, nickName + "has the same interests as you!!");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getFriends() {
        // TODO Auto-generated method stub
        return null;
    }
}

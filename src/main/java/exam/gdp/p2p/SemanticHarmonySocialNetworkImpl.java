package exam.gdp.p2p;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork {

    final private Peer peer;
    final private PeerDHT dht;

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
        return null;
    }

    public String createAuserProfileKey(List<Integer> _answer) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean join(String _profile_key, String _nick_name) {
        // TODO Auto-generated method stub
        return false;
    }

    public List<String> getFriends() {
        // TODO Auto-generated method stub
        return null;
    }

}

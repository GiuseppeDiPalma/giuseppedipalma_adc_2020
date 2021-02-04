package exam.gdp.p2p;

import java.io.IOException;
import java.util.List;

import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork {

    final private Peer peer;
    final private PeerDHT dht;
    private String nickname = "";

    public SemanticHarmonySocialNetworkImpl(int idPeer, String masterPeer, final MessageListener listener)
            throws IOException {

        peer = new PeerBuilder(Number160.createHash(idPeer)).ports(Utils.MASTER_PORT_BASE + idPeer).start();
        dht = new PeerBuilderDHT(peer).start();
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

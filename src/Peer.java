import java.util.Collection;
import java.util.Objects;
import java.util.Vector;

/* INCAPSULA ID E VIEW DI UN PEER */
public class Peer {

    //Id e view del peer
    protected Integer mId;
    protected PeerView mView;
    protected int mC;

    //Costruttore prende come parametro l'id e la grandezza della view
    public Peer(int id, int c){
        mId = id;
        mC = c;
    }

    //Aggiunge un peer alla view (default timestamp 0)
    public boolean addNeighbor(int id){
        return mView.addViewEntry(id);
    }

    //Restituisce la topology (source, destination) of node
    StringBuilder getTopology(){
        StringBuilder res = new StringBuilder();
        Vector<Integer> neighbors = mView.getNeighbors();

        for (Integer idNeighbor : neighbors) {
            res.append(mId).append(",").append(idNeighbor);
            res.append(System.getProperty("line.separator"));
        }

        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return Objects.equals(mId, peer.mId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mId);
    }
}

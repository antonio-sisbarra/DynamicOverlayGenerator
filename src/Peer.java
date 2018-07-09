import java.util.Objects;
import java.util.Vector;

/* INCAPSULA ID E VIEW DI UN PEER */
public class Peer {

    //Id e view del peer
    private Integer mId;
    private PeerView mView;
    private int mC;

    //Costruttore prende come parametro l'id e la grandezza della view
    Peer(int id, int c) {
        mId = id;
        mC = c;

        //Inizializzo la view
        mView = new PeerView(mC, mId);
    }

    //Aggiunge un peer alla view (default timestamp 0)
    boolean addNeighbor(int id){
        return mView.addViewEntry(id);
    }

    //Effettua lo scambio della view con un altro peer
    void exchangeViews(Peer otherPeer, Integer currentTimestamp) {

        //Costruisco cache da inviare al destinatario più fresh link che si riferisce a me
        PeerView toSend = mView.getCacheCopy();
        toSend.addViewEntryCurrentTime(mId, currentTimestamp);

        //Mi serve la stessa cosa dall'other, anche io mi devo aggiornare
        PeerView toReceive = otherPeer.getmView().getCacheCopy();
        toReceive.addViewEntryCurrentTime(otherPeer.getmId(), currentTimestamp);

        //Scambio le view, ogni peer fa merge per mantenere link più freschi
        mView.mergeView(toReceive);
        otherPeer.getmView().mergeView(toSend);
    }

    /* GETTERS */

    public Integer getmId() {
        return mId;
    }

    public PeerView getmView() {
        return mView;
    }

    public int getmC() {
        return mC;
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

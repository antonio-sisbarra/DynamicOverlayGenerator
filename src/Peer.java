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

    //Add a neighbor (default timestamp 0)
    public void addNeighbor(int id){
        mView.addViewEntry(id);
    }
}

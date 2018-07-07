import java.util.Vector;

/* INCAPSULA UNA RETE DI PEER IN FORMATO GRID */
public class GridNetwork {

    //Numero di Peer nella rete
    protected int mNPeers;

    //Rete di Peer che formano la rete
    protected Vector<Peer> mNetwork;

    //Costruttore ha come parametri numero di peer e grandezza view
    public GridNetwork(int nPeers, int mC) throws IllegalArgumentException{
        if(nPeers < 1 || mC < 1)
            throw new IllegalArgumentException("Wrong params");

        mNetwork = new Vector<>(nPeers);
        mNPeers = nPeers;

        //Impongo un limite alla larghezza della rete (int. inf. della radice di Npeers)
        int maxWidth = (int) Math.floor(Math.sqrt(nPeers));

        Peer newPeer;
        //Creo i vari peer
        for(int i=0; i<nPeers; i++) {
            newPeer = new Peer(i, mC);

            /* Cerco di collegare ad ogni peer i 4 vicini (nord, sud, ovest, est) */

            //Se non sono al confine destro e non sono l'ultimo collego a destra
            if ((i + 1) % maxWidth != 0 && i != nPeers-1)
                newPeer.addNeighbor(i+1);

            //Se non sono al confine sinistro
            if(i % maxWidth != 0)
                newPeer.addNeighbor(i-1);

            //Se sopra c'è un peer
            if(i-maxWidth >= 0)
                newPeer.addNeighbor(i-maxWidth);

            //Se sotto c'è un peer
            if(i+maxWidth < nPeers)
                newPeer.addNeighbor(i+maxWidth);

            //Aggiungo peer alla rete
            mNetwork.insertElementAt(newPeer, i);
        }
    }

    /* GETTERS */

    public int getmNPeers() {
        return mNPeers;
    }

    public Vector<Peer> getmNetwork() {
        return mNetwork;
    }

    //Restituisce la topology della rete (source, destination)
    public StringBuilder getNetworkTopology(){
        StringBuilder res = new StringBuilder();

        for(Peer p: mNetwork){
            res.append(p.getTopology());
        }

        return res;
    }
}

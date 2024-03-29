import java.util.Random;
import java.util.Vector;

/* INCAPSULA UNA RETE SCALEFREE */
public class ScaleFreeNetwork {

    //Nodi iniziali da avere nella scale free network (seed nodes)
    private static final int NINITIALNODES = 5;

    //Archi da aggiungere per ogni nodo che vado ad aggiungere
    private static final int MEDGESTOADD = 2;

    //Numero di Peer nella rete
    private int mNPeers;

    //Rete di Peer che formano la rete
    private Vector<Peer> mNetwork;

    //Vettore di id, utile per aggiungere nodi per avere scale free network
    private Vector<Integer> mNetworkIDs;


    //COSTRUTTORE: prende come parametri nPeers, grandezza cache, e parametro p per creare Random Network
    ScaleFreeNetwork(int nPeers, int c, double pProb) throws IllegalArgumentException {
        if(nPeers < NINITIALNODES || c < 1 || pProb <= 0 || pProb > 1)
            throw new IllegalArgumentException("Wrong params");

        mNetwork = new Vector<>(nPeers);
        mNPeers = nPeers;


        generateScaleFree(pProb, c);
    }

    //Barabasi Albert alg. to generate a scale free network
    private void generateScaleFree(double pProb, int mC) {
        //Creo vettore di ID utile per ScaleFree
        mNetworkIDs = new Vector<>();

        //Genero una rete iniziale random
        generateRandomNetwork(pProb, mC);

        Peer newPeerToAdd;
        //Aggiungo la restante parte di peer usando la procedura Barabasi-Albert
        for(int i=NINITIALNODES; i<mNPeers; i++){

            //Aggiungo peer alla rete
            newPeerToAdd = new Peer(i, mC);
            mNetwork.insertElementAt(newPeerToAdd, i);

            int iNeighbor, idNeighbor;
            //Aggiungo m archi (numero fissato)
            for(int k=0; k<MEDGESTOADD; k++){

                //Devo aggiungere per forza m archi, se seleziono duplicato ci riprovo
                boolean isAdded;
                do {
                    iNeighbor = new Random().nextInt(mNetworkIDs.size());
                    idNeighbor = mNetworkIDs.elementAt(iNeighbor);

                    isAdded = mNetwork.elementAt(idNeighbor).addNeighbor(i);
                }
                while (!isAdded);

                //Aggiorno la view del nuovo peer
                newPeerToAdd.addNeighbor(idNeighbor);

                //Aggiorno neighborIDs
                mNetworkIDs.add(idNeighbor);
            }

            //Aggiungo cedgesadded volte il nodo i alla lista ids (evito duplicates problem)
            for (int j = 0; j < MEDGESTOADD; j++)
                mNetworkIDs.add(i);

        }
    }

    //Erdos-Renyi model to build a random network
    private void generateRandomNetwork(double pProb, int mC) {
        //Creo peer di id i
        for (int i = 0; i < NINITIALNODES; i++) {
            mNetwork.insertElementAt(new Peer(i, mC), i);
        }

        //For each possible pair of peers (nInitialPeers)
        for (int i = 0; i < NINITIALNODES; i++) {
            for (int j = i + 1; j < NINITIALNODES; j++) {
                //Add a connection with p probability
                if (Math.random() <= pProb) {
                    mNetwork.elementAt(i).addNeighbor(j);
                    mNetwork.elementAt(j).addNeighbor(i);

                    //Add ids to vector for scalefree
                    mNetworkIDs.add(i);
                    mNetworkIDs.add(j);
                }
            }
        }

    }

    /* GETTERS */

    public int getmNPeers() {
        return mNPeers;
    }

    Vector<Peer> getmNetwork() {
        return mNetwork;
    }

    //Restituisce la topology della rete (source, destination)
    StringBuilder getNetworkTopology() {
        StringBuilder res = new StringBuilder();

        for(Peer p: mNetwork){
            res.append(p.getTopology());
        }

        return res;
    }
}

import java.util.Random;
import java.util.Vector;

/* INCAPSULA UNA RETE SCALEFREE */
public class ScaleFreeNetwork {

    //Nodi iniziali da avere nella scale free network (seed nodes)
    protected static final int NINITIALNODES = 5;

    //Archi da aggiungere per ogni nodo che vado ad aggiungere
    protected static final int MEDGESTOADD = 2;

    //Numero di Peer nella rete
    protected int mNPeers;

    //Rete di Peer che formano la rete
    protected Vector<Peer> mNetwork;

    //Vettore di id, utile per aggiungere nodi per avere scale free network
    protected Vector<Integer> mNetworkIDs;


    //COSTRUTTORE: prende come parametri nPeers, grandezza cache, e parametro p per creare Random Network
    public ScaleFreeNetwork(int nPeers, int c, int pProb) throws IllegalArgumentException{
        if(nPeers < NINITIALNODES || c < 1 || pProb <= 0 || pProb > 1)
            throw new IllegalArgumentException("Wrong params");

        mNetwork = new Vector<>(nPeers);
        mNPeers = nPeers;


        generateScaleFree(pProb, c);
    }

    //Barabasi Albert alg. to generate a scale free network
    protected void generateScaleFree(int pProb, int mC){
        //Creo vettore di ID utile per ScaleFree
        mNetworkIDs = new Vector<>();

        //Genero una rete iniziale random
        generateRandomNetwork(pProb, mC);

        Peer newPeerToAdd;
        //Aggiungo la restante parte di peer usando la procedura Barabasi-Albert
        for(int i=NINITIALNODES; i<mNPeers; i++){
            newPeerToAdd = new Peer(i, mC);

            int iNeighbor, idNeighbor, cEdgesAdded = 0;
            //Aggiungo m archi (numero fissato)
            for(int k=0; k<MEDGESTOADD; k++){
                iNeighbor = new Random().nextInt(mNetworkIDs.size());
                idNeighbor = mNetworkIDs.elementAt(iNeighbor);

                //Aggiorno le view
                newPeerToAdd.addNeighbor(idNeighbor);
                if(mNetwork.elementAt(idNeighbor).addNeighbor(i))
                    cEdgesAdded++;

                //Aggiorno neighborIDs
                mNetworkIDs.add(idNeighbor);
            }

            //Aggiungo cedgesadded volte il nodo i alla lista ids (evito duplicates problem)
            for(int j=0; j<cEdgesAdded; j++)
                mNetworkIDs.add(i);

        }
    }

    //Erdos-Renyi model to build a random network
    protected void generateRandomNetwork(int pProb, int mC){

        //For each possible pair of peers (nInitialPeers)
        for (int i = 0; i < NINITIALNODES; i++) {
            //Creo peer di id i
            mNetwork.insertElementAt(new Peer(i, mC), i);

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
}

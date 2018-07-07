import java.util.Random;
import java.util.Vector;

/* CONTIENE LA LOGICA DEL PROTOCOLLO NEWSCAST */
public class DynamicOverlayGenMain {

    //The network
    protected int mNPeers;
    protected Vector<Peer> mPeers;

    //Logic timestamp: parte da 1 (0 è l'inizio) e ad ogni passo si incrementa di 1
    protected Integer mLogicTimestamp = 1;

    /* 2 costruttori: uno lavora su grid network, l'altro su scale free network */

    //Costruttore per gridtable -> ha bisogno solo di npeers e c (grandezza cache)
    public DynamicOverlayGenMain(int nPeers, int nC){

        //Controllo semantica argomenti
        if(!checkArgs(nPeers, nC, 0.5)){
            System.err.println("------------------------------------------------------------------");
            System.err.println("Wrong semantic in args");
            System.err.println("------------------------------------------------------------------");
        }

        //Costruisco la rete e prendo un riferimento ai peer
        GridNetwork network = new GridNetwork(nPeers, nC);
        mNPeers = nPeers;
        mPeers = network.getmNetwork();

        /* DEBUG */
        for(Peer pToStamp: mPeers){
            System.out.println(pToStamp.getTopology());
        }

        //Eseguo il protocollo newscast
        executeNewscastProtocol(mPeers);

        //TODO: SCRIVO RETE SU DISCO
        /* DEBUG */
        for(Peer pToSTamp: mPeers){
            System.out.println(pToSTamp.getTopology());
        }
    }

    //Costruttore per scalefree -> ha bisogno di npeers, di c, e di p per BA alg.
    public DynamicOverlayGenMain(int nPeers, int nC, double p){

        //Controllo semantica argomenti
        if(!checkArgs(nPeers, nC, p)){
            System.err.println("------------------------------------------------------------------");
            System.err.println("Wrong semantic in args");
            System.err.println("------------------------------------------------------------------");
        }

        //Costruisco la rete e prendo un riferimento ai peer
        ScaleFreeNetwork network = new ScaleFreeNetwork(nPeers, nC, p);
        mNPeers = nPeers;
        mPeers = network.getmNetwork();

        /* DEBUG */
        for(Peer pToStamp: mPeers){
            System.out.println(pToStamp.getTopology());
        }

        //Eseguo il protocollo newscast
        executeNewscastProtocol(mPeers);

        //TODO: SCRIVO RETE SU DISCO
        /* DEBUG */
        for(Peer pToStamp: mPeers){
            System.out.println(pToStamp.getTopology());
        }
    }

    //Return false if there is a problem in args, true otherwise
    private boolean checkArgs(int nPeers, int nC, double p){
        if(nPeers < 1 || nC > nPeers || nC < 1 || p <= 0 || p > 1)
            return false;
        else
            return true;
    }

    //Esegue il protocollo newscast su tutta la rete
    private void executeNewscastProtocol(Vector<Peer> peers){

        //Per ogni peer eseguo il protocollo
        for(Peer p: peers){
            Peer otherPeer;

            //Ciclo fin quando non seleziono un peer diverso da me
            do {
                otherPeer = selectPeer(peers);
            } while (otherPeer.equals(p));

            //Aggiorno le view dei due peer
            updateState(p, otherPeer);

            //Incremento il timestamp
            mLogicTimestamp++;
        }

    }

    //Seleziona un peer nella rete (per poi scambiare le view)
    private Peer selectPeer(Vector<Peer> peerVector){
        int indSelected = new Random().nextInt(peerVector.size());

        return peerVector.elementAt(indSelected);
    }

    //Aggiorna le view di due peer (tramite uno scambio)
    private void updateState(Peer a, Peer b){

        //Per lo scambio è necessario conoscere timestamp corrente
        a.exchangeViews(b, mLogicTimestamp);
    }

    //Metodo main
    public static void main(String[] args){

        //I parametri sono 2 o 3
        if(args.length < 3 || args.length > 4){
            System.err.println("------------------------------------------------------------------");
            System.err.println("Parameters are: nNodes, g,c OR s,c,p");
            System.err.println("g stands for grid");
            System.err.println("s stands for scalefree and it needs also p, param for BA algorithm");
            System.err.println("c is the number of items in the cache of each peer");
            System.err.println("------------------------------------------------------------------");
        }
        else{
            try {
                if (args.length == 3) {
                    new DynamicOverlayGenMain(Integer.parseInt(args[0]), Integer.parseInt(args[2]));
                } else {
                    new DynamicOverlayGenMain(Integer.parseInt(args[0]), Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]));
                }
            }
            catch(NumberFormatException e){
                System.err.println("------------------------------------------------------------------");
                System.err.println("Error in params format");
                System.err.println("------------------------------------------------------------------");
            }
        }


    }
}

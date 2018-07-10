import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

/* CONTIENE LA LOGICA DEL PROTOCOLLO NEWSCAST */
public class DynamicOverlayGenMain {

    //The network
    private Vector<Peer> mPeers;

    //Logic timestamp: parte da 1 (0 è l'inizio) e ad ogni passo si incrementa di 1
    private Integer mLogicTimestamp = 1;

    //Per scrivere una topologia di rete su file
    private String GRIDTOPOLOGYFILENAME = "GridTopology";
    private String SCALEFREETOPOLOGYFILENAME = "ScaleFreeTopology";
    private String GENERATEDTOPOLOGYFILENAME = "GeneratedTopology";
    private FileWriter mTopologyFW;
    private PrintWriter mTopologyPW;


    /* 2 costruttori: uno lavora su grid network, l'altro su scale free network */

    //Costruttore per gridtable -> ha bisogno solo di npeers e c (grandezza cache)
    private DynamicOverlayGenMain(int nPeers, int nC) throws IOException {

        //Controllo semantica argomenti
        if (wrongArgs(nPeers, nC, 0.5)) {
            System.err.println("------------------------------------------------------------------");
            System.err.println("Wrong semantic in args");
            System.err.println("------------------------------------------------------------------");
        }

        //Costruisco la rete e prendo un riferimento ai peer
        System.out.println("I'm building the grid network...");
        GridNetwork network = new GridNetwork(nPeers, nC);
        mPeers = network.getmNetwork();

        //Stampo topologia rete di partenza su disco
        System.out.println("I'm saving the grid network on file...");
        GRIDTOPOLOGYFILENAME = GRIDTOPOLOGYFILENAME.concat(nPeers + "nodes.csv");
        mTopologyFW = new FileWriter(GRIDTOPOLOGYFILENAME);
        mTopologyPW = new PrintWriter(mTopologyFW);
        printNetworkOnFile(mPeers, mTopologyPW);

        //Eseguo il protocollo newscast
        System.out.println("I'm executing the protocol on the network...");
        executeNewscastProtocol(mPeers);

        //Stampo rete creata su disco
        System.out.println("I'm saving the generated network on file...");
        GENERATEDTOPOLOGYFILENAME = GENERATEDTOPOLOGYFILENAME.concat(nPeers + "nodes" + nC + "cache.csv");
        mTopologyFW = new FileWriter(GENERATEDTOPOLOGYFILENAME);
        mTopologyPW = new PrintWriter(mTopologyFW);
        printNetworkOnFile(mPeers, mTopologyPW);

    }

    //Costruttore per scalefree -> ha bisogno di npeers, di c, e di p per BA alg.
    private DynamicOverlayGenMain(int nPeers, int nC, double p) throws IOException {

        //Controllo semantica argomenti
        if (wrongArgs(nPeers, nC, p)) {
            System.err.println("------------------------------------------------------------------");
            System.err.println("Wrong semantic in args");
            System.err.println("------------------------------------------------------------------");
        }

        //Costruisco la rete e prendo un riferimento ai peer
        System.out.println("I'm building the ScaleFree network...");
        ScaleFreeNetwork network = new ScaleFreeNetwork(nPeers, nC, p);
        mPeers = network.getmNetwork();

        //Stampo topologia rete di partenza su disco
        System.out.println("I'm saving the ScaleFree network on file...");
        SCALEFREETOPOLOGYFILENAME = SCALEFREETOPOLOGYFILENAME.concat(nPeers + "nodes.csv");
        mTopologyFW = new FileWriter(SCALEFREETOPOLOGYFILENAME);
        mTopologyPW = new PrintWriter(mTopologyFW);
        printNetworkOnFile(mPeers, mTopologyPW);

        //Eseguo il protocollo newscast
        System.out.println("I'm executing the protocol on the network...");
        executeNewscastProtocol(mPeers);

        //Stampo rete creata su disco
        System.out.println("I'm saving the generated network on file...");
        GENERATEDTOPOLOGYFILENAME = GENERATEDTOPOLOGYFILENAME.concat(nPeers + "nodes" + nC + "cache.csv");
        mTopologyFW = new FileWriter(GENERATEDTOPOLOGYFILENAME);
        mTopologyPW = new PrintWriter(mTopologyFW);
        printNetworkOnFile(mPeers, mTopologyPW);
    }

    //Return false if there is a problem in args, true otherwise
    private boolean wrongArgs(int nPeers, int nC, double p) {
        return nPeers < 1 || nC > nPeers || nC < 1 || p <= 0 || p > 1;
    }

    //Esegue il protocollo newscast su tutta la rete
    private void executeNewscastProtocol(Vector<Peer> peers){

        System.out.println("Current timestamp: " + mLogicTimestamp);

        //Per ogni peer eseguo il protocollo
        for(Peer p: peers){
            Peer otherPeer;

            //Ciclo fin quando non seleziono un peer diverso da me
            do {
                otherPeer = selectPeer(peers);
            } while (otherPeer.equals(p));

            //Aggiorno le view dei due peer
            System.out.println("Exchanging views between peers: " + p.getmId() + ", " + otherPeer.getmId());
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
        } else {
            try {
                if (args.length == 3) {
                    new DynamicOverlayGenMain(Integer.parseInt(args[0]), Integer.parseInt(args[2]));
                } else {
                    new DynamicOverlayGenMain(Integer.parseInt(args[0]), Integer.parseInt(args[2]),
                            Double.parseDouble(args[3]));
                }
            } catch (NumberFormatException e) {
                System.err.println("------------------------------------------------------------------");
                System.err.println("Error in params format");
                System.err.println("------------------------------------------------------------------");
            } catch (IOException e) {
                System.err.println("------------------------------------------------------------------");
                System.err.println("Error in IO operation");
                System.err.println("------------------------------------------------------------------");
            }
        }


    }

    //Salva la topologia di una rete su disco
    private void printNetworkOnFile(Vector<Peer> peers, PrintWriter pw) {

        //Stampo le etichette di titolo source , dest
        pw.println("Source,Target");

        //Ciclo su ogni nodo e stampo la sua topology (view)
        for (Peer pToStamp : peers) {
            pw.print(pToStamp.getTopology());
        }

        //Chiudo file
        pw.close();
    }
}

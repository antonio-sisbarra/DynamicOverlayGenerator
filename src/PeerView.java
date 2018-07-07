import java.util.*;

/* INCAPSULA LA STRUTTURA DI UNA VIEW */
public class PeerView {

    //Grandezza cache e cache (vector di coppie id,timestamp)
    protected int mC;
    protected TreeSet<PeerViewEntry> mCache;

    //Costruttore 1: prende come parametro solo la grandezza della cache
    public PeerView(int c){
        //Uso reverse order per avere ordinamento decrescente
        mCache = new TreeSet<>(Collections.reverseOrder());

        mC = c;
    }

    //Costruttore 2: prende come parametro la grandezza della cache e una map di vicini (con timestamp già settati)
    public PeerView(int c, Collection<PeerViewEntry> neighbors){
        //Uso reverse order per avere ordinamento decrescente
        mCache = new TreeSet<>(Collections.reverseOrder());
        mCache.addAll(neighbors);

        mC = c;
    }

    //Restituisce un vector di peerId (i vicini)
    protected Vector<Integer> getNeighbors(){
        if(mCache == null) return null;

        Vector<Integer> neighborsId = new Vector<>(mC);
        for(PeerViewEntry e: mCache){
            neighborsId.add(e.getmId());
        }

        return neighborsId;
    }

    //Effettua il merge tra una view e l'attuale (scegliendo peer con timestamp più recente)
    protected void mergeView(PeerView otherView){
        if(mCache == null || otherView == null || otherView.getmCache() == null)
            return;

        TreeSet<PeerViewEntry> newCacheAfterMerge = new TreeSet<>();

        mCache.addAll(otherView.getmCache());
        int countInserted = 0;
        for(PeerViewEntry e: mCache){
            newCacheAfterMerge.add(e);
            countInserted++;

            //Se ho inserito abbastanza elementi nella cache nuova -> break
            if(countInserted == mC)
                break;
        }

        mCache = newCacheAfterMerge;
    }

    //Aggiunge un nodo alla view (default timestamp 0)
    public boolean addViewEntry(Integer id){
        PeerViewEntry entry = new PeerViewEntry(id, 0);

        if(mCache != null)
            return mCache.add(entry);

        else return false;
    }

    /* GETTERS */
    public int getmC() {
        return mC;
    }

    public TreeSet<PeerViewEntry> getmCache() {
        return mCache;
    }


}

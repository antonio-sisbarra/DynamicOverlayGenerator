/* INCAPSULA LA COPPIA ID, TIMESTAMP; ORDINAMENTO DESCRESCENTE PER TIMESTAMP */
public class PeerViewEntry implements Comparable<PeerViewEntry>{

    int mId;
    Integer mTimeStamp;

    public PeerViewEntry(int id, int timestmp){
        mId = id;
        mTimeStamp = timestmp;
    }

    /* GETTERS */
    public int getmId() {
        return mId;
    }

    public int getmTimeStamp() {
        return mTimeStamp;
    }

    /* UTILE PER ORDINAMENTO NELLA PEERVIEW */
    @Override
    public int compareTo(PeerViewEntry o) {
        /**Return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         **/
        if(mTimeStamp != null && o != null)
            return mTimeStamp.compareTo(o.getmTimeStamp());

        else return 0;
    }

}

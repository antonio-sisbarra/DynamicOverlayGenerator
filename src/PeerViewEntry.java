import java.util.Objects;

/* INCAPSULA LA COPPIA ID, TIMESTAMP; ORDINAMENTO DECRESCENTE PER TIMESTAMP */
public class PeerViewEntry implements Comparable<PeerViewEntry>{

    private Integer mId;
    private Integer mTimeStamp;

    PeerViewEntry(int id, int timestmp) {
        mId = id;
        mTimeStamp = timestmp;
    }

    /* GETTERS */
    int getmId() {
        return mId;
    }

    int getmTimeStamp() {
        return mTimeStamp;
    }

    /* UTILE PER ORDINAMENTO NELLA PEERVIEW */
    @Override
    public int compareTo(PeerViewEntry o) {
        if(mTimeStamp != null && o != null)
            //Se i timestamp sono diversi si ordina per timestamp, altrimenti per id
            if(mTimeStamp.compareTo(o.getmTimeStamp()) > 0 || mTimeStamp.compareTo(o.getmTimeStamp()) < 0)
                return mTimeStamp.compareTo(o.getmTimeStamp());
            else
                return mId.compareTo(o.getmId());

        else return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeerViewEntry that = (PeerViewEntry) o;
        return mId.equals(that.mId) &&
                Objects.equals(mTimeStamp, that.mTimeStamp);
    }
}

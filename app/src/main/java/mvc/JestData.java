package mvc;

import com.path.android.jobqueue.JobManager;

import java.util.Observable;

import cmput301w16t15.shareo.ShareoApplication;
import io.searchbox.annotations.JestId;
import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-03-05.
 */
public abstract class JestData extends MVCModel {
    @JestId
    private String JestID;

    private transient ShareoData dataSource;
    private static ShareoData defaultDataSource = ShareoData.getInstance();

    /**
     * Returns the JestID associated with this object. Never returns null, but will instead throw
     * an exception.
     * @return The JestID.
     * @throws NullIDException No JestID has been set, or it has been set to null.
     */
    public String getJestID() throws NullIDException {
        if (JestID == null) {
            if (JestID == null) {
                throw new NullIDException();
            }
        }
        return JestID;
    }

    public void setJestID(String ID) {
        JestID = ID;
    }

    public ShareoData getDataSource() {
        if (dataSource == null) {
            try {
                dataSource = defaultDataSource;
            } catch (ClassCastException e) {
                dataSource = null;
            }
        }
        return dataSource;
    }

    public void setDataSource(ShareoData model) {
        dataSource = model;
    }

    @Override
    public boolean equals(Object other) {
        try {
            if (other instanceof JestData) {
                return ((JestData)other).getJestID().equals(this.getJestID());
            }
        } catch (NullIDException e) {
            return false;
        }
        return false;
    }
}

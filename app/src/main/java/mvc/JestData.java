package mvc;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;

import io.searchbox.annotations.JestId;
import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-03-05.
 */
public abstract class JestData extends Observable implements Serializable {

    private transient ShareoData dataSource;
    private static ShareoData defaultDataSource = ShareoData.getInstance();

    public abstract String getJestID() throws NullIDException;

    public abstract void setJestID(String ID);

    public ShareoData getDataSource() {
        if (dataSource == null) {
            dataSource = defaultDataSource;
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

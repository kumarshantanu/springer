package starfish.impl;

import java.util.List;

import javax.sql.DataSource;

public interface ReplicationSlavesPointer {

    public List<DataSource> getDataSources();

}
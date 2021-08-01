package org.xersys.imbentaryofx.listener;

import javax.sql.rowset.CachedRowSet;


public interface CachedRowsetCallback {
    public void Result(CachedRowSet foValue);
    public void FormClosing();
}

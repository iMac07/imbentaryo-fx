package org.xersys.imbentaryofx.listener;

public interface DetailUpdateCallback {
    public void Result(int fnRow, int fnIndex, Object foValue);
    public void RemovedItem(int fnRow);
    public void FormClosing();
}

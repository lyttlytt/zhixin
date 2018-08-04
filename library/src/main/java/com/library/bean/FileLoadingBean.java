package com.library.bean;

/**
 * Author：SEELE on 2016/7/25 14:34
 * Contact: studylifetime@sina.com
 */
public class FileLoadingBean {
    private long total;
    private long currentlength;
    private  boolean doneFlag;

    public FileLoadingBean(long currentlength, long total) {
        this.total=total;
        this.currentlength=currentlength;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrentlength() {
        return currentlength;
    }

    public void setCurrentlength(long currentlength) {
        this.currentlength = currentlength;
    }

    public boolean isDoneFlag() {
        return doneFlag;
    }

    public void setDoneFlag(boolean doneFlag) {
        this.doneFlag = doneFlag;
    }
}

package org.dan.entity;

public class BaseRecord {
    private String indexName;
    private int pv;
    private long uv;

    public BaseRecord(String indexName, int pv, long uv) {
        this.indexName = indexName;
        this.pv = pv;
        this.uv = uv;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }
}

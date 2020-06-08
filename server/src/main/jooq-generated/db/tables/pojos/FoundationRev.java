/*
 * This file is generated by jOOQ.
 */
package db.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FoundationRev implements Serializable {

    private static final long serialVersionUID = 1644123494;

    private Integer   version;
    private String    description;
    private Timestamp migratedOn;
    private Integer   executionTimeSec;

    public FoundationRev() {}

    public FoundationRev(FoundationRev value) {
        this.version = value.version;
        this.description = value.description;
        this.migratedOn = value.migratedOn;
        this.executionTimeSec = value.executionTimeSec;
    }

    public FoundationRev(
        Integer   version,
        String    description,
        Timestamp migratedOn,
        Integer   executionTimeSec
    ) {
        this.version = version;
        this.description = description;
        this.migratedOn = migratedOn;
        this.executionTimeSec = executionTimeSec;
    }

    public Integer getVersion() {
        return this.version;
    }

    public FoundationRev setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public FoundationRev setDescription(String description) {
        this.description = description;
        return this;
    }

    public Timestamp getMigratedOn() {
        return this.migratedOn;
    }

    public FoundationRev setMigratedOn(Timestamp migratedOn) {
        this.migratedOn = migratedOn;
        return this;
    }

    public Integer getExecutionTimeSec() {
        return this.executionTimeSec;
    }

    public FoundationRev setExecutionTimeSec(Integer executionTimeSec) {
        this.executionTimeSec = executionTimeSec;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FoundationRev (");

        sb.append(version);
        sb.append(", ").append(description);
        sb.append(", ").append(migratedOn);
        sb.append(", ").append(executionTimeSec);

        sb.append(")");
        return sb.toString();
    }
}

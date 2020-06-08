/*
 * This file is generated by jOOQ.
 */
package db.tables.pojos;


import db.enums.Reaction;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Takereaction implements Serializable {

    private static final long serialVersionUID = -202143652;

    private Integer   takeId;
    private Integer   userId;
    private Reaction  kind;
    private Timestamp reactedAt;
    private String    reactedIp;

    public Takereaction() {}

    public Takereaction(Takereaction value) {
        this.takeId = value.takeId;
        this.userId = value.userId;
        this.kind = value.kind;
        this.reactedAt = value.reactedAt;
        this.reactedIp = value.reactedIp;
    }

    public Takereaction(
        Integer   takeId,
        Integer   userId,
        Reaction  kind,
        Timestamp reactedAt,
        String    reactedIp
    ) {
        this.takeId = takeId;
        this.userId = userId;
        this.kind = kind;
        this.reactedAt = reactedAt;
        this.reactedIp = reactedIp;
    }

    public Integer getTakeId() {
        return this.takeId;
    }

    public Takereaction setTakeId(Integer takeId) {
        this.takeId = takeId;
        return this;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Takereaction setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Reaction getKind() {
        return this.kind;
    }

    public Takereaction setKind(Reaction kind) {
        this.kind = kind;
        return this;
    }

    public Timestamp getReactedAt() {
        return this.reactedAt;
    }

    public Takereaction setReactedAt(Timestamp reactedAt) {
        this.reactedAt = reactedAt;
        return this;
    }

    public String getReactedIp() {
        return this.reactedIp;
    }

    public Takereaction setReactedIp(String reactedIp) {
        this.reactedIp = reactedIp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Takereaction (");

        sb.append(takeId);
        sb.append(", ").append(userId);
        sb.append(", ").append(kind);
        sb.append(", ").append(reactedAt);
        sb.append(", ").append(reactedIp);

        sb.append(")");
        return sb.toString();
    }
}

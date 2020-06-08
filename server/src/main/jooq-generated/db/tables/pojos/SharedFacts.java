/*
 * This file is generated by jOOQ.
 */
package db.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SharedFacts implements Serializable {

    private static final long serialVersionUID = 1156505536;

    private Integer    sharedBy;
    private Timestamp  sharedOn;
    private String     sharedIp;
    private Integer    viewCount;
    private String     title;
    private Integer    urlVersion;
    private String     factid;
    private BigDecimal highlightStart;
    private BigDecimal highlightEnd;
    private BigDecimal viewStart;
    private BigDecimal viewEnd;

    public SharedFacts() {}

    public SharedFacts(SharedFacts value) {
        this.sharedBy = value.sharedBy;
        this.sharedOn = value.sharedOn;
        this.sharedIp = value.sharedIp;
        this.viewCount = value.viewCount;
        this.title = value.title;
        this.urlVersion = value.urlVersion;
        this.factid = value.factid;
        this.highlightStart = value.highlightStart;
        this.highlightEnd = value.highlightEnd;
        this.viewStart = value.viewStart;
        this.viewEnd = value.viewEnd;
    }

    public SharedFacts(
        Integer    sharedBy,
        Timestamp  sharedOn,
        String     sharedIp,
        Integer    viewCount,
        String     title,
        Integer    urlVersion,
        String     factid,
        BigDecimal highlightStart,
        BigDecimal highlightEnd,
        BigDecimal viewStart,
        BigDecimal viewEnd
    ) {
        this.sharedBy = sharedBy;
        this.sharedOn = sharedOn;
        this.sharedIp = sharedIp;
        this.viewCount = viewCount;
        this.title = title;
        this.urlVersion = urlVersion;
        this.factid = factid;
        this.highlightStart = highlightStart;
        this.highlightEnd = highlightEnd;
        this.viewStart = viewStart;
        this.viewEnd = viewEnd;
    }

    public Integer getSharedBy() {
        return this.sharedBy;
    }

    public SharedFacts setSharedBy(Integer sharedBy) {
        this.sharedBy = sharedBy;
        return this;
    }

    public Timestamp getSharedOn() {
        return this.sharedOn;
    }

    public SharedFacts setSharedOn(Timestamp sharedOn) {
        this.sharedOn = sharedOn;
        return this;
    }

    public String getSharedIp() {
        return this.sharedIp;
    }

    public SharedFacts setSharedIp(String sharedIp) {
        this.sharedIp = sharedIp;
        return this;
    }

    public Integer getViewCount() {
        return this.viewCount;
    }

    public SharedFacts setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public SharedFacts setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getUrlVersion() {
        return this.urlVersion;
    }

    public SharedFacts setUrlVersion(Integer urlVersion) {
        this.urlVersion = urlVersion;
        return this;
    }

    public String getFactid() {
        return this.factid;
    }

    public SharedFacts setFactid(String factid) {
        this.factid = factid;
        return this;
    }

    public BigDecimal getHighlightStart() {
        return this.highlightStart;
    }

    public SharedFacts setHighlightStart(BigDecimal highlightStart) {
        this.highlightStart = highlightStart;
        return this;
    }

    public BigDecimal getHighlightEnd() {
        return this.highlightEnd;
    }

    public SharedFacts setHighlightEnd(BigDecimal highlightEnd) {
        this.highlightEnd = highlightEnd;
        return this;
    }

    public BigDecimal getViewStart() {
        return this.viewStart;
    }

    public SharedFacts setViewStart(BigDecimal viewStart) {
        this.viewStart = viewStart;
        return this;
    }

    public BigDecimal getViewEnd() {
        return this.viewEnd;
    }

    public SharedFacts setViewEnd(BigDecimal viewEnd) {
        this.viewEnd = viewEnd;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SharedFacts (");

        sb.append(sharedBy);
        sb.append(", ").append(sharedOn);
        sb.append(", ").append(sharedIp);
        sb.append(", ").append(viewCount);
        sb.append(", ").append(title);
        sb.append(", ").append(urlVersion);
        sb.append(", ").append(factid);
        sb.append(", ").append(highlightStart);
        sb.append(", ").append(highlightEnd);
        sb.append(", ").append(viewStart);
        sb.append(", ").append(viewEnd);

        sb.append(")");
        return sb.toString();
    }
}

/**
 * created on 2017年11月20日 下午5:32:12
 */
package cn.utstarcom.vmsadapter.md5verify.dto;

/**
 * @author UTSC0167
 * @date 2017年11月20日
 *
 */
public final class MediaContentStatus {

    private final int mediaContentId;
    private String srcFileurl;
    private String localFileurl;
    private Character status;

    public MediaContentStatus(int mediaContentId) {

        this.mediaContentId = mediaContentId;
    }

    public MediaContentStatus(int mediaContentId, String srcFileurl, String localFileurl,
            Character status) {
        super();
        this.mediaContentId = mediaContentId;
        this.srcFileurl = srcFileurl;
        this.localFileurl = localFileurl;
        this.status = status;
    }

    public int getMediaContentid() {
        return mediaContentId;
    }

    public String getSrcFileurl() {
        return srcFileurl;
    }

    public void setSrcFileurl(String srcFileurl) {
        this.srcFileurl = srcFileurl;
    }

    public String getLocalFileUrl() {
        return localFileurl;
    }

    public void setLocalFileUrl(String localFileurl) {
        this.localFileurl = localFileurl;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((localFileurl == null) ? 0 : localFileurl.hashCode());
        result = prime * result + mediaContentId;
        result = prime * result + ((srcFileurl == null) ? 0 : srcFileurl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaContentStatus other = (MediaContentStatus) obj;
        if (localFileurl == null) {
            if (other.localFileurl != null)
                return false;
        } else if (!localFileurl.equals(other.localFileurl))
            return false;
        if (mediaContentId != other.mediaContentId)
            return false;
        if (srcFileurl == null) {
            if (other.srcFileurl != null)
                return false;
        } else if (!srcFileurl.equals(other.srcFileurl))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MediaContentStatus [mediaContentId=" + mediaContentId + ", srcFileurl=" + srcFileurl
                + ", localFileurl=" + localFileurl + ", status=" + status + "]";
    }
}

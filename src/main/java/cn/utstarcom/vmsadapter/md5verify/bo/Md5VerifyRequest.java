/**
 * created on 2017年11月21日 上午9:51:00
 */
package cn.utstarcom.vmsadapter.md5verify.bo;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public class Md5VerifyRequest {

    private Character request;
    private String file;

    public Md5VerifyRequest() {
    };

    public Md5VerifyRequest(Character request, String file) {
        this.request = request;
        this.file = file;
    }

    public Character getRequest() {
        return request;
    }

    public void setRequest(Character request) {
        this.request = request;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Md5VerifyRequest [request=" + request + ", file=" + file + "]";
    }
}

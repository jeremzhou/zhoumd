/**
 * created on 2017年11月21日 上午9:53:30
 */
package cn.utstarcom.vmsadapter.md5verify.bo;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public class Md5VerifyResponse {

    private Character response;
    private String file;
    private String md5;
    private String success;

    public Md5VerifyResponse() {

    };

    public Md5VerifyResponse(Character response, String file, String md5, String success) {
        this.response = response;
        this.file = file;
        this.md5 = md5;
        this.success = success;
    }

    public Character getResponse() {
        return response;
    }

    public void setResponse(Character response) {
        this.response = response;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}

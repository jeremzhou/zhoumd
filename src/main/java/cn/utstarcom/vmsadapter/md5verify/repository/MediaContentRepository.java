/**
 * created on 2017年11月21日 上午11:25:09
 */
package cn.utstarcom.vmsadapter.md5verify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.utstarcom.vmsadapter.md5verify.pojo.MediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public interface MediaContentRepository extends JpaRepository<MediaContent, Integer> {

    @Modifying
    @Query(value = "UPDATE mediacontent SET status = ?2 WHERE mediacontentid = ?1", nativeQuery = true)
    int updateStatus(int mediaContentId, Character status);

    @Modifying
    @Query(value = "INSERT INTO mediacontent (mediacontentid, fileurl, status) VALUES (?1, ?2, ?3)", nativeQuery = true)
    int saveByNativeTest2(int mediacontentId, String fileUrl, Character status);

    @Modifying
    @Query(value = "DELETE FROM mediacontent WHERE mediacontentid = ?1", nativeQuery = true)
    int deleteByPkTest2(int mediaContentId);

    @Query(value = "Select mediacontentid, substr(fileurl, instr(fileurl, '/', -1, 4)), substr(fileurl, instr(fileurl, '/', 1, 3)), status FROM mediacontent WHERE mediacontentid = ?1", nativeQuery = true)
    List<Object> queryByPkTest2(int mediaContentId);
}

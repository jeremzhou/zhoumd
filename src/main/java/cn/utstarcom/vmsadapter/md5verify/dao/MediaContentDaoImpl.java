/**
 * created on 2017年11月21日 下午5:30:04
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.utstarcom.vmsadapter.md5verify.common.ProgramStatusType;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.MediaContent;
import cn.utstarcom.vmsadapter.md5verify.repository.MediaContentRepository;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
@Repository
public class MediaContentDaoImpl implements MediaContentDao {

    private static final Logger logger = LoggerFactory.getLogger(MediaContentDaoImpl.class);

    private static final String QueryPendingVerifyListSql = "             \n"
            + "SELECT                                                     \n"
            + "    m.mediacontentid,                                      \n"
            + "    substr(m.fileurl, instr(m.fileurl, '/', -1, 4)),       \n"
            + "    substr(m.fileurl, instr(m.fileurl, '/', 1, 3)),        \n"
            + "    m.status                                               \n"
            + "FROM                                                       \n"
            + "    mediacontent m,                                        \n"
            + "    programmediacontent pm                                 \n"
            + "WHERE                                                      \n"
            + "    (                                                      \n"
            + "        m.status = '" + ProgramStatusType.WAIT_MD5VERIFY + "'\n"
            + "        OR m.status = '" + ProgramStatusType.MD5VERIFY_FAILURE + "'\n"
            + "    )                                                      \n"
            + "    AND m.mediacontentid = pm.mediacontentid               \n"
            + "    AND pm.objtype = '1'                                   \n"
            + "    AND pm.objid = ?                                         ";

    private static final String saveByNativeTestSql = "INSERT INTO mediacontent (mediacontentid, fileurl, status) VALUES (?, ?, ?)";

    private static final String deleteByPkTestSql = "DELETE FROM mediacontent WHERE mediacontentid = ?";

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private MediaContentRepository mediaContentRepository;

    @Override
    public List<MediaContentStatus> queryPendingVerifyList(int programId) {

        return this.sessionFactory.getCurrentSession().doReturningWork((Connection connection) -> {

            final List<MediaContentStatus> mediaContentStatusList = new LinkedList<>();
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(QueryPendingVerifyListSql);
                preparedStatement.setInt(1, programId);
                ResultSet resultSet = preparedStatement.executeQuery();
                convertResultSet(resultSet, mediaContentStatusList);
            } catch (Exception e) {
                logger.error("queryPendingVerifyList for programId: {} generate exception.",
                        programId);
                throw e;
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
            return mediaContentStatusList;
        });
    }

    @Override
    public int updateStatus(int mediaContentId, Character status) {

        return this.mediaContentRepository.updateStatus(mediaContentId, status);
    }

    @Override
    public int saveByNativeTest(MediaContent mediaContent) {

        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork((Connection connection) -> {

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(saveByNativeTestSql);
                preparedStatement.setInt(1, mediaContent.getMediaContentid());
                preparedStatement.setString(2, mediaContent.getFileurl());
                preparedStatement.setString(3, mediaContent.getStatus().toString());
                return preparedStatement.executeUpdate();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        });
    }

    @Override
    public int deleteByPkTest(int mediaContentId) {

        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork((Connection connection) -> {

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(deleteByPkTestSql);
                preparedStatement.setInt(1, mediaContentId);
                return preparedStatement.executeUpdate();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        });
    }

    @Override
    public int saveByNativeTest2(MediaContent mediaContent) {

        return this.mediaContentRepository.saveByNativeTest2(mediaContent.getMediaContentid(),
                mediaContent.getFileurl(), mediaContent.getStatus());
    }

    @Override
    public int deleteByPkTest2(int mediaContentId) {

        return this.mediaContentRepository.deleteByPkTest2(mediaContentId);
    }

    @Override
    public MediaContentStatus queryByPkTest2(int mediaContentId) {

        List<Object> objList = this.mediaContentRepository.queryByPkTest2(mediaContentId);
        MediaContentStatus mediaContentStatus = new MediaContentStatus(mediaContentId);
        if (objList.size() > 0) {
            Object[] cells = (Object[]) objList.get(0);
            mediaContentStatus.setSrcFileurl((String) (cells[1]));
            mediaContentStatus.setLocalFileUrl((String) (cells[2]));
            mediaContentStatus.setStatus((Character) (cells[3]));
        }

        return mediaContentStatus;
    }

    private final void convertResultSet(ResultSet resultSet,
            List<MediaContentStatus> mediaContentStatusList) throws SQLException {

        while (resultSet.next()) {
            MediaContentStatus mediaContentStatus = new MediaContentStatus(resultSet.getInt(1));
            mediaContentStatus.setSrcFileurl(resultSet.getString(2));
            mediaContentStatus.setLocalFileUrl(resultSet.getString(3));
            mediaContentStatus.setStatus(resultSet.getString(4).toCharArray()[0]);
            mediaContentStatusList.add(mediaContentStatus);
        }
    }
}

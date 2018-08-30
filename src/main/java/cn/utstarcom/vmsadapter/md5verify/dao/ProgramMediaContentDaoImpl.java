/**
 * created on 2017年11月30日 下午2:23:25
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.utstarcom.vmsadapter.md5verify.pojo.ProgramMediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月30日
 *
 */
@Repository
public class ProgramMediaContentDaoImpl implements ProgramMediaContentDao {

    private static final String saveByNativeTestSql = "INSERT INTO programmediacontent (programmediacontentid, objtype, objid, mediacontentid) VALUES (?, ?, ?, ?)";

    private static final String deleteByPkTestSql = "DELETE FROM programmediacontent WHERE programmediacontentid = ?";

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int saveByNativeTest(ProgramMediaContent programMediaContent) {

        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork((Connection connection) -> {

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(saveByNativeTestSql);
                preparedStatement.setInt(1, programMediaContent.getProgrammediaContentId());
                preparedStatement.setString(2, programMediaContent.getObjtype().toString());
                preparedStatement.setInt(3, programMediaContent.getObjid());
                preparedStatement.setInt(4, programMediaContent.getMediaContentid());
                return preparedStatement.executeUpdate();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        });
    }

    @Override
    public int deleteByPkTest(int programMediaContentId) {

        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork((Connection connection) -> {

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(deleteByPkTestSql);
                preparedStatement.setInt(1, programMediaContentId);
                return preparedStatement.executeUpdate();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        });
    }

}

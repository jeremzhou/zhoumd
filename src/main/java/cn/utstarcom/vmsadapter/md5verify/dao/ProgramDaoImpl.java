/**
 * created on 2017年11月21日 下午3:28:27
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.utstarcom.vmsadapter.md5verify.common.ProgramStatusType;
import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.Program;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
@Repository
public class ProgramDaoImpl implements ProgramDao {

    private static final Logger logger = LoggerFactory.getLogger(ProgramDaoImpl.class);

    private static final String strProgramId = "programid";
    private static final String strStatus = "status";
    private static final String strMd5ChkCnt = "md5chkcnt";
    private static final String strMd5ChkLastTime = "md5chklasttime";
    private static final String queryPendingVerifyListSql = "               \n"
            + "SELECT                                                       \n"
            + "    programid,                                               \n"
            + "    status,                                                  \n"
            + "    md5chkcnt,                                               \n"
            + "    md5chklasttime                                           \n"
            + "FROM                                                         \n"
            + "    program                                                  \n"
            + "WHERE                                                        \n"
            + "    (                                                        \n"
            + "        status = '" + ProgramStatusType.WAIT_MD5VERIFY + "'\n"
            + "    )                                                        \n"
            + "    OR (                                                     \n"
            + "        status =  '" + ProgramStatusType.MD5VERIFY_FAILURE + "'\n"
            + "        AND md5chkcnt < ?                                    \n"
            + "        AND md5chklasttime IS NOT NULL                       \n"
            + "        AND ceil((SYSDATE - md5chklasttime) * 24 * 60) >= ?  \n"
            + "    )                                                          ";

    private static final String updateStatusSql = "                         \n"
            + "UPDATE                                                       \n"
            + "    program                                                  \n"
            + "SET                                                          \n"
            + "    status = ?,                                              \n"
            + "    md5chkcnt = ?,                                           \n"
            + "    md5chklasttime = sysdate                                 \n";
    private static final String updateStatusNoMd5ChkCntSql = "              \n"
            + "UPDATE                                                       \n"
            + "    program                                                  \n"
            + "SET                                                          \n"
            + "    status = ?,                                              \n"
            + "    md5chklasttime = sysdate                                 \n";
    private static final String updateStatusConditionSql = "                \n"
            + "WHERE                                                        \n"
            + "    programid = ?                                            \n";
    private static final String updateSuccessStatusConditionSql = "         \n"
            + "    AND NOT EXISTS (                                         \n"
            + "        SELECT                                               \n"
            + "            1                                                \n"
            + "        FROM                                                 \n"
            + "            mediacontent m,                                  \n"
            + "            programmediacontent pm                           \n"
            + "        WHERE                                                \n"
            + "            (                                                \n"
            + "                 m.status ='" + ProgramStatusType.WAIT_MD5VERIFY + "'\n"
            + "             OR m.status ='" + ProgramStatusType.MD5VERIFY_FAILURE + "'\n"
            + "            )                                                \n"
            + "            AND m.mediacontentid = pm.mediacontentid         \n"
            + "            AND pm.objtype = '1'                             \n"
            + "            AND pm.objid = ?                                 \n"
            + "        )                                                      ";

    private static final String saveByNativeTestSql = "INSERT INTO program (programid,status, md5chkcnt, md5chklasttime) VALUES (?, ?, ?, ?)";

    private static final String deleteByPkTestSql = "DELETE FROM program WHERE programid = ?";

    private static final String queryByPkTestSql = "                       \n"
            + "SELECT                                                       \n"
            + "    programid,                                               \n"
            + "    status,                                                  \n"
            + "    md5chkcnt,                                               \n"
            + "    md5chklasttime                                           \n"
            + "FROM                                                         \n"
            + "    program                                                  \n"
            + "WHERE                                                        \n"
            + "    programid = ?";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ProgramStatus> queryPendingVerifyList(int md5ChkCntThreshold,
            int md5VerifyInterval) {

        return this.sessionFactory.getCurrentSession().doReturningWork((Connection connection) -> {

            final List<ProgramStatus> programStatusList = new LinkedList<>();
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(queryPendingVerifyListSql);
                preparedStatement.setInt(1, md5ChkCntThreshold);
                preparedStatement.setInt(2, md5VerifyInterval);
                ResultSet resultSet = preparedStatement.executeQuery();
                convertResultSet(resultSet, programStatusList);
            } catch (Exception e) {
                logger.error(
                        "queryPendingVerifyList for md5ChkCntThreshold: {} md5VerifyInterval: {} generate exception.",
                        md5ChkCntThreshold, md5VerifyInterval);
                throw e;
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
            return programStatusList;
        });
    }

    @Override
    public int updateStatus(ProgramStatus programStatus) {

        String updateSql = updateStatusSql + updateStatusConditionSql;
        if (ProgramStatusType.INCOMPLETE.equals(programStatus.getStatus())
                || ProgramStatusType.AUDIT_SUCCESS.equals(programStatus.getStatus()))
            updateSql += updateSuccessStatusConditionSql;

        Query query = this.entityManager.createNativeQuery(updateSql);
        query.setParameter(1, programStatus.getStatus());
        query.setParameter(2, programStatus.getMd5chkcnt());
        query.setParameter(3, programStatus.getProgramid());
        if (ProgramStatusType.INCOMPLETE.equals(programStatus.getStatus())
                || ProgramStatusType.AUDIT_SUCCESS.equals(programStatus.getStatus()))
            query.setParameter(4, programStatus.getProgramid());
        int value = query.executeUpdate();
        this.entityManager.flush();
        return value;
    }

    @Override
    public int updateStatus(int programId, Character status) {

        Query query = this.entityManager
                .createNativeQuery(updateStatusNoMd5ChkCntSql + updateStatusConditionSql);
        query.setParameter(1, status);
        query.setParameter(2, programId);
        int value = query.executeUpdate();
        this.entityManager.flush();
        return value;
    }

    @Override
    public int saveByNativeTest(Program program) {

        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork((Connection connection) -> {

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(saveByNativeTestSql);
                preparedStatement.setInt(1, program.getProgramid());
                preparedStatement.setString(2, program.getStatus().toString());
                preparedStatement.setInt(3, program.getMd5chkcnt());
                preparedStatement.setTimestamp(4,
                        new Timestamp(program.getMd5chklasttime().getTime()));
                return preparedStatement.executeUpdate();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        });
    }

    @Override
    public int deleteByPkTest(int programId) {

        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork((Connection connection) -> {

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(deleteByPkTestSql);
                preparedStatement.setInt(1, programId);
                return preparedStatement.executeUpdate();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        });
    }

    @Override
    public int saveByNativeTest2(Program program) {
        Query query = this.entityManager.createNativeQuery(saveByNativeTestSql);
        query.setParameter(1, program.getProgramid());
        query.setParameter(2, program.getStatus().toString());
        query.setParameter(3, program.getMd5chkcnt());
        query.setParameter(4, new Timestamp(program.getMd5chklasttime().getTime()));
        int value = query.executeUpdate();
        this.entityManager.flush();
        return value;
    }

    @Override
    public int deleteByPkTest2(int programId) {

        Query query = this.entityManager.createNativeQuery(deleteByPkTestSql);
        query.setParameter(1, programId);
        int value = query.executeUpdate();
        this.entityManager.flush();
        return value;
    }

    @Override
    public ProgramStatus queryByPkTest2(int programId) {

        Query query = this.entityManager.createNativeQuery(queryByPkTestSql);
        query.setParameter(1, programId);
        Object result = query.getSingleResult();
        Object[] cells = (Object[]) result;
        ProgramStatus programStatus = new ProgramStatus(((BigDecimal) cells[2]).intValue());
        programStatus.setStatus((Character) cells[1]);
        programStatus.setMd5chkcnt(((BigDecimal) cells[2]).intValue());
        return programStatus;
    }

    private final void convertResultSet(ResultSet resultSet, List<ProgramStatus> programStatusList)
            throws SQLException {

        while (resultSet.next()) {
            ProgramStatus programStatus = new ProgramStatus(resultSet.getInt(strProgramId));
            programStatus.setStatus(resultSet.getString(strStatus).toCharArray()[0]);
            programStatus.setMd5chkcnt(resultSet.getInt(strMd5ChkCnt));
            programStatus.setMd5chklasttime(resultSet.getTimestamp(strMd5ChkLastTime));
            programStatusList.add(programStatus);
        }
    }

}

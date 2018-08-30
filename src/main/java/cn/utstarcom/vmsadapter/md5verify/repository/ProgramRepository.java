/**
 * created on 2017年11月21日 上午11:22:32
 */
package cn.utstarcom.vmsadapter.md5verify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.utstarcom.vmsadapter.md5verify.pojo.Program;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public interface ProgramRepository extends JpaRepository<Program, Integer> {

}

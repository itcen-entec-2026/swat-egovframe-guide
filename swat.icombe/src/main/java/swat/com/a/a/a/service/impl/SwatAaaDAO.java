package swat.com.a.a.a.service.impl;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository
public class SwatAaaDAO extends EgovComAbstractDAO {

	public void logInsertWebLogSummary() {
		insert("swat.com.a.a.a.service.impl.SwatAaaDAO.logInsertWebLogSummary");
		delete("swat.com.a.a.a.service.impl.SwatAaaDAO.logDeleteWebLogSummary");
	}

}

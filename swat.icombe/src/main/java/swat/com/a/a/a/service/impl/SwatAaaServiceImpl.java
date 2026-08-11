package swat.com.a.a.a.service.impl;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import swat.com.a.a.a.service.SwatAaaService;

@Service
@RequiredArgsConstructor
@Slf4j
public class SwatAaaServiceImpl extends EgovAbstractServiceImpl implements SwatAaaService {

	private final SwatAaaDAO swatAaaDAO;

	@Override
	public void logInsertWebLogSummary() {
		log.debug("logInsertWebLogSummary");
		swatAaaDAO.logInsertWebLogSummary();
	}

}

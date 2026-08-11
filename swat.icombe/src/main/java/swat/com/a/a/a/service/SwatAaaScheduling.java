package swat.com.a.a.a.service;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SwatAaaScheduling extends EgovAbstractServiceImpl {

	private final SwatAaaService swatAaaService;

	public void webLogSummary() {
		log.debug("webLogSummary");
		swatAaaService.logInsertWebLogSummary();
	}

}

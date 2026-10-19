package hrdb.icombe;

import org.junit.jupiter.api.Test;

import egovframework.com.utl.sim.service.EgovFileScrty;

class LeebhTest {

	@Test
	void test() throws Exception {
		System.out.println(EgovFileScrty.encryptPassword("perf01", "perf01"));
		System.out.println(EgovFileScrty.encryptPassword("perf02", "perf02"));
		System.out.println(EgovFileScrty.encryptPassword("perf03", "perf03"));
		System.out.println(EgovFileScrty.encryptPassword("perf04", "perf04"));
		System.out.println(EgovFileScrty.encryptPassword("perf05", "perf05"));
		System.out.println(EgovFileScrty.encryptPassword("perf06", "perf06"));
		System.out.println(EgovFileScrty.encryptPassword("perf07", "perf07"));
		System.out.println(EgovFileScrty.encryptPassword("perf08", "perf08"));
		System.out.println(EgovFileScrty.encryptPassword("perf09", "perf09"));
		System.out.println(EgovFileScrty.encryptPassword("perf10", "perf10"));

//		for (int i = 1; i <= 10; i++) {
//			System.out.println(i);
//			System.out.println(EgovFileScrty.encryptPassword("perf0" + i, "perf0" + i));
//		}
	}

}

package teammates.testing.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import teammates.testing.BaseTest2;
import teammates.testing.lib.BrowserInstance;
import teammates.testing.lib.BrowserInstancePool;
import teammates.testing.lib.TMAPI;
import teammates.testing.object.Scenario;

public class CoordCourseAddCaseSensitivityTest extends BaseTest2 {
	static BrowserInstance bi;
	static Scenario scn = setupScenarioInstance("scenario");
	
	private static final String COURSE_ID_LOWER = "cs3210";
	private static final String COURSE_ID_UPPER = "CS3210";
	private static final String COURSE_NAME_LOWER = "software engineering";
	private static final String COURSE_NAME_UPPER = "SOFTWARE ENGINEERING";
	
	@BeforeClass
	public static void classSetup() throws Exception {
		System.out.println("========== CoordCourseAddCaseSensitivityTest");
		bi = BrowserInstancePool.request();

		TMAPI.cleanupCourse(scn.course.courseId);
		bi.coordinatorLogin(scn.coordinator.username, scn.coordinator.password);
	}

	@AfterClass
	public static void classTearDown() throws Exception {
		bi.logout();
		TMAPI.cleanupCourse(scn.course.courseId);
		TMAPI.cleanupCourse(COURSE_ID_LOWER);
		TMAPI.cleanupCourse(COURSE_ID_UPPER);
		TMAPI.cleanupCourse("testing01");
		TMAPI.cleanupCourse("testing02");
		
		BrowserInstancePool.release(bi);
		System.out.println("CoordCourseAddCaseSensitivityTest ==========//");
	}

	/**
	 * Test: CaseSensitivityAtCreateCourse
	 * 
	 * */
	@Test
	public void testCaseSensitivityAtCreateCourse() throws Exception {
		System.out.println("testCaseSensitivityAtCreateCourse: courseID - insensitive");
		//-------------------------lower or upper courseID + different names
		bi.clickCourseTab();
		bi.addCourse(COURSE_ID_LOWER, "testing 1st course ID");
		
		bi.verifyAddedCourse(COURSE_ID_LOWER, "testing 1st course ID");
		
		bi.addCourse(COURSE_ID_UPPER, "testing 2nd course ID");
		
//		assertEquals(MESSAGE_COURSE_EXISTS, getElementText(statusMessage));
		assertTrue(bi.isCoursePresent(COURSE_ID_LOWER, "testing 1st course ID"));
//		assertFalse(isCoursePresent(COURSE_ID_UPPER, "testing 2nd course ID"));
		assertTrue(bi.isCoursePresent(COURSE_ID_UPPER, "testing 2nd course ID"));//temp sensitive
		//CLEANUP
		TMAPI.cleanupCourse(COURSE_ID_LOWER);
		TMAPI.cleanupCourse(COURSE_ID_UPPER);
		//-------------------------lower or upper courseID + same name
		bi.clickCourseTab();
		bi.addCourse(COURSE_ID_LOWER, COURSE_NAME_LOWER);
		bi.clickCourseTab();
		bi.verifyAddedCourse(COURSE_ID_LOWER, COURSE_NAME_LOWER);
		
		bi.addCourse(COURSE_ID_UPPER, COURSE_NAME_LOWER);
		
		//TODO: VERIFY
//		assertEquals(MESSAGE_COURSE_EXISTS, getElementText(statusMessage));
		assertTrue(bi.isCoursePresent(COURSE_ID_LOWER, COURSE_NAME_LOWER));
//		assertFalse(isCoursePresent(COURSE_ID_UPPER, COURSE_NAME_LOWER));
		assertTrue(bi.isCoursePresent(COURSE_ID_UPPER, COURSE_NAME_LOWER));//temp sensitive
		//CLEANUP
		TMAPI.cleanupCourse(COURSE_ID_LOWER);
		TMAPI.cleanupCourse(COURSE_ID_UPPER);
		

		System.out.println("testCaseSensitivityAtCreateCourse: course name - sensitive");
		//-------------------------different IDs + upper or lower course name
		bi.clickCourseTab();
		bi.addCourse("testing01", COURSE_NAME_LOWER);
		bi.clickCourseTab();
		bi.verifyAddedCourse("testing01", COURSE_NAME_LOWER);
		bi.addCourse("testing02", COURSE_NAME_UPPER);
		//TODO: VERIFY
		assertEquals(bi.MESSAGE_COURSE_ADDED, bi.getElementText(bi.statusMessage));
		assertTrue(bi.isCoursePresent("testing01", COURSE_NAME_LOWER));
		assertFalse(bi.isCoursePresent("testing02", COURSE_NAME_LOWER));
		TMAPI.cleanupCourse("testing01");
		TMAPI.cleanupCourse("testing02");
	}
}
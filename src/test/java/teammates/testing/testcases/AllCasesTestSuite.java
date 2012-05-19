package teammates.testing.testcases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import teammates.testing.junit.TMAPITest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	AllJsUnitTests.class,
	TMAPITest.class,
	CoordCourseAddPageHtmlTest.class,
	CoordCourseAddPageUiTest.class,
	CoordCourseAddApiTest.class
})

public class AllCasesTestSuite {	
	
}
package teammates.storage.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.Query;

import teammates.common.datatransfer.CourseAttributes;
import teammates.common.datatransfer.EntityAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Utils;
import teammates.storage.entity.Course;

/**
 * Handles CRUD Operations for course entities.
 * The API uses data transfer classes (i.e. *Attributes) instead of presistable classes.
 */
public class CoursesDb extends EntitiesDb {
	
	/* Explanation: Based on our policies for the storage component, this class
	 * does not handle cascading. It treats invalid values as an exception. 
	 * 
	 */

	public static final String ERROR_UPDATE_NON_EXISTENT_COURSE = "Trying to update a Course that doesn't exist: ";
	
	@SuppressWarnings("unused")
	private static final Logger log = Utils.getLogger();

	/**
	 * Preconditions: <br>
	 * * All parameters are non-null. 
	 * @return Null if not found.
	 */
	public CourseAttributes getCourse(String courseId) {
		Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
		
		Course c = getCourseEntity(courseId);

		if (c == null) {
			return null;
		}

		return new CourseAttributes(c);
	}
	
	
	/**
	 * @deprecated Not scalable. Use only in admin features. 
	 */
	@Deprecated
	public List<CourseAttributes> getAllCourses() {
		
		Query q = getPM().newQuery(Course.class);
		
		@SuppressWarnings("unchecked")
		List<Course> courseList = (List<Course>) q.execute();
	
		List<CourseAttributes> courseDataList = new ArrayList<CourseAttributes>();
		for (Course c : courseList) {
			courseDataList.add(new CourseAttributes(c));
		}
	
		return courseDataList;
	}

	/**
	 * Updates the course.<br>
	 * Updates only course archive status.<br>
	 * Does not follow the 'keep existing' policy <br> 
	 * Preconditions: <br>
	 * * {@code courseToUpdate} is non-null. <br>
	 * @throws InvalidParametersException, EntityDoesNotExistException
	 */
	public void updateCourse(CourseAttributes courseToUpdate) throws InvalidParametersException, EntityDoesNotExistException {
		Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseToUpdate);
		
		courseToUpdate.sanitizeForSaving();
		
		if (!courseToUpdate.isValid()) {
			throw new InvalidParametersException(courseToUpdate.getInvalidityInfo());
		}
		
		Course courseEntityToUpdate = getCourseEntity(courseToUpdate.id);
		
		if (courseEntityToUpdate == null) {
			throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT_COURSE);
		}
		
		courseEntityToUpdate.setArchiveStatus(Boolean.valueOf(courseToUpdate.isArchived));
		
		getPM().close();
	}
	

	/**
	 * Note: This is a non-cascade delete.<br>
	 *   <br> Fails silently if there is no such object.
	 * <br> Preconditions: 
	 * <br> * {@code courseId} is not null.
	 */
	public void deleteCourse(String courseId) {
		Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);

		CourseAttributes entityToDelete = new CourseAttributes();
		entityToDelete.id = courseId;
		
		deleteEntity(entityToDelete);

	}
	
	@Override
	protected Object getEntity(EntityAttributes attributes) {
			
		return getCourseEntity( ((CourseAttributes) attributes).id );
	}


	private Course getCourseEntity(String courseId) {
		
		Query q = getPM().newQuery(Course.class);
		q.declareParameters("String courseIdParam");
		q.setFilter("ID == courseIdParam");
		
		@SuppressWarnings("unchecked")
		List<Course> courseList = (List<Course>) q.execute(courseId);
		
		if (courseList.isEmpty() || JDOHelper.isDeleted(courseList.get(0))) {
			return null;
		}
	
		return courseList.get(0);
	}
}

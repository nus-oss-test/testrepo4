package teammates.ui.controller;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;
import teammates.logic.api.GateKeeper;

public class InstructorEvalDeleteAction extends InstructorEvalsPageAction {
	
	@Override
	protected ActionResult execute() throws EntityDoesNotExistException {
		
		String courseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
		String evalName = getRequestParamValue(Const.ParamsNames.EVALUATION_NAME);
		
		new GateKeeper().verifyAccessible(
				logic.getInstructorForGoogleId(courseId, account.googleId), 
				logic.getEvaluation(courseId, evalName));
		
		logic.deleteEvaluation(courseId,evalName);
		statusToUser.add(Const.StatusMessages.EVALUATION_DELETED);
		statusToAdmin = "Evaluation <span class=\"bold\">" + evalName + 
				"</span> in Course <span class=\"bold\"[" + courseId + "]/span> deleted";
		
		return createRedirectResult(Const.ActionURIs.INSTRUCTOR_EVALS_PAGE);
	}
	
}

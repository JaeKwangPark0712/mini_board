package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;

public class JoinHandler implements CommandHandler{
	
	 private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	 private JoinService joinService = new JoinService();

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getMethod().equalsIgnoreCase("GET")) {
			return processForm(request, response);
		} else if(request.getMethod().equalsIgnoreCase("POST")) {
			return procseeSubmit(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
		
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) {
		return FORM_VIEW;
	}

	private String procseeSubmit(HttpServletRequest request, HttpServletResponse response) {
		JoinRequest joinRequest  = new JoinRequest();
		joinRequest.setId(request.getParameter("id"));
		joinRequest.setName(request.getParameter("name"));
		joinRequest.setPassword(request.getParameter("password"));
		joinRequest.setConfirmPassword(request.getParameter("confirmPassword"));
		
		Map<String, Boolean> errors = new HashMap<>();
		request.setAttribute("errors", errors);
		
		joinRequest.validate(errors);
		
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		try {
			joinService.join(joinRequest);
			return "/WEB-INF/view/joinSuccess.jsp";
		} catch(DuplicateIdException e) {
			errors.put("duplicateId", Boolean.TRUE);
			return FORM_VIEW;
		}
		
	}

}

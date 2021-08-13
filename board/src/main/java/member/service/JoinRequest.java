package member.service;

import java.util.Map;

public class JoinRequest {// 회원가입 기능 구현에 필요한 정보를 담는 자바빈 클래스
	
	// 멤버 필드
	private String id;
	private String name;
	private String password;
	private String confirmPassword;
	
	// Getter, Setter 메서드
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	// 비밀번호와 비밀번호 확인에 입력한 값이 일치하는지 확인하는 메서드
	public boolean isPasswordEqualToConfirm() {
		return password != null && password.equals(confirmPassword);
	}
	
	// 회원 가입 창 form에서 비어있는 부분이 있는지 확인하는 메서드
	public void validate(Map<String, Boolean> errors) {
		checkEmpty(errors, id, "id");
		checkEmpty(errors, name, "name");
		checkEmpty(errors, password, "password");
		checkEmpty(errors, confirmPassword, "confirmPassword");
		// 비밀번호와 비밀번호 확인이 일치하지 않을 경우, errors 맵에 "notMatch" 라는 키와 값을 추가한다!
		if(!errors.containsKey(confirmPassword)) {
			if(!isPasswordEqualToConfirm()) {
				errors.put("notMatch", Boolean.TRUE);
			}
		}
	}
	
	private void checkEmpty(Map<String, Boolean> errors, String value, String fieldName) {
		if(value == null || value.isEmpty()) {
			errors.put(fieldName, Boolean.TRUE);
		}
		
	}
}

package com.cafe24.ypshop.backend.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cafe24.ypshop.backend.validator.constraints.ValidName;

public class NameValidator implements ConstraintValidator<ValidName, String> {

	//compile의 인자인 3개 단어만 허용하겠다는 선언
	private Pattern pattern = Pattern.compile("PARK|JIN|YOUNG");
	
	@Override
	public void initialize(ValidName arg0) {
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.length()==0 || value.contentEquals("")) {
			return false;
		}
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
}

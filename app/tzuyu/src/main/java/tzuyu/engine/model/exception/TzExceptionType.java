/*
 * Copyright (C) 2013 by SUTD (Singapore)
 * All rights reserved.
 *
 * 	Author: SUTD
 *  Version:  $Revision: 1 $
 */

package tzuyu.engine.model.exception;

import lstar.LStarException.Type;

/**
 * @author LLT
 *
 */
public enum TzExceptionType {
	PARAMETER_SELECTOR_FAIL_INIT_CLASS,
	//need file name.
	JUNIT_FAIL_WRITE_FILE,  
	INTERRUPT,
	ALPHABET_EMPTY,
	RETHROW;

	public static TzExceptionType fromLStar(Type type) {
		switch (type) {
		case AlphabetEmptyAction:
			return ALPHABET_EMPTY;
		}
		return null;
	}
}

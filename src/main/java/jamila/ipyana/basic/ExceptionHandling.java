package jamila.ipyana.basic;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value= {IllegalArgumentException.class})
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest req) {
		MessageResponseError err = new MessageResponseError("BAD ARGUMENT", ex.getMessage());
		return handleExceptionInternal(ex, err, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
	}
	@ExceptionHandler(value= {Exception.class})
	protected ResponseEntity<Object> handleIllegalArgumentException(EntityNotFoundException ex, WebRequest req) {
		MessageResponseError err = new MessageResponseError("NOT FOUND", ex.getMessage());
		return handleExceptionInternal(ex, err, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
	}
	
	@ExceptionHandler(value= {AuthenticationException.class})
	protected ResponseEntity<Object> handleIllegalArgumentException(AuthenticationException ex, WebRequest req) {
		MessageResponseError err = new MessageResponseError("UNAUTHORIZED", ex.getMessage());
		return handleExceptionInternal(ex, err, new HttpHeaders(), HttpStatus.UNAUTHORIZED, req);
	}
	
	
	
	public static class MessageResponseError {
		private String reasonCode;
		
		private String message;
		
		public MessageResponseError() {}
		
		public MessageResponseError(String reason, String msg) {
			this.setReasonCode(reason);
			this.setMessage(msg);
		}

		public String getReasonCode() {
			return reasonCode;
		}

		public void setReasonCode(String reasonCode) {
			this.reasonCode = reasonCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}

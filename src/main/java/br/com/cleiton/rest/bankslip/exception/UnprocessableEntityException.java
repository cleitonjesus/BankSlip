package br.com.cleiton.rest.bankslip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excesão para retornar o código que não foi possível processar a entidade.
 *
 * @author Cleiton de Jesus Hartje
 */
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Unprocessable entity")
public class UnprocessableEntityException extends RuntimeException {
  public UnprocessableEntityException() {
  }

  public UnprocessableEntityException(String message) {
    super(message);
  }

  public UnprocessableEntityException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnprocessableEntityException(Throwable cause) {
    super(cause);
  }

  public UnprocessableEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

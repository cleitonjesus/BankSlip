package br.com.cleiton.rest.bankslip.entity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BankSlipDetails extends BankSlipEntity {
  private BigDecimal fine;

  public BankSlipDetails(@NotNull String id, @NotNull LocalDate due_date, @NotNull BigDecimal total_in_cents, @NotNull String customer, @NotNull BankSlipStatus status) {
    super(due_date, total_in_cents, customer, status);
    setId(id);
  }

  public BigDecimal getFine() {
    return fine;
  }

  public void setFine(BigDecimal fine) {
    this.fine = fine;
  }
}

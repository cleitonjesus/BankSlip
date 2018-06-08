package br.com.cleiton.rest.bankslip.entity;

import javax.validation.constraints.NotNull;

/**
 * Entidade usada somente para editar o status do boleto
 *
 * @author Cleiton de Jesus Hartje
 */
public class BankSlipStatusEdit {

  @NotNull
  private BankSlipStatus status;

  public BankSlipStatusEdit() {

  }

  public BankSlipStatus getStatus() {
    return status;
  }

  public void setStatus(BankSlipStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "BankSlipStatusEdit{" +
        ", status='" + status + '\'' +
        '}';
  }
}

package br.com.cleiton.rest.bankslip.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Classe entidade com as informações do boleto para sua criação.
 *
 * @author Cleiton de Jesus Hartje
 */
public class BankSlipCreate {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate due_date;
  @NotNull
  @NumberFormat(pattern = "##########.##")
  @DecimalMin("0")
  private BigDecimal total_in_cents;
  @NotNull
  private String customer;
  @NotNull
  private BankSlipStatus status;

  public BankSlipCreate() {

  }

  public BankSlipCreate(@NotNull LocalDate due_date, @NotNull BigDecimal total_in_cents, @NotNull String customer, @NotNull BankSlipStatus status) {
    this.due_date = due_date;
    this.total_in_cents = total_in_cents;
    this.customer = customer;
    this.status = status;
  }

  public LocalDate getDue_date() {
    return due_date;
  }

  public void setDue_date(LocalDate due_date) {
    this.due_date = due_date;
  }

  public BigDecimal getTotal_in_cents() {
    return total_in_cents;
  }

  public void setTotal_in_cents(BigDecimal total_in_cents) {
    this.total_in_cents = total_in_cents;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public BankSlipStatus getStatus() {
    return status;
  }

  public void setStatus(BankSlipStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "BankSlipEntity{" +
        "due_date=" + due_date +
        ", total_in_cents=" + total_in_cents +
        ", customer='" + customer + '\'' +
        ", status='" + status + '\'' +
        '}';
  }
}

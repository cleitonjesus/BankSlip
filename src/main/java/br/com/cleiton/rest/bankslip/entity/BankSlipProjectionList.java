package br.com.cleiton.rest.bankslip.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDate;

@Projection(name = "bankSlipProjectionList", types = { BankSlipEntity.class })
public interface BankSlipProjectionList {

  @Value("#{target.id}")
  String getId();

  @Value("#{target.due_date}")
  LocalDate getDue_date();

  @Value("#{target.total_in_cents}")
  BigDecimal getTotal_in_cents();

  @Value("#{target.customer}")
  String getCustomer();
}

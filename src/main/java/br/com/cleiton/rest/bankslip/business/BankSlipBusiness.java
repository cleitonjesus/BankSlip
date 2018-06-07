package br.com.cleiton.rest.bankslip.business;

import br.com.cleiton.rest.bankslip.entity.BankSlipDetails;

public interface BankSlipBusiness {
  BankSlipDetails findByIdDetails(String id);
}

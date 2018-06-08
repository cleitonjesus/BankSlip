package br.com.cleiton.rest.bankslip.business;

import br.com.cleiton.rest.bankslip.entity.BankSlipDetails;

/**
 * Interface com a regra de negócio da aplicação
 *
 * @author Cleiton de Jesus Hartje
 */
public interface BankSlipBusiness {
  BankSlipDetails findByIdDetails(String id);
}

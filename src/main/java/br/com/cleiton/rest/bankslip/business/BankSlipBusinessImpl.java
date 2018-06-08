package br.com.cleiton.rest.bankslip.business;

import br.com.cleiton.rest.bankslip.entity.BankSlipDetails;
import br.com.cleiton.rest.bankslip.entity.BankSlipEntity;
import br.com.cleiton.rest.bankslip.exception.NotFoundException;
import br.com.cleiton.rest.bankslip.repository.BankSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Implementação das regra de negócio da aplicação
 *
 * @author Cleiton de Jesus Hartje
 */
@Component
@Transactional(readOnly = true)
public class BankSlipBusinessImpl implements BankSlipBusiness {

  final BankSlipRepository bankSlipRepository;

  @Autowired
  public BankSlipBusinessImpl(BankSlipRepository bankSlipRepository) {
    this.bankSlipRepository = bankSlipRepository;
  }

  /**
   * Busca o boleto calculando a multa se tiver.
   * <br/>
   * 0,5% - menos de 10 dias
   * <br/>
   * 1% - mais de 10 dias
   *
   * @param id - id da boleto
   * @return Detalhes do boleto
   */
  @Override
  public BankSlipDetails findByIdDetails(String id) {
    BankSlipEntity result = bankSlipRepository.findById(id).orElseThrow(NotFoundException::new);

    BankSlipDetails bankSlipEntity = new BankSlipDetails(result.getId(), result.getDue_date(),
      result.getTotal_in_cents(), result.getCustomer(), result.getStatus());

    if (bankSlipEntity.getDue_date().isBefore(LocalDate.now())) {
      BigDecimal interestRate = new BigDecimal("0.005");
      interestRate = interestRate.setScale(15, BigDecimal.ROUND_UP);
      BigDecimal fineDays = new BigDecimal(ChronoUnit.DAYS.between(bankSlipEntity.getDue_date(), LocalDate.now()));
      if (fineDays.longValue() > 10) {
        interestRate = new BigDecimal("0.01");
        interestRate = interestRate.setScale(15, BigDecimal.ROUND_UP);
      }
      BigDecimal fine = interestRate
        .multiply(bankSlipEntity.getTotal_in_cents())
        .multiply(fineDays);
      fine = fine.setScale(2, BigDecimal.ROUND_UP);
      bankSlipEntity.setFine(fine);
    }
    return bankSlipEntity;
  }

}

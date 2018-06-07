package br.com.cleiton.rest.bankslip.repository;

import br.com.cleiton.rest.bankslip.entity.BankSlipEntity;
import br.com.cleiton.rest.bankslip.entity.BankSlipProjectionList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface BankSlipRepository extends CrudRepository<BankSlipEntity, String> {

  Collection<BankSlipProjectionList> findAllProjectedBy();

}

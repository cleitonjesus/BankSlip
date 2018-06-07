package br.com.cleiton.rest.bankslip.controller;

import br.com.cleiton.rest.bankslip.business.BankSlipBusiness;
import br.com.cleiton.rest.bankslip.entity.*;
import br.com.cleiton.rest.bankslip.exception.InvalidParameterException;
import br.com.cleiton.rest.bankslip.exception.NotFoundException;
import br.com.cleiton.rest.bankslip.repository.BankSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/**
 * Controller rest para geração de boletos
 *
 * @author Cleiton de Jesus Hartje
 */
@RestController
@RequestMapping("/rest")
public class BankSlipRestController {

  @Autowired
  BankSlipRepository bankSlipRepository;

  @Autowired
  BankSlipBusiness bankSlipBusiness;

  @PostMapping("/bankslips")
  public ResponseEntity<BankSlipEntity> createBankslip(@RequestBody BankSlipCreate bankSlipEntity) {
    BankSlipEntity result;
    try {
      result = bankSlipRepository.save(
        new BankSlipEntity(bankSlipEntity.getDue_date(), bankSlipEntity.getTotal_in_cents(),
          bankSlipEntity.getCustomer(), bankSlipEntity.getStatus()));
    }
    catch (Exception E) {
      throw new InvalidParameterException(E);
    }
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{id}")
        .buildAndExpand(result.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  @GetMapping("/bankslips")
  public ResponseEntity<Collection<BankSlipProjectionList>> listBankslip() {
    Collection<BankSlipProjectionList> listResult = bankSlipRepository.findAllProjectedBy();
    return ResponseEntity.ok(listResult);
  }

  @GetMapping("/bankslips/{id}")
  public ResponseEntity<BankSlipDetails> detailsBankslip(@Validated @PathVariable String id) {
    return ResponseEntity.ok(bankSlipBusiness.findByIdDetails(id));
  }

  @PutMapping("/bankslips/{id}")
  public ResponseEntity<String> editStatus(@Validated @RequestBody BankSlipStatusEdit status, @PathVariable String id) {
    Optional<BankSlipEntity> optionalBankSlipEntity = bankSlipRepository.findById(id);
    BankSlipEntity result = optionalBankSlipEntity.orElseThrow(NotFoundException::new);
    result.setStatus(status.getStatus());
    bankSlipRepository.save(result);
    return ResponseEntity.ok("");
  }

  public BankSlipRepository getBankSlipRepository() {
    return bankSlipRepository;
  }

  public void setBankSlipRepository(BankSlipRepository bankSlipRepository) {
    this.bankSlipRepository = bankSlipRepository;
  }
}

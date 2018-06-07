package br.com.cleiton.rest.bankslip.business;

import br.com.cleiton.rest.bankslip.entity.BankSlipEntity;
import br.com.cleiton.rest.bankslip.entity.BankSlipStatus;
import br.com.cleiton.rest.bankslip.repository.BankSlipRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


public class BankSlipBusinessTest {

  BankSlipBusiness bankSlipBusiness;

  @Mock
  BankSlipRepository bankSlipRepository;

  BankSlipEntity trillian, itau;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    if (bankSlipBusiness == null)
      bankSlipBusiness = new BankSlipBusinessImpl(bankSlipRepository);

    this.trillian =
      new BankSlipEntity(LocalDate.now().minusDays(10),
        new BigDecimal("100000"),
        "Trillian Company",
        BankSlipStatus.PENDING);
    this.trillian.setId("1");
    this.itau =
      new BankSlipEntity(LocalDate.now().minusDays(20),
        new BigDecimal("200000"),
        "Itau S.A.",
        BankSlipStatus.PENDING);
    this.itau.setId("2");
    Mockito.doReturn(Optional.of(this.trillian)).when(bankSlipRepository).findById(this.trillian.getId());
    Mockito.doReturn(Optional.of(this.itau)).when(bankSlipRepository).findById(this.itau.getId());
  }

  @Test
  public void findByIdDetailsBefore10days() {
    assertThat(bankSlipBusiness.findByIdDetails(trillian.getId()))
      .satisfies(it -> assertTrue(it.getFine().compareTo(new BigDecimal("5000")) == 0));
  }

  @Test
  public void findByIdDetailsAfter10days() {
    assertThat(bankSlipBusiness.findByIdDetails(itau.getId()))
      .satisfies(it -> assertTrue(it.getFine().compareTo(new BigDecimal("40000")) == 0));
  }
}

package br.com.cleiton.rest.bankslip.controller;

import br.com.cleiton.rest.Application;
import br.com.cleiton.rest.bankslip.entity.BankSlipEntity;
import br.com.cleiton.rest.bankslip.entity.BankSlipStatus;
import br.com.cleiton.rest.bankslip.repository.BankSlipRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class BankSlipControllerTest {
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
    MediaType.APPLICATION_JSON.getSubtype(),
    Charset.forName("utf8"));

  private MockMvc mockMvc;

  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  BankSlipRepository bankSlipRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  BankSlipEntity santander, itau;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
      .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
      .findAny()
      .orElse(null);

    assertNotNull("the JSON message converter must not be null",
      this.mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setUp() {
    this.bankSlipRepository.deleteAll();

    this.santander = bankSlipRepository.save(
      new BankSlipEntity(LocalDate.now().minusDays(10),
        new BigDecimal("100000.0"),
        "Santander S.A.",
        BankSlipStatus.PENDING));
    this.itau = bankSlipRepository.save(
      new BankSlipEntity(LocalDate.now().minusDays(20),
        new BigDecimal("200000.0"),
        "Itau S.A.",
        BankSlipStatus.PENDING));

    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

   @Test
   public void createBankSlip() throws Exception {
     String bankSlipJson = json(new BankSlipEntity(LocalDate.of(2018, 1, 1),
       new BigDecimal("100000"),
       "Trillian Company",
       BankSlipStatus.PENDING));

     this.mockMvc.perform(post("/rest/bankslips")
       .contentType(contentType)
       .content(bankSlipJson))
       .andExpect(status().isCreated());
   }

  @Test
  public void createInvalidTotal_in_cents_BankSlip() throws Exception {
    String bankSlipJson = "{" +
      "\"due_date\" : \"2018-01-01\" ,\n" +
      "\"total_in_cents\" : \"a\" ,\n" +
      "\"customer\" : \"Trillian Company\" ,\n" +
      "\"status\" : \"PENDING\"\n" +
      "}";

    this.mockMvc.perform(post("/rest/bankslips")
      .contentType(contentType)
      .content(bankSlipJson))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void createInvalidDue_dateBankSlip() throws Exception {
    String bankSlipJson = "{" +
      "\"due_date\" : \"2018-30-01\" ,\n" +
      "\"total_in_cents\" : \"100000\" ,\n" +
      "\"customer\" : \"Trillian Company\" ,\n" +
      "\"status\" : \"PENDING\"\n" +
      "}";

    this.mockMvc.perform(post("/rest/bankslips")
      .contentType(contentType)
      .content(bankSlipJson))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void createCustomerRequireBankSlip() throws Exception {
    String bankSlipJson = "{" +
      "\"due_date\" : \"2018-01-01\" ,\n" +
      "\"total_in_cents\" : \"100000\" ,\n" +
      "\"customer\" : \"\" ,\n" +
      "\"status\" : \"PENDING\"\n" +
      "}";

    this.mockMvc.perform(post("/rest/bankslips")
      .contentType(contentType)
      .content(bankSlipJson))
      .andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void createTotal_in_centsZeroBankSlip() throws Exception {
    String bankSlipJson = "{" +
      "\"due_date\" : \"2018-01-01\" ,\n" +
      "\"total_in_cents\" : \"0\" ,\n" +
      "\"customer\" : \"\" ,\n" +
      "\"status\" : \"PENDING\"\n" +
      "}";

    this.mockMvc.perform(post("/rest/bankslips")
      .contentType(contentType)
      .content(bankSlipJson))
      .andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void createInvalidStatusBankSlip() throws Exception {
    String bankSlipJson = "{" +
      "\"due_date\" : \"2018-01-01\" ,\n" +
      "\"total_in_cents\" : \"100000\" ,\n" +
      "\"customer\" : \"Trillian Company\" ,\n" +
      "\"status\" : \"Invalid\"\n" +
      "}";

    this.mockMvc.perform(post("/rest/bankslips")
      .contentType(contentType)
      .content(bankSlipJson))
      .andExpect(status().isBadRequest());
  }

   @Test
   public void listBankSlip() throws Exception {
     mockMvc.perform(get("/rest/bankslips"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(contentType))
       .andExpect(jsonPath("$[0].id", is(this.santander.getId())))
       .andExpect(jsonPath("$[0].customer", is(this.santander.getCustomer())))
       .andExpect(jsonPath("$[0].due_date", is(this.santander.getDue_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
       .andExpect(jsonPath("$[0].total_in_cents").value(this.santander.getTotal_in_cents()));
   }

  @Test
  public void detailsBankslipBefore10Days() throws Exception {
    mockMvc.perform(get("/rest/bankslips/" + this.santander.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(contentType))
      .andExpect(jsonPath("$.id", is(this.santander.getId())))
      .andExpect(jsonPath("$.customer", is(this.santander.getCustomer())))
      .andExpect(jsonPath("$.due_date", is(this.santander.getDue_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
      .andExpect(jsonPath("$.total_in_cents").value(this.santander.getTotal_in_cents()))
      .andExpect(jsonPath("$.fine").value("5000.0"));
  }

  @Test
  public void detailsBankslipAfter10Days() throws Exception {
    mockMvc.perform(get("/rest/bankslips/" + this.itau.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(contentType))
      .andExpect(jsonPath("$.id", is(this.itau.getId())))
      .andExpect(jsonPath("$.customer", is(this.itau.getCustomer())))
      .andExpect(jsonPath("$.due_date", is(this.itau.getDue_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
      .andExpect(jsonPath("$.total_in_cents").value(this.itau.getTotal_in_cents()))
      .andExpect(jsonPath("$.status").value(this.itau.getStatus().name()))
      .andExpect(jsonPath("$.fine").value("40000.0"));
  }

  @Test
  public void detailsBankslipNotFound() throws Exception {
    mockMvc.perform(get("/rest/bankslips/1"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void payBankslip() throws Exception {
    this.mockMvc.perform(put("/rest/bankslips/" + this.itau.getId())
      .contentType(contentType)
      .content("{ \"status\": \"" + BankSlipStatus.PAID.name() + "\" }"))
      .andExpect(status().isOk());
    Optional<BankSlipEntity> optionalBankSlipEntity = bankSlipRepository.findById(this.itau.getId());
    assertTrue(optionalBankSlipEntity.isPresent());
    BankSlipEntity itauEdit = optionalBankSlipEntity.get();
    Assert.assertEquals(BankSlipStatus.PAID, itauEdit.getStatus());
  }

  @Test
  public void cancelBankslip() throws Exception {
    this.mockMvc.perform(put("/rest/bankslips/" + this.itau.getId())
      .contentType(contentType)
      .content("{ \"status\": \"" + BankSlipStatus.CANCELED.name() + "\" }"))
      .andExpect(status().isOk());
    Optional<BankSlipEntity> optionalBankSlipEntity = bankSlipRepository.findById(this.itau.getId());
    assertTrue(optionalBankSlipEntity.isPresent());
    BankSlipEntity itauEdit = optionalBankSlipEntity.get();
    Assert.assertEquals(BankSlipStatus.CANCELED, itauEdit.getStatus());
  }

  @Test
  public void invalidStatusEditBankslip() throws Exception {
    this.mockMvc.perform(put("/rest/bankslips/" + this.itau.getId())
      .contentType(contentType)
      .content("{ \"status\": \"Invalid\" }"))
      .andExpect(status().isBadRequest());
  }

  protected <T> String json(T o) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    this.mappingJackson2HttpMessageConverter.write(
      o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }

}

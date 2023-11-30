package ru.netology.Test;
import com.codeborne.selenide.Condition;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ru.netology.Page.BuyPage;
import ru.netology.Page.MainPage;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.cleanDatabase;


public class TicketBuyerTest {
   MainPage mainPage;
   BuyPage buyPage;


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        mainPage=new MainPage();
        buyPage= mainPage.goToBuyPage();
    }

    @AfterEach
     void TearDownAll(){
        cleanDatabase();
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @Test
    @DisplayName("Test happy path  form Buy")
    public void shouldTestTheHappyPath() {
    buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
    buyPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Test happy path with latin Owner for form Buy")
    public void shouldTestTheHappyPathWithLatin() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Test negative value for card number form Buy")
    public void shouldTestNegativeForCardNumber() {
        buyPage.putData(DataHelper.getSecondCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.errorNotificationWait();
        Assertions.assertEquals("DECLINED", SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Card Number is null")
    public void shouldTestCardNumberNull() {
        buyPage.putData("",DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.wrongCardNumberNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Card Number is incorrect")
    public void shouldTestIncorrectCardNumber() {
        buyPage.putData(DataHelper.generateRandomCardNumber(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.errorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is 13")
    public void shouldTestMonthNumber13() {
        buyPage.putData(DataHelper.getFirstCardInfo(),"13",DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is 00")
    public void shouldTestMonthNumber00() {
        buyPage.putData(DataHelper.getFirstCardInfo(),"00",DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is not a two digit number")
    public void shouldTestMonthWithNotTwoDigitNumber() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.generateMonth(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.wrongMonthNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value should be null")
    public void shouldTestMonthValueNull() {
        buyPage.putData(DataHelper.getFirstCardInfo(),"",DataHelper.generateYear("yy"),DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.wrongMonthNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Test empty form")
    public void shouldTestEmptyForm() {
        buyPage.putData("","","","","");
        buyPage.wrongCardNumberNotificationWait();
        buyPage.wrongMonthNotificationWait();
        buyPage.wrongYearNotificationWait();
        buyPage.ownerEmptyNotificationWait();
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value should be past")
    public void shouldTestPastMonth() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.lastMonth("MM"),DataHelper.thisYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value should be past")
    public void shouldTestPastYear() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.lastYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.expiredCardNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is empty")
    public void shouldTestYearIsEmpty() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),"",DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.wrongYearNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is more than five years form now")
    public void shouldTestYearFutureMoreThanFiveYears() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.futureMoreThanFiveYears("yy"),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is not a two digit number")
    public void shouldTestYearWithOneDigitNumber() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.randomYear(),DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.wrongYearNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("CVC value is one digit number")
    public void shouldTestCvcAsOneDigitNumber() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcNumber());
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("CVC value is two digit number")
    public void shouldTestCvcAsTwoDigitNumber() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfo(),DataHelper.cvcDoubleNumber());
        buyPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with numbers at the end")
    public void shouldTestOwnerWithNumbers() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfo() + "23",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with numbers in the beginning")
    public void shouldTestOwnerWithNumbersAtTheBeginning() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"23" + DataHelper.ownerInfo(),DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is digital")
    public void shouldTestOwnerValueAsDigital() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.generateOwnerInfo(),DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is empty")
    public void shouldTestOwnerAsEmpty() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"",DataHelper.cvcInfo());
        buyPage.ownerEmptyNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with dot at the end")
    public void shouldTestOwnerWithDotAtTheEnd() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng() + ".",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with dot at the beginning")
    public void shouldTestOwnerWithDotAtTheBeginning() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"." + DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is a dot")
    public void shouldTestOwnerAsADot() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),".",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with hyphen at the end")
    public void shouldTestOwnerWithHyphenAtTheEnd() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng() + "-",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with hyphen at the beginning")
    public void shouldTestOwnerWithHyphenAtTheBeginning() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"-" + DataHelper.ownerInfoEng() ,DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value contains 2 letters")
    public void shouldTestOwnerWithTwoLetters() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"S H",DataHelper.cvcInfo());
        buyPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value contains letter dot and surname")
    public void shouldTestOwnerWithCutedNameWithDot() {
        buyPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"R.Vaneskegyan",DataHelper.cvcInfo());
        buyPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.geStatusInData());
    }
}
















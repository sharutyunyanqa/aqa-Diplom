package ru.netology.Test;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;


import ru.netology.Page.CreditPage;
import ru.netology.Page.MainPage;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class TicketBuyByCreditTest {

    MainPage mainPage;
    CreditPage creditPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        mainPage = new MainPage();
        creditPage = mainPage.goToCreditPage();
    }

    @AfterEach
    void TearDownAll() {
        cleanDatabase();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Test happy path for form Buy by credit")
    public void shouldTestTheHappyPathForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Test happy path with latin Owner for form Credit")
    public void shouldTestLatinOwner() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Test negative value for card number form Buy by credit")
    public void shouldTestNegativeForCardNumberForSecondForm() {
        creditPage.putData(DataHelper.getSecondCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.errorNotificationWait();
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Card Number is null")
    public void shouldTestCardNumberNullForSecondForm() {
        creditPage.putData("", DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.wrongCardNumberNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Card Number is incorrect")
    public void shouldTestIncorrectCardNumberForSecondForm() {
        creditPage.putData(DataHelper.generateRandomCardNumber(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.errorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is 13")
    public void shouldTestMonthNumber13ForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), "13", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is 00")
    public void shouldTestMonthNumber00ForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), "00", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is not a two digit number")
    public void shouldTestMonthWithNotTwoDigitNumberForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.generateMonth(), DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.wrongMonthNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value should be null")
    public void shouldTestMonthValueNullForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), "", DataHelper.generateYear("yy"), DataHelper.ownerInfoEng(), DataHelper.cvcInfo());
        creditPage.wrongMonthNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Test empty form")
    public void shouldTestEmptyFormForSecondForm() {
        creditPage.putData("", "", "", "", "");
        creditPage.wrongCardNumberNotificationWait();
        creditPage.wrongMonthNotificationWait();
        creditPage.wrongYearNotificationWait();
        creditPage.ownerEmptyNotificationWait();
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value should be past")
    public void shouldTestPastMonthForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.lastMonth("MM"), DataHelper.thisYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value should be past")
    public void shouldTestPastYearForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.lastYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.expiredCardNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is empty")
    public void shouldTestYearIsEmptyForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), "", DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.wrongYearNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is more than five years form now")
    public void shouldTestYearFutureMoreThanFiveYearsForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.futureMoreThanFiveYears("yy"), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.validityErrorNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is not a two digit number")
    public void shouldTestYearWithOneDigitNumberForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.randomYear(), DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.wrongYearNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("CVC value is one digit number")
    public void shouldTestCvcAsOneDigitNumberForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcNumber());
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("CVC value is two digit number")
    public void shouldTestCvcAsTwoDigitNumberForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo(), DataHelper.cvcDoubleNumber());
        creditPage.wrongFormatCVVNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with numbers at the end")
    public void shouldTestOwnerWithNumbersForSecondForm() {

        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), DataHelper.ownerInfo() + "23", DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with numbers in the beginning")
    public void shouldTestOwnerWithNumbersAtTheBeginningForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(), DataHelper.month(), DataHelper.generateYear("yy"), "23" + DataHelper.ownerInfo(), DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is digital")
    public void shouldTestOwnerValueAsDigitalForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.generateOwnerInfo(),DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is empty")
    public void shouldTestOwnerAsEmptyForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"",DataHelper.cvcInfo());
        creditPage.ownerEmptyNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with dot at the end")
    public void shouldTestOwnerWithDotAtTheEndForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng() + ".",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with dot at the beginning")
    public void shouldTestOwnerWithDotAtTheBeginningForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"." + DataHelper.ownerInfoEng(),DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is a dot")
    public void shouldTestOwnerAsADotForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),".",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with hyphen at the end")
    public void shouldTestOwnerWithHyphenAtTheEndForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),DataHelper.ownerInfoEng() + "-",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with hyphen at the beginning")
    public void shouldTestOwnerWithHyphenAtTheBeginningForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"-" + DataHelper.ownerInfoEng() ,DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value contains 2 letters")
    public void shouldTestOwnerWithTwoLettersForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"S H",DataHelper.cvcInfo());
        creditPage.incorrectFormatOwnerNotificationWait();
        Assertions.assertNull(SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value contains letter dot and surname")
    public void shouldTestOwnerWithCutedNameWithDotForSecondForm() {
        creditPage.putData(DataHelper.getFirstCardInfo(),DataHelper.month(),DataHelper.generateYear("yy"),"R.Vaneskegyan",DataHelper.cvcInfo());
        creditPage.successNotificationWait();
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());
    }
}




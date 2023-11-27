import com.codeborne.selenide.Condition;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class TicketBuyByCreditTest {

    DataHelper.CardInfo firstCardInfo;
    DataHelper.SecondCardInfo secondCardInfo;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        $(byText("Купить в кредит")).click();
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();


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
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification__title")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Успешно"));
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Test negative value for card number form Buy by credit")
    public void shouldTestNegativeForCardNumberForSecondForm() {
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getSecondCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(" .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals("DECLINED", SQLHelper.getStatusForCreditForm());


    }

    @Test
    @DisplayName("Card Number is null")
    public void shouldTestCardNumberNullForSecondForm() {
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }
    @Test
    @DisplayName("Test happy path with latin Owner for Credit")
    public void shouldTestTheHappyPathWithLatinCredit() {
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfoEng());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification__title")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Успешно"));
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Card Number is incorrect")
    public void shouldTestIncorrectCardNumberForSecondForm() {

        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.generateRandomCardNumber().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification_status_error .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Month value is 13")
    public void shouldTestMonthNumber13ForSecondForm() {

        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        $("input[placeholder='" + "08" + "']").setValue("13");
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value is 00")
    public void shouldTestMonthNumber00ForSecondForm() {

        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        $("input[placeholder='" + "08" + "']").setValue("00");
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Month value is not a two digit number")
    public void shouldTestMonthWithNotTwoDigitNumberForSecondForm() {

        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        int monthRandom = generateMonth();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(monthRandom));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Month value should be null")
    public void shouldTestMonthValueNullForSecondForm() {

        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Test empty form")
    public void shouldTestEmptyFormForSecondForm() {

        $$("button span.button__text").find(exactText("Продолжить")).click();
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
        $("input[placeholder='" + "08" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
        $("input[placeholder='" + "22" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
        $$(".input__control").get(3).parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
        $("input[placeholder='" + "999" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
    }

    @Test
    @DisplayName("Month value should be past")
    public void shouldTestPastMonthForSecondForm() {
//        
        $(By.className("button")).click();
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String monthPast = lastMonth("MM");
        $("input[placeholder='" + "08" + "']").setValue(monthPast);
        String yearRandom = thisYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(yearRandom));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Year value should be past")
    public void shouldTestPastYearForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String yearLast = lastYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(yearLast));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Истёк срок действия карты"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is empty")
    public void shouldTestYearIsEmptyForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is more than five years form now")
    public void shouldTestYearFutureMoreThanFiveYearsForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String futureYears = futureMoreThanFiveYears("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(futureYears));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверно указан срок действия карты"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Year value is not a two digit number")
    public void shouldTestYearWithOneDigitNumberForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = randomYear();
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("CVC value is one digit number")
    public void shouldTestCvcAsOneDigitNumberForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int CvcNumber = cvcNumber();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(CvcNumber));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("CVC value is two digit number")
    public void shouldTestCvcAsTwoDigitNumberForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        String cvcDoubleNumber = cvcDoubleNumber();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(cvcDoubleNumber));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with numbers at the end")
    public void shouldTestOwnerWithNumbersForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        int generateOwnerInfo = generateOwnerInfo();
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo() + generateOwnerInfo);
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Owner value is with numbers in the beginning")
    public void shouldTestOwnerWithNumbersAtTheBeginningForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        int generateOwnerInfo = generateOwnerInfo();
        $$(".input__control").get(3).setValue(generateOwnerInfo + DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());


    }

    @Test
    @DisplayName("Owner value is digital")
    public void shouldTestOwnerValueAsDigitalForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        int generateOwnerInfo = generateOwnerInfo();
        $$(".input__control").get(3).setValue(String.valueOf(generateOwnerInfo));
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Owner value is empty")
    public void shouldTestOwnerAsEmptyForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with dot at the end")
    public void shouldTestOwnerWithDotAtTheEndForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo() + ".");
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());
    }

    @Test
    @DisplayName("Owner value is with dot at the beginning")
    public void shouldTestOwnerWithDotAtTheBeginningForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue("." + DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Owner value is a dot")
    public void shouldTestOwnerAsADotForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(".");
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());


    }

    @Test
    @DisplayName("Owner value is with hyphen at the end")
    public void shouldTestOwnerWithHyphenAtTheEndForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo() + "-");
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Owner value is with hyphen at the beginning")
    public void shouldTestOwnerWithHyphenAtTheBeginningForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue("-" + DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Owner value contains 2 letters")
    public void shouldTestOwnerWithTwoLettersForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue("S H");
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".input__sub")
                .shouldBe(visible)
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.getStatusForCreditForm());

    }

    @Test
    @DisplayName("Owner value contains letter dot and surname")
    public void shouldTestOwnerWithCutedNameWithDotForSecondForm() {
//        
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue("R.Vaneskehyan");
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $(".notification__title")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Успешно"));
        Assertions.assertEquals("APPROVED", SQLHelper.getStatusForCreditForm());

    }
}




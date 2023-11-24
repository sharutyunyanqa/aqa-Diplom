import com.codeborne.selenide.Condition;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataHelper.*;


public class TicketBuyerTest {

    DataHelper.CardInfo firstCardInfo;
    DataHelper.SecondCardInfo secondCardInfo;





    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        $(By.className("button")).click();
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();



    }

//    @AfterEach
//    static void tearDownAll() {
//        cleanDatabase();
//    }


    @Test
    @DisplayName("Test happy path for form Buy")
    public void shouldTestTheHappyPath() {
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getFirstCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        String expectedID = "APPROVED";
        $(".notification__title")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Успешно"));
        Assertions.assertEquals(expectedID, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Test negative value for card number form Buy")
    public void shouldTestNegativeForCardNumber() {
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getSecondCardInfo().getCardNumber());
        String randomMonth = month();
        $("input[placeholder='" + "08" + "']").setValue(String.valueOf(randomMonth));
        String randomYear = generateYear("yy");
        $("input[placeholder='" + "22" + "']").setValue(String.valueOf(randomYear));
        $$(".input__control").get(3).setValue(DataHelper.ownerInfo());
        int generatedCvc = cvcInfo();
        $("input[placeholder='" + "999" + "']").setValue(String.valueOf(generatedCvc));
        $$("button span.button__text").find(exactText("Продолжить")).click();
        String expectedID = "DECLINED";
        $(".notification__title")
                .shouldBe(visible, Duration.ofSeconds(30))
                .shouldHave(exactText("Успешно"));// не должно быть успешно, должна быть ошибка
        Assertions.assertEquals(expectedID, SQLHelper.geStatusInData());


    }

    @Test
    @DisplayName("Card Number is null")
    public void shouldTestCardNumberNull() {
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").setValue(DataHelper.getNull().getCardNumber());
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Card Number is incorrect")
    public void shouldTestIncorrectCardNumber() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Month value is 13")
    public void shouldTestMonthNumber13() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Month value is 00")
    public void shouldTestMonthNumber00() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Month value is not a two digit number")
    public void shouldTestMonthWithNotTwoDigitNumber() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Month value should be null")
    public void shouldTestMonthValueNull() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Test empty form")
    public void shouldTestEmptyForm() {
        $$("button span.button__text").find(exactText("Продолжить")).click();
        $("input[placeholder='" + "0000 0000 0000 0000" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
        $("input[placeholder='" + "08" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
        $("input[placeholder='" + "22" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
        $$(".input__control").get(3).parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
        $("input[placeholder='" + "999" + "']").parent().parent().lastChild().shouldBe(visible).shouldHave(exactText("Неверный формат"));
    }

    @Test
    @DisplayName("Month value should be past")
    public void shouldTestPastMonth() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Year value should be past")
    public void shouldTestPastYear() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Year value is empty")
    public void shouldTestYearIsEmpty() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Year value is more than five years form now")
    public void shouldTestYearFutureMoreThanFiveYears() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Year value is not a two digit number")
    public void shouldTestYearWithOneDigitNumber() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("CVC value is one digit number")
    public void shouldTestCvcAsOneDigitNumber() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("CVC value is two digit number")
    public void shouldTestCvcAsTwoDigitNumber() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Owner value is with numbers at the end")
    public void shouldTestOwnerWithNumbers() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Owner value is with numbers in the beginning")
    public void shouldTestOwnerWithNumbersAtTheBeginning() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());


    }

    @Test
    @DisplayName("Owner value is digital")
    public void shouldTestOwnerValueAsDigital() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Owner value is empty")
    public void shouldTestOwnerAsEmpty() {
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
                .shouldHave(exactText("Неверный формат"));
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Owner value is with dot at the end")
    public void shouldTestOwnerWithDotAtTheEnd() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());
    }

    @Test
    @DisplayName("Owner value is with dot at the beginning")
    public void shouldTestOwnerWithDotAtTheBeginning() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }

    @Test
    @DisplayName("Owner value is a dot")
    public void shouldTestOwnerAsADot() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());


    }

    @Test
    @DisplayName("Owner value is with hyphen at the end")
    public void shouldTestOwnerWithHyphenAtTheEnd() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }
    @Test
    @DisplayName("Owner value is with hyphen at the beginning")
    public void shouldTestOwnerWithHyphenAtTheBeginning() {
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
        Assertions.assertEquals(null, SQLHelper.geStatusInData());

    }
}
















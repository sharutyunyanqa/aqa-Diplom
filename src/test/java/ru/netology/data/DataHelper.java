package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static final Faker fakerRu = new Faker(new Locale("ru"));


    private static final Faker fakerEn = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("1111 2222 3333 4444");
    }

    public static SecondCardInfo getSecondCardInfo() {
        return new SecondCardInfo("5555 6666 7777 8888");
    }


    public static CardInfoRandom generateRandomCardNumber(){
        return new CardInfoRandom(fakerEn.numerify("################")) ;
    }
    public static String month() {
        Random random = new Random();
        int monthValue = random.nextInt(12) + 1;


        String formattedMonth = String.format("%02d", monthValue);
        return formattedMonth;


    }

    public static int generateMonth() {
        Random random = new Random();
        return random.nextInt(9) + 1;

    }
    public static String lastMonth(String pattern){
        LocalDate lastMonth= LocalDate.now().minusMonths(2);
        return lastMonth.format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String thisYear(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String lastYear(String pattern) {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        return lastYear.format(DateTimeFormatter.ofPattern(pattern));
    }


    public static String generateYear(String pattern) {
        Year currentYear = Year.now();
        Year futureYear = currentYear.plusYears(5);

        return futureYear.format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String futureMoreThanFiveYears(String pattern) {
        Year currentYear = Year.now();
        Year futureYear = currentYear.plusYears(10);

        return futureYear.format(DateTimeFormatter.ofPattern(pattern));
    }


    public static String randomYear() {
        return fakerEn.numerify("#");
    }

    public static String ownerInfo() {
        String firstname = fakerRu.name().firstName();
        String lastname = fakerRu.name().lastName();
        return firstname + " " + lastname;
    }
    public static String ownerInfoEng() {
        String firstname = fakerEn.name().firstName();
        String lastname = fakerEn.name().lastName();
        return firstname + " " + lastname;
    }


    public static int generateOwnerInfo() {
        return fakerRu.number().randomDigit();
    }

    public static int cvcInfo() {
        return fakerRu.number().numberBetween(100, 999);
    }
    public static int cvcNumber(){
        return fakerRu.number().numberBetween(0,9);
    }
    public static String cvcDoubleNumber(){
        return fakerRu.numerify("##");
    }

    @Value
    public static class CardInfo {
        String cardNumber;
    }

    @Value
    public static class SecondCardInfo {
        String cardNumber;
    }

    @Value
    public static class CardInfoRandom {
        String cardNumber;
    }


}

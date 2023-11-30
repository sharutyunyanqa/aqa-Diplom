# Инструкция для запуска авто-тестов 

## Для того чтобы данные авто-тесты запустились на компьютере нужно сделать следующие шаги:

* *Установить на компьютер InetelliJ IDEA. [Руководсвто по установле InetelliJ IDEA ](https://harrix.dev/blog/2019/install-intellij-idea/)*
* *Установить на компьютер Docker Desktop. [Руководство по установке Docker](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)*
* *Запустить на компьютере Docker Desktop*
* *Запустить на компьютере InetelliJ IDEA*
* *Открыть проект QA-Diplom при помощи File-Open- и указать путь к папке проекта*
* *Открыть терминал в InetelliJ IDEA и набрать команду docker-compose up и нажать на кнопку enter*
* *После того, как появится надпись ready for connections, открыть еще одно окно терминала и с помощью команды java -jar aqa-shop.jar запустить jar файл и нажать на кнопку enter*
* *После того как появится надпись  Started ShopApplication in 26.759 seconds (JVM running for ) можно запускать авто-тесты.*

## Для того чтобы их запустить, нужно сделать следующее:
* *Открыть новое окно в терминале*
* *Написать  ./gradlew clean test -D db.url=jdbc:mysql://localhost:3306/app и нажать на кнопку enter*

# Отчет по итогам автоматизации формы покупки тура

Проект по автоматизации тестирования формы покупки тура был успешно завершен. Целью проекта было обеспечить автоматизированные тесты для проверки функциональности и надежности веб-приложения.

Было запланировано сделать авто-тесты только для формы Купить, а по итогам сделатны авто-тесты для обеих форм покупки('Купить' и 'Купить в кредит'). Для удобства тестирование разных форм было разделено на 2 тестовых класса. В каждом классе по 28 тест кейсов из которых 3 позитивных и 25 негативных кейса. 17 из 28 кейсов успешные, на остальные 11 были заведены баг-репорты. Не были реализованы только API тесты для проверки форм оплаты. Причина нехватка времени.

**Сработавшие риски**

* Из-за отсутствия тестовых меток в CSS коде было потрачено больше времени на отбор подходящих селекторов для реализации авто-тестов. 
* Также были проблемы с техникой! В один день обновилась операционная система Windows, Docker Desktop и DBeaver. Это заняло где-то 4-5 часов. 
* Были проблемы с сервисом виртуальных машин gate-simulator, из-за чего нельзя было обращаться к базе данных, смотреть статус покупки, и само приложение работало не корректно, выдавая ошибки при заполнении любых данных.

Так как расчет времени на реализацию проекта был рассчитан с учетом подобных рисков, превышение времени реализации нет. На реализацию проекта было потрачено 25-27 часов.   


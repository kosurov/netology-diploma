# Дипломная работа. Поисковая система

Цель дипломной работы - разработать класс поискового движка, который способен быстро находить указанное слово среди pdf-файлов, причём ранжировать результаты по количеству вхождений. Также нужно разработать сервер, который обслуживает входящие запросы с помощью этого движка.

## Заготовка проекта

В проекте были заготовки кода:

| Класс      | Описание |
| ----------- | ----------- |
| `Main`      | Изначально в нём находилась заготовка использования поискового движка. После его реализации, содержимое `main` я заменил на запуск сервера, обслуживающего поисковые запросы       |
| `PageEntry`   | Класс, описывающий один элемент результата одного поиска. Он состоит из имени пдф-файла, номера страницы и количества раз, которое встретилось это слово на ней        |
| `SearchEngine`   | Интерфейс, описывающий поисковый движок. Всё что должен уметь делать поисковый движок, это на запрос из слова отвечать списком элементов результата ответа        |
| `BooleanSearchEngine`   | Реализация поискового движка, которую мне было необходимо написать       |

## PDF
Для работы с пдф я использовал библиотеку `com.itextpdf:itext7-core:7.1.15`.

## Индексация и поиск
Реализация класса `BooleanSearchEngine`. Для того, чтобы поиск работал быстро, предусмотрено сканирование всех пдф-ок в конструкторе класса с сохранением информации для каждого слова, чтобы метод `search` отрабатывал быстро, по сути возвращая уже посчитанный в конструкторе для слова ответ. Т.е. в конструкторе для каждого слова сохраняется готовый на возможный будущий запрос ответ `List<PageEntry>` (для этого используется мапа, где ключом будет слово, а значением - поисковый ответ для него). Такое предварительное сканирование файлов, по которым мы будем искать, называется _индексацией_.

В итоге, я реализовал логику индексации в конструкторе. Сканируя каждый пдф-файл, мы перебираем его страницы, для каждой страницы извлекаем из неё слова и подсчитываем их количество. После чего, для каждого уникального слова создаём объект `PageEntry` и сохраняем в список `List<PageEntry>`, который затем передаем в мапу. Также предусмотрен регистронезависимый поиск, т.е. по слову "бизнес" учитывается и "бизнес", и "Бизнес" в документах (для этого при обработке каждое слово переводится в нижний регистр с помощью встроенного метода класса `String` для этих целей).

Списки ответов для каждого слова отсортированы в порядке уменьшения поля `count`. Для этого класс `PageEntry` реализует интерфейс `Comparable`.

## Сервер
Для реализации работы поискового движка в `main` запускается сервер, слушающий порт `8989`, к которому будут происходить подключения от клиента `Client` и на входной поток подавать одно слово (обозначим как `word`). Отвечать сервер будет результатом вызова метода `search(word)`, но в виде JSON-текста.

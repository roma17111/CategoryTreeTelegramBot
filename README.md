<h1 style="color: green" align="center">Category Tree Bot</h1>

<h5>Данный проект - телеграмм бот, позволяющий пользователям
создавать, редактировать и удалять дерево категорий
и элементы в нём.</h5>

<h2 style="color: yellow" align="center">Основные команды бота:</h2>

- /addElement [Один параметр]  - Добавить корень к дереву при
 условии, что корня не существует.
- /addElement [1 параметр(Родитель)] [2 параметр(Потомок)] - Добавить
Добавить новый элемень к родительскому элементу.
- /removeElement element - Удаление элемента и всех его потомков
- /viewTree Показать структуру дерева элементов в удомном виде
- /download Загрузить Excel document с деревом элементов в формате xlsx
- /upload Выгрузить Excel document с деревом элементов в формате xlsx
и со внесением изменений в БД.
- /help Помощь по командам.
- <a href=https://web.telegram.org/k/#@category_of_tree_bot>Ссылка на бота</a>

<h2 style="color: red" align="center">Стек технологий проекта:</h2>

- Spring boot telegrambots
- Spring boot starter data JPA
- JDBC
- PostgreSQL
- liquibase для миграции данных 
- Apache POI для работы с excel документами
- maven для сборки проекта
- Docker для размещения бота на удалённом сервере.
            
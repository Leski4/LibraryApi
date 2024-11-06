# LibraryApi
Для начала нужно открыть данный проект в ide.
Далее нужно выставить свои параметры БД такие как username, password в файле application.properties.
Чтобы начать работу в данном API нужно запустить проект, перейти по URL: http://localhost:8080/swagger-ui/index.html#/. Далее необходимо открыть https://jwt.io/ и сгенирировать токен следующим образом:
1. В payload вписать два поля sub - для имя пользователя, aud - для роли пользователя (USER, ADMIN).
2. В verify signature вставить ключ koCQYlY5kyH1lM5hBKleheWGgu4MMm1ADDwXBb8dnN0 и поставить галочку на secret base64 encoded.
3. Берём получившийся токен, переходим в swagger и вставляем его в поле authorize.
4. Теперь можно пользоваться доступными методами.

Сразу после запуска приложения будут созданы и заполнены таблицы БД при помощи Flyway. В демо данных БД уже есть пользователи (для получения токена уже существующих в БД пользователей достаточно использовать только поле sub). Admin может записывать и вычёркивать учёт на книгу только для существующих пользователей в БД. Обычный пользователь может посмотреть свободные для выбора книги и взять её, в дальнейшем он может её вернуть. 

Для запуска тестов необходимо открыть докер с установленным образом postgres:latest. После этого в терминале в idea прописать "mvn test" и с помощью сочетания клавиш ctrl + enter запустить тесты.

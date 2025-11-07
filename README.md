# Deployment guide

В данной конфигурации я подключаюсь к дб использую datasource. Если вам захочется использовать Resource Local в persistence.xml, то вы пропускаете секцию с commands.cli, а также:
 - используете данный dockerfile:
   
   ```
   FROM quay.io/wildfly/wildfly

   COPY modules/ /opt/jboss/wildfly/modules/
    
   COPY ${WAR_PATH} /opt/jboss/wildfly/standalone/deployments/
    
   CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
   ```
 - убираете из docker-compose.yml секцию args.

Чтобы запустить такое чудо инженерной мысли как wildfly необходимо сначала отредактировать файлы  **commands.cli** и **docker-compose.yml** и скачать драйвер базы данных.

Для успешного подключения к бд необходимо скачать драйвер по вашему варианту. У меня это был postgresql.

Например, вот postgresql [драйвер](https://jdbc.postgresql.org/download/postgresql-42.7.8.jar) версии 42.7.8.

Его вы кладете в заранее созданную директорию *modules/org/postgresql/main* (находящуюся в том же каталоге, что и dockerfile) вместе с **module.xml** файлом. Пример последнего лежит в репозитории по пути *modules/org/postgresql/main/module.xml*.

## **commands.cli**:

Этот файл необходим для создания datasource на нашем сервере (wildfly) чтобы мы потом могли с помощью него подключиться к базе данных.

> [!NOTE]
>
> Файл лежит в репозитории по пути customization/commands.cli

В этой секции необходимо заменить драйвер по варианту, а именно все указанные переменные (driver-name, driver-module-name и т.д.).
С этим вам сможет помочь интернет.

```
/subsystem=datasources/jdbc-driver=postgresql:add( \
    driver-name="postgresql", \
    driver-module-name="org.postgresql", \
    driver-class-name=org.postgresql.Driver, \
    driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource \
)
```

Здесь указывамем jndi-name который вы будете использовать в persistence.xml.
Переменные окружения будут браться из тех что вы указали в .env файле (про него в секции docker-compose.yml).

```
/subsystem=datasources/data-source=MyPostgresDS:add( \
    jndi-name="java:/jboss/datasources/MyPostgresDS", \
    enabled=true, \
    connection-url="${env.DATABASE_URL}", \
    driver-name="postgresql", \
    user-name="${env.DB_USERNAME}", \
    password="${env.DB_PASSWORD}" \
)
```
## persistence.xml

Пример этого конфига лежит в репозитории по пути *src/main/resources/META-INF/persistence.xml*. В нашем случае необходим для подключения к базе данных.

## docker-compose.yml

Здесь все просто - вы создаете файл .env в той же директории, где находится docker-compose.yml и прописываете там все параметры - **docker compose** подтянет все переменные окружения оттуда. docker-compose.yml при этом менять не обязательно, только если вы хотите по-другому прокинуть порты.

Пример .env:

```
DATABASE_URL=jdbc:postgresql://web3_db:5432/web3_db
DB_USERNAME=shkibidist
DB_PASSWORD=shkibidi
DB_NAME=web3_db
WAR_PATH=path_to_web_archive
```

Если вы настроили все конфиги под себя и перенесли все необходимые файлы и каталоги:

- customization
- modules
- web3.war, war-архив вашей лабы

то вы можете запустить лабу командой `docker compose up`

> [!NOTE]
>
> Чтобы каждый раз не ребилдить image сервера с целью задеплоить новую версию лабы, вы можете использовать wildfly maven plugin и его goal - wildfly:deploy, либо же подключиться к менеджмент консоли сервера по порту 9990. По умолчанию контейнер пробросит порт 9990 на локалхост.
> Если вы захотите редеплоить лабу удаленно, в docker-compose можете изменить "9990:9990" на "0.0.0.0:9990:9990".
> Редеплой с помощью плагина - `mvn wildfly:deploy` (нужно заранее создать пользователя widfly - подключаетесь к контейнеру с помощью `docker exec -it web3 bash` и исполняете там `wildfly/bin/add-user.sh`).

## dockerfile

Если вы не хотите использовать **docker compose**, то image с wildfly вы можете сбилдить отдельно для последующего использования.

```bash
docker build --build-arg DATABASE_URL=db_url \
        --build-arg DB_USERNAME=username \
        --build-arg DB_PASSWORD=password \
        --build-arg WAR_PATH=war_path \
        dockerfile_path
```

Почему такой костыль с аргументами? Потому что необходимо создать datasource (параметры подключения к бд) еще до запуска сервера, легче всего это сделать на этапе билда.

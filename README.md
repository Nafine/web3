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

Чтобы запустить такое чудо инженерной мысли как wildfly необходимо сначала отредактировать файлы  **commands.cli** и **docker-compose.yml**:

## **commands.cli**:

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

## docker-compose.yml

Здесь все просто - вы создаете файл .env в той же директории, где находится docker-compose.yml и прописываете там все параметры - **docker compose** подтянет все переменные окружения оттуда. docker-compose.yml при этом менять не обязательно, только если вы хотите по-другому покинуть порты.

Пример .env:

```
DATABASE_URL=jdbc:postgresql://web3_db:5432/web3_db
DB_USERNAME=shkibidist
DB_PASSWORD=shkibidi
WAR_PATH=path_to_web_archive
```

## dockerfile

Если вы не хотите использовать **docker compose**, то image с wildfly вы можете сбилдить отдельно для последующего использования.

```bash
docker build --build-args DATABASE_URL=db_url \
        --build-arg DB_USERNAME=username \
        --build-arg DB_PASSWORD=password \
        --build-arg WAR_PATH=war_path \
        dockerfile_path
```

Почему такой костыль с аргументами? Потому что необходимо создать datasource (параметры подключения к бд) еще до запуска сервера, легче всего это сделать на этапе билда.

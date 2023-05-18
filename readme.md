## Requirements

The following software is required to work with the Debezium codebase and build it locally:

* [Git](https://git-scm.com) 2.2.1 or later
* JDK 17 or later, e.g. [OpenJDK](http://openjdk.java.net/projects/jdk/)
* [Docker Engine](https://docs.docker.com/engine/install/) or [Docker Desktop](https://docs.docker.com/desktop/) 1.9 or later
* [Apache Maven](https://maven.apache.org/index.html) 3.8.4 or later  
  (or invoke the wrapper with `./mvnw` for Maven commands)
* [CURL](https://curl.se/) 7.79.1 or later
* [JQ](https://stedolan.github.io/jq/) 1.6 or later

See the links above for installation instructions on your platform. You can verify the versions are installed and running:

    $ git --version
    $ javac -version
    $ mvn -version
    $ docker --version
    $ curl --version
    $ jq --version


## Описание

Возвращает количество сессий для каждого пользователя посредством API метода.

## Тестирование

### Сборка и запуск проекта
Для тестирования необходимо собрать проект и запустить проект в Docker:
```bash
$ chmod +x sh/run
$ sh/run
```

В результате запустится контейнер `Docker`.
В Realm `test` будет добавлен пользователь с параметрами:

      username : test
      password : test

## Тестирование 

### Получение токена пользователя

Чтобы получить токен пользователя выполняем скрипт:

```bash
$ curl -X "POST" "https://localhost:8443/realms/test/protocol/openid-connect/token" \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     -H 'User-Agent: KC-Rest/0.0.1 (iPhone; CPU iPhone OS 5_1_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 iOS/9B206 KC-Rest/0.0.1' \
     --data-urlencode "client_id=test-client" \
     --data-urlencode "client_secret=eucQDUfNCRhf0g05eKg1fvBmfQTI3awu" \
     --data-urlencode "grant_type=password" \
     --data-urlencode "password=test" \
     --data-urlencode "scope=openid" \
     --data-urlencode "username=test"
```

В результате получим ответ :
```json
{
  "access_token": "${ACCESS_TOKEN}",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "${REFRESH_TOKEN}",
  "token_type": "Bearer",
  "id_token": "${ID_TOKEN}",
  "not-before-policy": 0,
  "session_state": "34007ddb-8891-43a4-94fd-59f1a3ac76bb",
  "scope": "openid profile email"
}
```

Здесь нам нужен только `${ACCESS_TOKEN}`.

### Получение количества сессий для каждого пользователя

Чтобы получить количество сессий для каждого пользователя выполняем скрипт :

```bash
$ curl "https://localhost:8443/realms/test/kc-spi-rest/sessions-count" \
     -H 'Content-Type: application/json' \
     -H 'Authorization: Bearer ${ACCESS_TOKEN}'
```
В результате получим ответ :

```json
{
  "${USERNAME}": ${SESSION_COUNT}
}
```
Например если создали только одного пользователя `test` - ответ будет таким :
```json
{
  "test": 1
}
```

## Сборка проекта

После сборки проекта скриптом:

```bash
$ chmod +x sh/run
$ sh/run
```

в директории `target` находится артефакт `kc_spi.jar`
Этот артефакт нужно разместить на сервере Keycloak в директории `/opt/keycloak/providers/`

## Копирование в Docker
```bash
$ docker cp target/kc_spi.jar ${CONTAINER_NAME}:/opt/keycloak/providers/
```
, где `${CONTAINER_NAME}` - имя контейнера с Keycloak

## Использование

Для получения количества сессий используется `GET` запрос.
```bash
$ curl "https://${KC_HOST}/realms/${REALM_NAME}/kc-spi-rest/sessions-count" \
     -H 'Content-Type: application/json' \
     -H 'Authorization: Bearer ${JWT_TOKEN}'

```

, где
`${KC_HOST}` - хост keycloak
`${REALM_NAME}` - имя realm
`${JWT_TOKEN}` - access token

Ответ возвращается в виде :
```json
{
  "${USERNAME}":${SESSION_COUNT}
}
```

, где
`${USERNAME}` - имя пользователя
`${SESSION_COUNT}` - количество сессий

	
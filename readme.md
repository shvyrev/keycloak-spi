## Описание

Возвращает количество сессий для каждого пользователя посредством API метода.

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
  ${USERNAME}:${SESSION_COUNT}
}
```

, где
`${USERNAME}` - имя пользователя
`${SESSION_COUNT}` - количество сессий

## Сборка проекта

Сборка осуществляется скриптом:

```bash
$ chmod +x sh/run
$ sh/run
```

Полученный артефакт нужно разместить на сервере Keycloak в директории `/opt/keycloak/providers/`

### Запуск в Docker
```bash
$ docker cp target/kc_spi.jar ${CONTAINER_NAME}:/opt/keycloak/providers/
```
, где
`${CONTAINER_NAME}` - имя контейнера с Keycloak
	
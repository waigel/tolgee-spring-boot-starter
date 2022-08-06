## tolgee-spring-boot-starter

This spring-boot library in kotlin, integrate the i18n platform tolgee into spring-boot to provide translations.

This library provides translations from tolgee to spring boot.
Important for e-mails or notifications that are created on the backend and sent to the customer.

## Example

```kotlin
@RestController
class TestController(
    private var translation: TranslateClient
) {
    @GetMapping("/")
    fun returnTestTranslation(): String {
        val params: HashMap<String, String> = linkedMapOf();
        params["name"] = "Johannes"
        params["created"] = "06.08.2022"
        return translation.t("TEST_WITH_PARAMS", "en", params)
    }
}
```

## Configuration

You need an api key for the tolgee server with minimal translation view permissions.

```properties
tolgee.enabled=true //optional, default true
tolgee.default-language=de-DE //optional
tolgee.force-language=en //optional
tolgee.api-key=<api key> //required
tolgee.api-url=https://app.tolgee.io/v2 //optional, required if different
```

## Sync

`tolgee-spring-boot-starter` can automatic sync the translation in a certain interval to keep translations up to date
without interrupting the server.

You can enable this feature with the following configuration

```properties
tolgee.sync.enabled=true
tolgee.sync.interval=60000 //interval in ms
```
___
This project is licensed under MIT.

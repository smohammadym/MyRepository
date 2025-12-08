# Webentwicklung Backend / ük 295

## Branches & Tags

Branches und Tags werden mit den Fallstudienaufträgen destruktiv gemacht, heisst `main` hat den Zielzustand, der Start für den
letzten Auftrag hat einen commit mehr, in welchem das Delta weggenommen wird.
Einzig der branch `deployment` hat nicht destruktive commits mehr als `main`.

**Alle Änderungen am Produkt (ausser `deployment`) kommen so auf den `main` branch.
Danach müssen die Änderungen auf initial propagiert werden und die tags verschoben (siehe Befehle unten)**

| Branch/Tag           | Beschreibung                                                                                            |
|----------------------|---------------------------------------------------------------------------------------------------------|
| __deployment__       | Azure Deployment                                                                                        |
| __main__             | Endzustand Fallstudie nach Auftrag 8                                                                    |
| __open-api__         | Ausgangslage Auftrag Fallstudie 8: Globales Exceptionhandling, Endzustand Auftrag Fallstudie 7: OpenAPI |
| __validation__       | Ausgangslage Auftrag Fallstudie 7: OpenAPI, Endzustand Fallstudie Auftrag 6: Validation                 |
| __testing__          | Ausgangslage Fallstudie Auftrag 6: Validation, Endzustand Fallstudie Auftrag 5: Controllertests         |
| __testing-initial__  | Ausgangslage Fallstudie Auftrag 5: Controllertests                                                      |
| __tag-ressource__    | Endzustand Fallstudie Auftrag 4: Tag                                                                    |
| __item-service-dto__ | Ausgangslage Fallstudie Auftrag 4: Tag, Endzustand Fallstudie Auftrag 3: Item mit Service und DTO       |
| __item__             | Ausgangslage Fallstudie Auftrag 3: Item mit Service und DTO, Endzustand Fallstudie Auftrag 2: Item      |
| __initial__          | Ausgangslage der gesamten Fallstudie - Fallstudie Auftrag 1: SetUp erzeugt keine Änderungen             |

Folgende Befehle müssen entsprechend verwendet werden:

```shell
durchfuehrung="BA23"

git checkout deployment && git rebase main
git push -f

git checkout initial && git rebase main
git push -f

git tag -fs item HEAD~1 -m $durchfuehrung
git tag -fs item-service-dto HEAD~2 -m $durchfuehrung
git tag -fs tag-ressource HEAD~3 -m $durchfuehrung
git tag -fs testing-initial HEAD~4 -m $durchfuehrung
git tag -fs testing HEAD~5 -m $durchfuehrung
git tag -fs validation HEAD~6 -m $durchfuehrung
git tag -fs open-api HEAD~7 -m $durchfuehrung

git push -f --tags
```

## Wichtige Ordner

| Ordner       | Beschreibung                                                |
|--------------|-------------------------------------------------------------|
| **database** | Schema und SQL-Files mit MySQLWorkbench erzeugt             |
| **docs**     | Aktuelle für die Entwicklung relevante Dokumente und Bilder |
| **src**      | Java-Source-Code                                            |

## Applikation starten

1. Lokal eine MySQL Instanz installieren
2. `database/create-database.sql` ausführen (erzeugt Datenbank inkl. User)
    * alternativ das Script `database/create-database-with-data.sql` ausführen, um Testdaten zu erhalten
3. Im Terminal mit ```gradlew bootRun``` oder über das Gradle-Tab in der IDE die App starten
4. Applikation kann über ```http://localhost:8080``` angesprochen werden
5. Wurde DB mit Daten geladen, existiert der Benutzer `user` mit dem Passwort `test`
6. Zum Einloggen POST-Request an ```http://localhost:8080/auth/signin``` schicken
   mit ```{"username": "user", "password": "test"}``` im Body
7. Im Body der Response des Logins sind die Informationen für den Token vorhanden. (`tokenType accessToken`,
   z.B. `Bearer exJh...`) Diese müssen bei jedem künftigen Request im Header als value zum Attribut `Authorization`
   gesetzt werden.

## Nützliche urls

* Api-Docs und Swagger
    * [http://localhost:8080/v3/api-docs/](http://localhost:8080/v3/api-docs/)
    * [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* API-Endpunkte
    * [http://localhost:8080/persons](http://localhost:8080/persons)
    * [http://localhost:8080/items](http://localhost:8080/items)
    * [http://localhost:8080/tags](http://localhost:8080/tags)

# Docker MySql Images
Falls MySql von Docker aus gestartet werden soll, kann folgendes Kommando verwendet werden:
> docker run --name mysql_todo_list -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_todo_list -e MYSQL_USER=db_todo_list -e MYSQL_PASSWORD=Pass4CRUD2020 -p3306:3306 -d mysql:8.0.33


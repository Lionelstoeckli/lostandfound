# Setup-Anleitung — Lost & Found Backend (Modul 295)

Dieses Dokument beschreibt alle nötigen Konfigurationseinstellungen, um das Projekt lokal zu starten.

## 1. Voraussetzungen

| Komponente | Version |
|-----------|---------|
| Java JDK  | 26 |
| Maven     | 3.9+ (oder mvnw verwenden) |
| PostgreSQL| 14+ (lokal auf Port 5432) |
| Keycloak  | 24+ (lokal auf Port 8080) |

## 2. Datenbank (PostgreSQL)

| Einstellung | Wert |
|-------------|------|
| Host        | `localhost` |
| Port        | `5432` |
| Datenbank   | `lostandfound` |
| Benutzer    | `postgres` |
| Passwort    | `12345` |

Datenbank vor dem ersten Start anlegen:

```sql
CREATE DATABASE lostandfound;
```

Die Tabellen werden beim ersten Start der Applikation automatisch erstellt
(Hibernate `ddl-auto: update`).

## 3. Keycloak

| Einstellung | Wert |
|-------------|------|
| Issuer-URI  | `http://localhost:8080/realms/lostandfound` |
| Realmname   | `lostandfound` |
| Client-ID   | `lostandfound` |
| Client-Typ  | public, Direct Access Grants enabled |
| Rollen      | `user`, `admin` (Client-Rollen) |

### Realm-Import

Der Realm-Export liegt unter `keycloak/lostandfound-realm.json` und kann in der
Keycloak-Admin-Konsole über *Add realm → Import* eingelesen werden.

### Test-Benutzer

| Username | Passwort | Rollen        |
|----------|----------|---------------|
| `user`   | `user`   | `user`        |
| `admin`  | `admin`  | `user`, `admin` |

## 4. Netzwerk-Ports

| Dienst        | Port  |
|---------------|-------|
| Backend (Spring Boot) | `9090` |
| Keycloak      | `8080` |
| PostgreSQL    | `5432` |
| Frontend (CORS-erlaubt) | `4200` |

## 5. Projekt starten

```bash
./mvnw spring-boot:run
```

Anschliessend ist die Swagger-UI erreichbar unter:

```
http://localhost:9090/swagger-ui.html
```

Über das Schloss-Symbol in der Swagger-UI kann ein Bearer-Token hinterlegt
werden, das vorher z.B. via Postman vom Keycloak abgefragt wurde:

```
POST http://localhost:8080/realms/lostandfound/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

client_id=lostandfound
grant_type=password
username=admin
password=admin
scope=openid profile roles offline_access
```

## 6. Tests ausführen

```bash
./mvnw test
```

Voraussetzung: Keycloak läuft (für `ItemControllerTest`, der echte Tokens
abruft). `DBTest` und `ItemServiceTest` benötigen keinen Keycloak.

## 7. Endpoints (Übersicht)

| Methode | Pfad | Rolle |
|--------|------|-------|
| GET    | `/api/user`, `/api/item`, `/api/report`, `/api/claim` | user / admin |
| GET    | `/api/{resource}/{id}` | user / admin |
| POST   | `/api/{resource}` | user / admin |
| PUT    | `/api/{resource}/{id}` | admin |
| DELETE | `/api/{resource}/{id}` | admin |
| GET    | `/api/report/type/{type}` | user / admin |
| GET    | `/api/claim/status/{status}` | user / admin |

[![javaEksamen](https://github.com/kristiania-pgr203-2021/pgr203-exam-java-2021/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr203-2021/pgr203-exam-java-2021/actions/workflows/maven.yml)

# PGR203 Avansert Java eksamen


## Beskriv hvordan programmet skal testes:
index.html fungerer som løsningens forside. Den lister opp de registrerte spørsmålene med hver enkelt spørsmåls svaralternativer.

Hvis man på forsiden trykker på lenken med teksten «Add new question» føres man til siden newQuestionnaire.html, hvor man kan legge 
til et spørsmål med tittel og tekst. Man blir også stilt et valg om hvordan spørsmålet skal framstilles for respondenten.
Det gis to valgmuligheter å velge mellom. Svaralternativene kan framstilles for spørsmålsrespondenten enten i form av 
en enkel opplisting med hvert alternativ på hver sin linje under hverandre, eller som en skala presentert i form av en nedtrekksmeny
med seks svaralternativer som rangerer fra enig til helt uenig. 
Når man har lagret et spørsmål i databasen blir man ført videre til siden addOption.html, hvor man kan legge til svaralternativer til 
registrerte spørsmål.

På samme side (dvs addOption.html) kan man registrere seg med fornavn, etternavn og epostadresse. 
Fyller man inn dette, vil man få lastet inn siden på ny med en melding om at man er registrert og hvilken 
epostadresse man er blitt registrert med. Fornavnet blir lagret i en cookie når man trykker for å lagre et svaralternativ.

Når man har trykket for å lagre et svaralternativ i databasen, får man tilbakemelding om at svaralternativet har blitt lagret, og et valg 
av to lenker, hvorav den ene viderefører til forsiden, mens den andre fører tilbake til forrige side hvor man kan legge til et 
nytt svaralternativ.

Hvis man på forsiden trykker på lenken med teksten «Add scale to question» blir man ført til siden ScaleToQuestion.html, hvor man
kan besvare et spørsmål med et av seks svaralternativer som rangerer på en skala fra enig til helt uenig.
Ved lagring av et av skalaens svaralternativer vil man automatisk bli videreført til siden listAll.html, hvor alle spørsmålene listes opp
med respondentenes besvarelser på dem.

Hvis man på forsiden trykker på lenken med teksten «filter questionnnaire» blir man ført til siden questionnaireFilter.html, hvor man 
fra en nedtrekksliste kan filtrere på de registrerte spørsmålene. Det registrerte spørsmålet man velger vil returnere alle svaralternativene
registrert for det valgte spørsmålet.

Hvis man på forsiden trykker på lenken merket med teksten «Update question» blir man ført til siden updateQuestion.html, hvor man kan velge 
å endre tittel eller tekst på det spørsmålet man velger fra nedtrekksmeny.

EKSTRA FUNKSJONALITET LAGT TIL:
* Man kan registrere seg med fornavn, etternavn og epostadresse på samme side som man lagrer et svaralternativ (addOption.html).
Fornavnet blir lagret i en cookie når man trykker for å lagre et svaralternativ.
Da initialiseres cookie-navnet og sendes med i messageBody. Men når man går til en annen side blir write-metoden oppdatert, hvilket fører
til at verdien i cookie-en blir nullsatt. Vi kunne kanskje brukt en HashMap for write-metoden, men tiden strakk ikke til.

* Diagram som illustrerer hvordan programmet virker er opprettet i Lucidchart. Lenke til diagrammet er vedlagt på linjen nedenfor.
  https://lucid.app/documents/view/2c4d73d6-8365-4d19-840e-632b44dbe42d

* Når man registrerer seg med fornavn, etternavn og epostadresse på siden addOption.html utføres det en POST som sender brukeren
tilbake dit de var før. Man får lastet inn siden på ny med en melding om at man er registrert og hvilken epostadresse man er 
blitt registrert med.

* Alle løsningens sider encoder med UTF-8 slik at norske tegn vises som de skal. Det fungerer i tester som gjør både POST og GET.

* Vi har benyttet AbstractDao for retrieve og list.

* Feil content-type er rettet slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer.

* addNewMembersController håndterer controllers slik at en GET og en POST request kan ha samme request target.

OBS. Programmet fungerer, men vi opplever utfordringer når vi pakker JAR som for oss framstår uforståelige. Da vi kjørte JAR-filer viste HTML-koden seg å ikke være encodet som den skulle. Vår dataSource befinner seg i QuestionnaireServer.java. 
Slik ligger det i FileReader i filens linje 63: FileReader("AvansertJavaEksamen/pgr203.properties")


## Korreksjoner av eksamensteksten i Wiseflow:

**DET ER EN FEIL I EKSEMPELKODEN I WISEFLOW:**

* I `addOptions.html` skulle url til `/api/tasks` vært `/api/alternativeAnswers` (eller noe sånt)

**Det er viktig å være klar over at eksempel HTML i eksamensoppgaven kun er til illustrasjon. Eksemplene er ikke tilstrekkelig for å løse alle ekstraoppgavene og kandidatene må endre HTML-en for å være tilpasset sin besvarelse**


## Sjekkliste

## Vedlegg: Sjekkliste for innlevering

* [x] Dere har lest eksamensteksten
* [ ] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [x] Koden er sjekket inn på github.com/pgr203-2021-repository
* [ ] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [ ] `README.md` inneholder en korrekt link til Github Actions
* [x] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
* [x] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [x] `README.md` inneholder et diagram som viser datamodellen

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [x] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [x] Serveren leser HTML-filer fra JAR-filen slik at den ikke er avhengig av å kjøre i samme directory som kildekoden
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [x] Programmet kan opprette spørsmål og lagrer disse i databasen (påkrevd for bestått)
* [x] Programmet kan vise spørsmål (påkrevd for D)
* [x] Programmet kan legge til alternativ for spørsmål (påkrevd for D)
* [x] Programmet kan registrere svar på spørsmål (påkrevd for C)
* [x] Programmet kan endre tittel og tekst på et spørsmål (påkrevd for B)

### Ekstraspørsmål (dere må løse mange/noen av disse for å oppnå A/B)

* [x] Før en bruker svarer på et spørsmål hadde det vært fint å la brukeren registrere navnet sitt. Klarer dere å implementere dette med cookies? Lag en form med en POST request der serveren sender tilbake Set-Cookie headeren. Browseren vil sende en Cookie header tilbake i alle requester. Bruk denne til å legge inn navnet på brukerens svar
* [x] Når brukeren utfører en POST hadde det vært fint å sende brukeren tilbake til dit de var før. Det krever at man svarer med response code 303 See other og headeren Location
* [x] Når brukeren skriver inn en tekst på norsk må man passe på å få encoding riktig. Klarer dere å lage en <form> med action=POST og encoding=UTF-8 som fungerer med norske tegn? Klarer dere å få æøå til å fungere i tester som gjør både POST og GET?
* [x] Å opprette og liste spørsmål hadde vært logisk og REST-fult å gjøre med GET /api/questions og POST /api/questions. Klarer dere å endre måten dere hånderer controllers på slik at en GET og en POST request kan ha samme request target?
* [x] Vi har sett på hvordan å bruke AbstractDao for å få felles kode for retrieve og list. Kan dere bruke felles kode i AbstractDao for å unngå duplisering av inserts og updates?
* [ ] Dersom noe alvorlig galt skjer vil serveren krasje. Serveren burde i stedet logge dette og returnere en status code 500 til brukeren
* [ ] Dersom brukeren går til http://localhost:8080 får man 404. Serveren burde i stedet returnere innholdet av index.html
* [ ] Et favorittikon er et lite ikon som nettleseren viser i tab-vinduer for en webapplikasjon. Kan dere lage et favorittikon for deres server? Tips: ikonet er en binærfil og ikke en tekst og det går derfor ikke an å laste den inn i en StringBuilder
* [ ] I forelesningen har vi sett på å innføre begrepet Controllers for å organisere logikken i serveren. Unntaket fra det som håndteres med controllers er håndtering av filer på disk. Kan dere skrive om HttpServer til å bruke en FileController for å lese filer fra disk?
* [x] Kan dere lage noen diagrammer som illustrerer hvordan programmet deres virker?
* [x] JDBC koden fra forelesningen har en feil ved retrieve dersom id ikke finnes. Kan dere rette denne?
* [x] I forelesningen fikk vi en rar feil med CSS når vi hadde `<!DOCTYPE html>`. Grunnen til det er feil content-type. Klarer dere å fikse det slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer?
* [ ] Klarer dere å lage en Coverage-rapport med GitHub Actions med Coveralls? (Advarsel: Foreleser har nylig opplevd feil med Coveralls så det er ikke sikkert dere får det til å virke)
* [ ] FARLIG: I løpet av kurset har HttpServer og tester fått funksjonalitet som ikke lenger er nødvendig. Klarer dere å fjerne alt som er overflødig nå uten å også fjerne kode som fortsatt har verdi? (Advarsel: Denne kan trekke ned dersom dere gjør det feil!)

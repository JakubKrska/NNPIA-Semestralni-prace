Tento projekt je semestrální prací pro předmět NNPIA. 
Cílem aplikace je poskytnout majitelům domácích mazlíčků nástroj pro sledování zdraví, očkování a medikace jejich zvířat.

PetCare je moderní webová aplikace, která řeší problém roztříštěných informací o zdravotním stavu domácích zvířat. 
Uživatelé mohou spravovat profily svých mazlíčků, zaznamenávat návštěvy u veterináře a využívat inteligentního asistenta pro rychlou konzultaci symptomů.

Klíčové vlastnosti:
 Digitální zdravotní knížka: Historie očkování a lékařských prohlídek.
 Profily mazlíčků: Evidence věku, váhy, plemene a specifických potřeb.
 AI Symptom Checker: Konzultace zdravotního stavu pomocí umělé inteligence.
 Zabezpečený přístup: Každý uživatel spravuje pouze svá data.

 Entity: Owner (User), Pet (jméno, druh, věk), MedicalRecord (typ záznamu, datum, poznámka, příloha).

 Funkce: "AI Pet Consultant" – Uživatel popíše symptomy nebo chování mazlíčka a aplikace na základě historie zvířete a jazykového modelu poskytne doporučení

![PetCare123](https://github.com/user-attachments/assets/915fffbc-18eb-4c3c-9f58-dd5e55837d01)

Popis entit a atributů

1. App_User (Uživatel / Majitel)
Tato entita představuje identitu v systému. Je klíčová pro proces autentizace a autorizace pomocí JWT.
 email: Slouží jako unikátní uživatelské jméno (login).
 password: Ukládá zahashovanou podobu hesla (pomocí BCrypt).
 first_name / last_name: Základní osobní údaje majitele.
 Relace: Jeden uživatel může vlastnit více mazlíčků (1:N).

3. Role a User_Roles (Zabezpečení)
Pro zajištění bezpečnosti bude implementován model RBAC (Role-Based Access Control).
 Role: Definuje oprávnění v systému (např. ROLE_USER pro běžné uživatele, ROLE_ADMIN pro správu).
 User_Roles: Vazební tabulka, která realizuje vztah Many-to-Many mezi uživateli a rolemi. To umožňuje, aby uživatel měl v budoucnu více rolí současně.

4. Pet (Domácí mazlíček)
Centrální entita celé aplikace, která propojuje majitele se zdravotními daty.
 species / breed: Druh a plemeno jsou důležité pro kontext AI asistenta (jiné rady pro rozdílná zvířata).
 birth_date: Ukládá datum narození, ze kterého aplikace dynamicky vypočítává aktuální věk.
 weight: Aktuální váha, která je klíčová pro záznamy o medikaci a dávkování.

5. Medical_Record (Lékařský záznam)
Představuje digitální deník zdraví.
 record_date: Časový údaj o návštěvě nebo události.
 type: Kategorie záznamu (např. očkování, operace, běžná kontrola, akutní stav).
 description: Detailní textový popis nálezu nebo instrukcí od veterináře.
 attachment_url: Cesta k uloženým souborům (např. naskenovaná lékařská zpráva nebo výsledky krve).
 Relace: Každý záznam je pevně vázán k jednomu konkrétnímu mazlíčkovi (N:1).

Popis datových vazeb
Uživatel (1) - Mazlíček (N): Každý mazlíček má v systému právě jednoho vlastníka, který má výhradní přístup k jeho datům. Pokud uživatel svůj účet smaže, dojde díky kaskádovému nastavení i k odstranění profilů jeho zvířat.
Mazlíček (1) - Lékařský záznam (N): Jeden mazlíček může mít v průběhu života neomezené množství záznamů, které tvoří jeho ucelenou zdravotní historii.
Využití pro AI Asistenta: Architektura umožňuje při dotazu na AI asistenta (AI Pet Consultant) načíst historii z tabulky Medical_Record a biometrické údaje z tabulky Pet, což poskytne jazykovému modelu (OpenAI) přesný kontext pro generování doporučení.

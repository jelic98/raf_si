## Uvod

Ovim dokumentom je prikazana, analizirana i definisana potreba za Project1. Dokument se fokusira se na potrebe zainteresovanih strana i opisuje detalje o tome kako Project1 ispunjava potrebe.

### Svrha

Ovaj dokument upoznaje kupce i korisnike sa softverskim proizvodom Project1.

### Definicije, akronimi i skraćenice

U ovom dokumentu koristimo sledeće akronime i skraćenice:

* **Project1** - Kolaborativni alat za modelovanje softverskih rešenja projektnih timova.

* **Projektni tim** -  skup registrovanih korisnika koji će zajedno raditi na dizajnu proizvoljnog softvera.

* **RQM** - Model zahteva sadrži zahteve korisnika i ima za cilj postizanje maksimalnog razumevanja i onih koji će projektovati i realizovati sistem.

* **FUM** - Funkcionalni model prikazuje skup slučajeva korišćenja sistema i aktera van granica sistema.

* **CRUD** - Operacije kreiranja, učitavanja, ažuriranja i brisanja podataka.

* **Import** - Automatski ili poluautomatski uvoz podataka ili skupova podataka između aplikacija.

* **Export** - Automatski ili poluautomatski izvoz podataka ili skupova podataka između aplikacija.

* **Kloniranje** - Proces kloniranja je postupak pravljenja identične kopije podataka.

* **Spajanje** - Objedinjenje dve ili više različitih verzija podataka.

* **Lokalizacija** - prilagođavanje proizvoda ili usluge kako bi se zadovoljile potrebe određenog jezika ili kulture.

### Pregled

Ostatak dokumenta sadrži karakteristike, ograničenja i dokumentaciju za softverski proizvod Project1. Takođe, čitalac ima pristup modelima koji su se koristili za razvoj softvera i može instalirati njegovu probnu verziju.

## Pozicioniranje

### Poslovna prilika

Moderni projektni timovi zahtevaju što efikasniji prelaz od ideje do realizacije konkretnog proizvoda. Kolaborativno modelovanje, mogućnost izmene modela u realnom vremenu i generisanje koda na osnovu modela znatno ubrzavaju razvoj proizvoda. Činjenica da ovaj alat radi u veb okruženju čini ga dostupnim i raspoloživim na svim uređajima koji su u posedstvu članova projektnih timova. Intuitivnost korisničkog interfejsa omogućava brzo prilagođavanje članova projektnih timova novom načinu poslovanja.

### Izjava o problemu

Zbog kompleksnosti alata za modelovanje, kako modernih tako i onih koji su na tržistu već dugi niz godina, kompanije su prinuđene da troše resurse u svrhe obučavanja svojih zaposlenih za efikasno i efektivno koršćenje ovih alata. Takođe, kompanija koja obezbedi obuku zaposlenog, indirektno subvencioniše konkurentsku kompaniju u koju zaposleni pređe nakon određenog vremena. Još jedna mana postojećih alata jeste nemogućnost istovremene kolaboracije u procesu izrade istog modela. Platformska zavisnost alata ograničava projektne timove po pitanju slobode izbora operativnih sistema za razvoj softverskih alata.

### Izjava o rešenju

Rešenje treba da omogući automatizaciju operacija nad modelima softverskih rešenja projektnih timova. Takođe, rešenje ne treba biti vezano za specifični domen, već se treba koristiti u poslovnim sistemima iz različitih domena.

## Pregled

Ovaj odeljak pruža visok nivo prikaza mogućnosti proizvoda, interfejsa za druge aplikacije i konfiguracije sistema.

### Perspektiva proizvoda

Softverski proizvod Project1 je nezavisan prilikom upotrebe. Tokom korišćenja alata, realizuje se komunikacija sa eksternim servisima poput servisa za prijavljivanje korisnika, servisa za logovanje grešaka, servisa za online pomoć, itd.

### Sažetak sposobnosti

Glavne funkcionalnosti alata Project1 podrazumevaju osnovne operacije za modelovanje softverskih rešenja projektnih timova, uključujuci kolaboraciju u realnom vremenu, konverzije izmedju modela i generisanje koda na osnovu modela. U radnom prostoru alata, korisnik može kreirati projekat, i u okviru projekta napraviti novi model. Uz oslonac na ovo rešenje, on može da kreira modele zahteva, funkcionalne modele i klasne dijagrame. Kako bi se ubrzali procesi dizajna i razvoja, alat obezbeđuje podršku za konverziju: modela zahteva u funkcionalni model i klasnog modela u Java kod. Zajednički rad na projektu je obezbeđen kroz praćenje verzija projekata. Svoje verzije modela korisnik komituje u neku bazu ili na server, a saradnici je mogu povući sa servera i spojiti sa svojim verzijama modela. Ovo može da funkcioniše i na drugačiji način, zavisno od izbora arhitekture. Zamisao je da praćenje i sinhronizovanje verzija bude jedna od funkcionalnosti.

### Pretpostavke i zavisnosti

Alat Project1 se može izvršavati u okviru svih veb pregledača.

### Licenciranje i instalacija

Naši korisnici dobijaju licencu za korišćenje alata Project1, koja važi samo za jednan računar.

## Кarakteristike

Ovaj dokument sadrži opšte mogućnosti softverskog proizvoda Project1.

* **CRUD** - Korisnik ima mogućnost korišćenja CRUD operacija.

* **Import** - Uvoz prethodno sačuvanih modela u radno okruženje.

* **Eksport** - Eksportovanje modela u formatu koje odredi korisnik.

* **Kloniranje** - Korisniku je omogućeno kloniranje modela u cilju uštede vremena pri kreiranju slicnih, koji zahtevaju minimalne izmene.

* **Spajanje** - Korisnik ima mogućnost da objedini dva ili više modela u cilju uštede vremena pri kreiranju.

* **Kolaboracija u realnom vremenu** - Više korisnika ima mogućnost izmene istog modela u realnom vremenu.

* **Konverzija modela** - Korisniku je omogućena konverzija između različitih tipova modela.

* **Generisanje koda** - Korisniku ima mogućnost generisanja koda direktno na osnovu klasnog modela.

## Ograničenja

### Bezbednost

Unutrašnje komponente alata ne smeju biti čitljive i lako dostupne korisniku.

* Heširanje fajlova na lokalnom skladištu - Svaki fajl koji je generisan od strane Project1-a treba biti heširan pre čuvanja kako bi korisniku onemogućio uvid u unutrašnjost alata.

* Bezbedna autentifikacija - Autentifikacija se treba dešavati na centralnom serveru i treba biti otporna na razne hakerske napada poput SQL injekcije i CSRF.

### Raspoloživost

Alat treba biti spreman za upotrebu u svakom trenutku i ne sme otkazivati.

### Portabilnost

Alat treba da se pokrene i radi bez mana na različitim platformama (MacOS, Windows, Linux, itd.) i u okviru različitih veb pregledača (Google Chrome, Opera, Mozilla Firefox, Safari, itd.) pri čemu treba imati konzistentan raspored UI elemenata i funkcionalnost.

### Lokacizacija

Alat bi trebalo da ima mogućnost lokalizacije.

### GUI

Radno okruženje alata treba da bude predstavljeno kroz grafički korisnički interfejs.

### Događajima upravljani sistem

Sistem treba da reaguje na događaje i okrivu i van granica sistema.

### Interaktivni sistem

Sistem je baziran na velikoj količini interakcija sa korisnikom.

## Dokumentacija

Ovaj odeljak opisuje dokumentaciju koja se mora razviti da bi podržala uspešnu implementaciju i korišćenje softverskog proizvoda Project1.

### Uputstvo za korišćenje

Korisnik ima mogućnost da u svakom trenutku pročita uputstvo kome se pristupa iz samog alata. Uputstvo sadrži detaljan pregled mogućnosti čije je korišćenje objašnjeno korak-po-korak. Uputstvo je stvoreno da pojasni opštu namenu alata, dok se za specifičnu upotrebu preporučuje online pomoć.

### Online pomoć

Online pomoć je namenjena korisnicima kojima uputstvo nije pomoglo ili alat koriste za specifični slučaj. Korisnk ima mogućnost da u svakom trenutku postavi pitanje čiji odgovor se dobija u roku od 24h. Pitanje se postavlja popunjavanjem [formulara](https://www.lazarjelic.com/ecloga/projects/software_admin/help.php).


## Odabir arhitekture softverskog rešenja

### Broker pattern

Ovaj arhitektonski šablon je pogodan za struktuiranje distribuiranih softverskih sistema sa nezavisnim komponentama koje interaguju putem RPC poruka. Broker je komponenta koja je odgovorna za koordinisanje komunikacije, kao što je prosleđivanje zahteva i prenošenje rezultata i izuzetaka.

![Broker pattern](https://miro.medium.com/max/875/1*1qRQZjLRAd0yY_T9p2OgBw.png)

#### Prednosti

* Proširivost komponenti

* Portabilnost

* Kompatibilnost sa drugim sistemima

* Ponovna upotrebljivost

#### Mane

* Ograničena efikasnost

* Manja tolerancija na kvar

* Testiranje i otklanjanje grešaka


### Event-bus pattern

Arhitektura vođena događajima je kompatibilna sa mikrosevisnom arhitekturom. Kao posledica akcije, događaj se kreira i koristi se pri prenošenju poruka svima onima koji ih očekuju. Servisi nisu više jako spregnuti, jer u publish-subscribe modelu, pozivalac ne upućuje poziv direktno. Umesto toga, pozvana komponenta reaguje na događaj na eventualno konzistentan način, što znači da sistem ne poštuje ACID princip.

![Event-bus pattern](https://miro.medium.com/max/790/1*DOZ4nVR9zkJm-EnXT3KOGQ.png)

#### Prednosti

* Skalabilnost

* Asinhronost

* Mogućnost oporavka

#### Mane

* Nekonzistentnost

* Povećana kompleksnost

### Pipe-filter pattern

Ovaj arhitektonski šablon je pogodan za dekompoziciju zadatka koji vrši kompleksno procesiranje na seriju zasebnih elemenata koji se mogu ponovo upotrebiti. Ovo može poboljšati skalabilnost i ponovnu upotrebljivost tako što dozvoljava da elementi zadatka koji vrše procesiranje budu raspoređeni na mašinama i skalirani nezavisno.

![Pipe-filter pattern](https://miro.medium.com/max/875/1*qikehZcDhhl_wWsqeI_nvg.png)

#### Prednosti

* Razumljivost

* Ponovna upotrebljivost

* Konkurentno izvršavanje

#### Mane

* Povećana kompleksnost

* Smanjene performanse

* Nemogućnost interaktivne transformacije

### Studija slučaja

Za izradu alata Project1 opredeljujemo se za mikroservisnu arhitekturu, tj. Broker pattern. Prednosti ovog pristupa uključuju skalabilnost na nivou servisa, proširivost i ponovnu upotrebljivost tako što interfejsi dopuštaju promene u implementaciji bez potrebe za izmenom klijenta. Sa druge strane, ovaj pristup nije pogodan zato što zahteva dodatni posao održavanja oko međusobnih ugovora i popravke servisa nakon otkaza. Dodatni problem mogu biti performanse kompletnog sistema zato što pojedinačni zahtev mora proći kroz više čvorova u distribuiranoj mreži.

![Arhitektura alata Project1](https://github.com/racunarski-fakultet/si2021-si-group-deepsea/blob/main/arch.png)

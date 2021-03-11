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

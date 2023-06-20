from abc import ABC
import time
import sys
from datetime import datetime
import statistics


# strategy
class Izvor(ABC):
    def load_number(self):
        pass

# concrete strategies
class TipkovnickiIzvor(Izvor):
    def load_number(self):
        input_data = sys.stdin.readline().rstrip() # read from standard input
        if (input_data.isdigit()):
            return int(input_data)
        else: 
            return -1

class DatotecniIzvor(Izvor):
    def __init__(self, datoteka):
        super().__init__()
        self.datoteka = datoteka    # ime datoteke
        self.position = 0           # pamtimo do kojeg retka smo procitali datoteku 
        self.file  = None

    def load_number(self):
        if self.file is None:
            self.file = open(self.datoteka, "r")
            self.position = self.file.tell()

        self.file.seek(self.position)           # prebaci nas na redak koji jos nismo procitali, a sljedeci je na redu
        line = self.file.readline().strip()     # data zapisan u tom retku

        if line.isdigit():
            self.position = self.file.tell()    # update td znamo gdje nastavljamo citat sljedeci put 
            return int(line)
        else:
            return -1




# observer
class Akcija(ABC):
    def update(self):
        pass

# concrete observers
class AkijaZapisiSve(Akcija):
    def update(self, kolekcija):
        with open('output.txt', 'w') as file:
            file.write(' '.join(str(elem) for elem in kolekcija))
            file.write('\n')
            file.write(str(datetime.now()))

class AkcijaIspisiSum(Akcija):
    def update(self, kolekcija):
        print('Sum: ', sum(kolekcija))

class AkijaIspisiAvg(Akcija):
    def update(self, kolekcija):
        print('Average: ', sum(kolekcija)/len(kolekcija) if kolekcija else 0)

class AkcijaIspisiMedian(Akcija):
    def update(self, kolekcija):
        print('Median: ', statistics.median(kolekcija) if kolekcija else 0)




# context and subject
class SlijedBrojeva():
    def __init__(self, izvor: Izvor) -> None:
        self.kolekcija = [] # interna kolekcija
        self.izvor = izvor  # izvor podataka (concrete strategy)
        self.akcije = []    # popis akcija (concrete observera)

    # dodaj novu akciju u listu
    def dodaj_akciju(self, akcija):
        self.akcije.append(akcija)

    # obavijesti svakog observera da si dobio novi podatak i da se updata
    def obavijesti(self, kolekcija):
        for akcija in self.akcije:
            akcija.update(kolekcija)

    def kreni(self):
        while True:
            next_number = self.izvor.load_number()  # uzmi novi broj iz konkretnog izvora

            if (next_number == -1):
                print('Kraj inputa, kolekcija je: ', self.kolekcija)
                print()
                break

            self.kolekcija.append(next_number)  # dodaj novi broj u kolekciju
            self.obavijesti(self.kolekcija)     # notify observer da je došao novi broj u kolekciju i da treba napravit akciju na koju je pretplacen
            time.sleep(1)



# main 
if __name__ == "__main__":
    print('Početak inputa s tipkovnice')
    context1 = SlijedBrojeva(TipkovnickiIzvor())
    context1.dodaj_akciju(AkijaZapisiSve())     # pretplata na ovu akciju 
    context1.dodaj_akciju(AkijaIspisiAvg())
    context1.kreni()

    datoteka = 'input.txt'
    print('Početak inputa iz datoteke {}'.format(datoteka))
    context2 = SlijedBrojeva(DatotecniIzvor(datoteka))
    context2.dodaj_akciju(AkcijaIspisiSum())
    context2.dodaj_akciju(AkcijaIspisiMedian())
    context2.kreni()
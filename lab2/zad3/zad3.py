def mymax(iterable, key = lambda x: x):
  # incijaliziraj maksimalni element i maksimalni ključ
  max_x = max_key = None

  # obiđi sve elemente
  for x in iterable:
    # ako je key(x) najveći -> ažuriraj max_x i max_key
    if (max_key is None or key(x) > max_key):
        max_key = key(x)
        max_x = x

  # vrati rezultat
  return max_x

# max value
maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
maxchar = mymax("Suncana strana ulice")
maxstring = mymax([
  "Gle", "malu", "vocku", "poslije", "kise",
  "Puna", "je", "kapi", "pa", "ih", "njise"])
print(maxint)
print(maxchar)
print(maxstring)
print()

# najdulja rijec
najduljaRijec = mymax([
  "Gle", "malu", "vocku", "poslije", "kise",
  "Puna", "je", "kapi", "pa", "ih", "njise"], lambda x: len(x))
print(najduljaRijec)
print()

# najskuplji poizvod
D={'burek':8, 'buhtla':5}
najskuplji_a = mymax(D, lambda x: D.get(x))
najskuplji_b = mymax(D, D.get) # može i ovako
print(najskuplji_a)
print(najskuplji_b)
print()

# poslijednja osoba prema leks. poretku
osobe = [
   ("Emily", "Davis"), 
   ("Emily", "Rodriguez"), 
   ("Liam", "Garcia"),
   ("Sophia", "Brown"), 
   ("Anna", "Williams"),
   ("Sophia", "Garcia"), 
   ("Isabella", "Johnson"),
   ("Michael", "Rodriguez"), 
] 
zadnjaPoImenu = mymax(osobe, lambda x : (x[0], x[1:]))  # leks. poredak: ime, prezime
zadnjaPoPrezimenu = mymax(osobe, lambda x : (x[1:], x[0]))  # leks. poredak: prezime, ime
print(zadnjaPoImenu)
print(zadnjaPoPrezimenu)
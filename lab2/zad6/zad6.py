import ast
import re

"""
VAZNO!!!

Program ne radi za sve slučajeve i oblikovni obrasci nisu do kraja razrađeni:
  - Cell bi trebo biti i subject i observer
  - nije implementirano pronalaženje ciklusa
  - ne radi za A1+3 npr.
  - ne evaluira se sve prije metodom evaluate nego samo celija nad kojom smo pozvali tu metodu 
"""


# observer 
class Cell():
    def __init__(self, name, exp, sheet):
        self.name = name
        self.exp = exp          # sadržaj ćelije
        self.value = None 
        self.refs = []          # lista ćelija koje ovo ćelija referncira
        self.sheet = sheet      # referenca na matičnu tablicu 


    def evaluate(self):
        # ako znamo value varatimo ju
        if self.value != None:
            return self.value
        
        # u listu self.refs spremi sve cells koje refenrenciramo
        self.refs = self.sheet.getrefs(self)

        # rekurzivno za svaku referenciranu ćeliju nađi value
        for ref in self.refs:
            ref.evaluate()

        try:
            self.value = ast.literal_eval(self.exp) # ako je self.exp = "2" onda je ok ali ako je self.exp = "A1" onda nije ok i raisa se ValueError
        except ValueError:
            self.value = sum(ref.value for ref in self.refs)

        return self.value



# subject
class Sheet():
    def __init__(self, rows, cols):
        self.cells = [[Cell("", "", self) for _ in range(cols)] for _ in range(rows)]


    def cell(self, ref):
        row = int(ref[1:]) - 1                  # broj
        col = ord(ref[0].upper()) - ord("A")    # slovo 
        return self.cells[row][col]
    

    def set(self, ref, content):
        cell = self.cell(ref)
        cell.name = ref
        cell.exp = content
        cell.value = None
        cell.refs = []
    

    def getrefs(self, cell):
        pattern = r'[A-Z][0-9]+'    # 1 slovo i nakon toga 1+ brojeva
        refs = re.findall(pattern, cell.exp)
        return [self.cell(ref) for ref in refs] # lista polja koje polje 'cell' referencira
    
    
    def evaluate(self, cell):
        cell.evaluate()




if __name__=="__main__":
    s = Sheet(5, 5)

    print()
    print('--- Primjer 1 --- \n')
    
    s.set('A1','2')
    s.set('A2','5')
    s.set('A3','A1+A2')

    s.evaluate(s.cell('A3'))
    refs = s.getrefs(s.cell('A3'))

    print('sadržaj od A1: ', s.cell('A1').exp)
    print('value od A1: ', s.cell('A1').value, '\n')
    print('sadržaj od A2: ', s.cell('A2').exp)
    print('value od A2: ', s.cell('A2').value, '\n')
    print('sadržaj od A3: ', s.cell('A3').exp)
    print('value od A3: ', s.cell('A3').value, '\n')

    print('A3 referencira polja:')
    for ref in refs:
        print('polje ', ref.name, ' sa sadržajem ', ref.exp, ' i value ', ref.value)
    print()
    print()


    print('--- Primjer 2 --- \n')

    s.set('A1','4')
    s.set('A4','A1+A3')
    s.evaluate(s.cell('A4'))
    refs = s.getrefs(s.cell('A4'))

    print('sadržaj od A1: ', s.cell('A1').exp)
    print('value od A1: ', s.cell('A1').value, '\n')
    print('sadržaj od A4: ', s.cell('A4').exp)
    print('value od A4: ', s.cell('A4').value, '\n')

    print('A4 referencira polja:')
    for ref in refs:
        print('polje ', ref.name, ' sa sadržajem ', ref.exp, ' i value ', ref.value)
    print()
    print()


    print('--- Primjer 3 --- \n')

    # bez expetiona
    try:
        s.set('A1','A3')
        s.evaluate(s.cell('A1'))
        refs = s.getrefs(s.cell('A1'))

        print('sadržaj od A1: ', s.cell('A1').exp)
        print('value od A1: ', s.cell('A1').value, '\n')
        
        print('A1 referencira polja:')
        for ref in refs:
            print('polje ', ref.name, ' sa sadržajem ', ref.exp, ' i value ', ref.value)

    except RuntimeError as e:
        print("Caught exception:",e)
    print()
    print()


    print('--- Primjer 4 --- \n')

    # sa expetionom
    try:
        s.set('A3','A1+A2')
        s.set('A1','A3')
        s.evaluate(s.cell('A1'))
        refs = s.getrefs(s.cell('A1'))

        print('exp od A1: ', s.cell('A1').exp)
        print('value od A1: ', s.cell('A1').value, '\n')
        
        print('A1 referencira polja:')
        for ref in refs:
            print('polje ', ref.name, ' sa sadržajem ', ref.exp, ' i value ', ref.value)

    except RuntimeError as e:
        print("Caught exception:",e)
    print()
    print()
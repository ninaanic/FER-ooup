#include <stdio.h>
#include <stdlib.h>

// [zadano] funkcije koje definiraju ponašanje konkretnih tipova --> virtualne metode 
char const* dogGreet(void){
  return "vau!";
}
char const* dogMenu(void){
  return "kuhanu govedinu";
}
char const* catGreet(void){
  return "mijau!";
}
char const* catMenu(void){
  return "konzerviranu tunjevinu";
}


// deklaracija tipa za pohranjivanje elementa virtaulnih tablica 
// elementi vtable-a su char const*
typedef char const* (*PTRFUN)();

// 2 tablice pokazivača i njihovo inicijaliziranje
PTRFUN DogVTable[2] = {
  (PTRFUN)dogGreet,     // table[0]
  (PTRFUN)dogMenu,      // table[1]
};

PTRFUN CatVTable[2] = {
  (PTRFUN)catGreet,
  (PTRFUN)catMenu,
};



// class Animal
typedef struct {
  char *name;        // i) ptr na ime ljubimca
  PTRFUN *vtable;    // ii) ptr na virtualnu tablicu
} Animal;

void animalPrintGreeting(Animal *p) {
  printf("%s pozdravlja: %s\n", p->name, p->vtable[0]());
};

void animalPrintMenu(Animal *p) {
  printf("%s voli %s\n", p->name, p->vtable[1]());
};



// class Dog
Animal* constructDog(Animal *p, char *name) {
  p->name = name;
  p->vtable = DogVTable;
  return p;
};

Animal* createDog(char* name) {
  Animal* p = (Animal*)malloc(sizeof(Animal)); // alokacija memorije 
  Animal* construct = constructDog(p, name);
  return construct;
};



// class Cat
Animal* constructCat(Animal* p, char* name) {
  p->name = name;
  p->vtable = CatVTable;
  return p;
};

Animal* createCat(char* name) {
  Animal* p = (Animal*)malloc(sizeof(Animal)); // alokacija memorije 
  Animal* construct = constructCat(p, name);
  return construct;
};



// [zadano] test
void testAnimals(void){
  struct Animal* p1=createDog("Hamlet");
  struct Animal* p2=createCat("Ofelija");
  struct Animal* p3=createDog("Polonije");

  animalPrintGreeting(p1);
  animalPrintGreeting(p2);
  animalPrintGreeting(p3);

  animalPrintMenu(p1);
  animalPrintMenu(p2);
  animalPrintMenu(p3);

  free(p1); free(p2); free(p3);
}



// main
int main() {
  testAnimals();
  return 0;
}
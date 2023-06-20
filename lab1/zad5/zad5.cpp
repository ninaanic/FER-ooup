#include <iostream>
#include <stdio.h>
#include <stdlib.h>

using namespace std;

class B {
  public:
    virtual int prva() = 0;
    virtual int druga(int) = 0;
};

class D: public B {
  public:
    virtual int prva() {
      return 42;
    }
    virtual int druga(int x) {
      return prva() + x;
    }
};

typedef int (*PTRFUN1)(void* that);
typedef int (*PTRFUN2)(void* that, int);

void nasaFunkcija(B* pb) {
  void** vTablePointerB = *(void***)pb;

  PTRFUN1 f1 = (PTRFUN1)vTablePointerB[0];
  cout << "f1 vraća: " << f1(pb) << "\n";

  PTRFUN2 f2 = (PTRFUN2)vTablePointerB[1];
  cout << "f2 vraća: " << f2(pb, 5) << "\n";
}

int main() {
    B* pb = new D;
    nasaFunkcija(pb);
    return 0;
}
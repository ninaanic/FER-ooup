#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef double (*PTRFUN)(void *, double); // PTRFUN je pointer na fju koja prima (void *, double) i vraća double

// class Unary_Function
typedef struct {
    PTRFUN *vtable;
    int lower_bound;
    int upper_bound;
} Unary_Function;

double negative_value_at(Unary_Function* obj, double x) {
    PTRFUN funptr = (PTRFUN)obj->vtable[0]; // value_at fja od obj 
    return -funptr(obj, x);
};

void tabulate(Unary_Function *obj) {
    PTRFUN funptr = (PTRFUN)obj->vtable[0]; // value_at fja od obj

    for(int x = obj->lower_bound; x <= obj->upper_bound; x++) {
        printf("f(%d)=%lf\n", x, funptr(obj, x));
    };
};

bool same_functions_for_ints(Unary_Function* f1, Unary_Function* f2, double tolerance) {
    if (f1->lower_bound != f2->lower_bound) return false;
    if (f1->upper_bound != f2->upper_bound) return false;

    PTRFUN funptr_f1 = (PTRFUN)f1->vtable[0];
    PTRFUN funptr_f2 = (PTRFUN)f2->vtable[0];

    for(int x = f1->lower_bound; x <= f1->upper_bound; x++) {
        double delta = funptr_f1(f1, x) - funptr_f2(f2, x);
        if (delta < 0) delta = -delta;
        if (delta > tolerance) return false;
    }
    return true;
};

PTRFUN UnaryVTable[2] = {
    (PTRFUN)0,                      // value_at, inicijalno 0 
    (PTRFUN)negative_value_at,      // - value_at
};

Unary_Function* constructor_unary(int lb, int ub) {
    Unary_Function* obj = (Unary_Function*)malloc(sizeof(Unary_Function));
    obj->vtable = UnaryVTable;
    obj->lower_bound = lb;
    obj->upper_bound = ub;
    return obj;
}



// class Square
typedef struct {
    PTRFUN *vtable;
    int lower_bound;
    int upper_bound;
} Square;

double value_at_square(Square* obj, double x) {
    return x*x;
}

PTRFUN SquareVTable[2] = {
    (PTRFUN)value_at_square,
    (PTRFUN)negative_value_at,
};

Square* constructor_square(int lb, int ub) {
    Square* obj = (Square*)malloc(sizeof(Square));
    obj->vtable = SquareVTable;
    obj->lower_bound = lb;
    obj->upper_bound = ub;
    return obj;
}


// class Linear
typedef struct {
    PTRFUN *vtable;
    int lower_bound;
    int upper_bound;
    double a;
    double b;
} Linear;

double value_at_linear(Linear* obj, double x) {
    return (obj->a) * x + (obj->b);
}

PTRFUN LinearVTable[2] = {
    (PTRFUN)value_at_linear,
    (PTRFUN)negative_value_at,
};

Linear* constructor_linear(int lb, int ub, double a_coef, double b_coef) {
    Linear* obj = (Linear*)malloc(sizeof(Linear));
    obj->vtable = LinearVTable;
    obj->lower_bound = lb;
    obj->upper_bound = ub;
    obj->a = a_coef;
    obj->b = b_coef;
    return obj;
}


// class main
int main() {
    Square* s = constructor_square(-2, 2);
    Unary_Function* f1 = (Unary_Function*)s; // cast s-a u pointer Unary_Function-a
    tabulate(f1);
    Linear* l = constructor_linear(-2, 2, 5, -2);
    Unary_Function* f2 = (Unary_Function*)l;
    tabulate(f2);
    printf("f1==f2: %s\n", same_functions_for_ints(f1, f2, 1E-6) ? "DA" : "NE");
    PTRFUN funptr = (PTRFUN)f2->vtable[1]; // negative_value_at
    printf("neg_val f2(1) = %lf\n", funptr(f2, 1.0));
    free(f1);
    free(f2);
    return 0;
}


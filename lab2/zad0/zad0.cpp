#include <iostream>
#include <assert.h>
#include <stdlib.h>

struct Point{
    int x; int y;
};
struct Shape{
    enum EType {circle, square, rhomb};
    EType type_;
};
struct Circle{
     Shape::EType type_;
     double radius_;
     Point center_;
};
struct Square{
     Shape::EType type_;
     double side_;
     Point center_;
};
// Rhomb
struct Rhomb{
     Shape::EType type_;
     double side_;
     Point center_;
};
void drawSquare(struct Square*){
    std::cerr <<"in drawSquare\n";
}
void drawCircle(struct Circle*){
    std::cerr <<"in drawCircle\n";
}
// drawRhomb
void drawRhomb(struct Rhomb*){
    std::cerr <<"in drawRhomb\n";
}

void drawShapes(Shape** shapes, int n){
    for (int i=0; i<n; ++i){
        struct Shape* s = shapes[i];
        switch (s->type_){
            case Shape::square:
                drawSquare((struct Square*)s);
                break;
            case Shape::circle:
                drawCircle((struct Circle*)s);
                break;
            // drawRhomb
            case Shape::rhomb:
                drawRhomb((struct Rhomb*)s);
                break;
            default:
                assert(0); 
                exit(0);
        }
    }
}


// moveShape
void moveSquare(struct Square* sq, int x, int y){
    std::cerr << "in moveSquare\n";
    std::cerr << "center od square before move: x = " << sq->center_.x << " y = " <<  sq->center_.y << "\n";
    sq->center_.x = sq->center_.x + x;
    sq->center_.y = sq->center_.y + y;
    std::cerr << "center od square after move: x = " << sq->center_.x << " y = " <<  sq->center_.y << "\n";
}
void moveCircle(struct Circle* c, int x, int y){
    std::cerr << "in moveCircle\n";
    std::cerr << "center od circle before move: x = " << c->center_.x << " y = " <<  c->center_.y << "\n";
    c->center_.x = c->center_.x + x;
    c->center_.y = c->center_.y + y;
    std::cerr << "center od circle after move: x = " << c->center_.x << " y = " <<  c->center_.y << "\n";
}
void moveShapes(Shape** shapes, int n, int x, int y) {
    for (int i=0; i<n; ++i){
        struct Shape* s = shapes[i];
        switch (s->type_){
            case Shape::square: {
                struct Square* sq = (struct Square*)s;
                moveSquare(sq, x, y);
                break;
            }
            case Shape::circle: {
                struct Circle* c = (struct Circle*)s;
                moveCircle(c, x, y);
                break;
            }
            default: {
                assert(0); 
                exit(0);
            }
        }
    }
}

    

int main() {
    Shape* shapes[5];

    shapes[0]=(Shape*)new Circle;
    shapes[0]->type_=Shape::circle;
    shapes[1]=(Shape*)new Square;
    shapes[1]->type_=Shape::square;
    shapes[2]=(Shape*)new Square;
    shapes[2]->type_=Shape::square;
    shapes[3]=(Shape*)new Circle;
    shapes[3]->type_=Shape::circle;
    // Rhomb
    shapes[4]=(Shape*)new Rhomb;
    shapes[4]->type_=Shape::rhomb;

    drawShapes(shapes, 5);

    std::cout << "\n";

    // moveShape
    Shape* shapes2[3];

    Circle* c = new Circle;
    c->type_ = Shape::circle;
    c->radius_ = 2.0;
    c->center_.x = 1;
    c->center_.y = 1;
    shapes2[0] = (Shape*)c;

    Square* sq = new Square;
    sq->type_ = Shape::square;
    sq->side_ = 3.0;
    sq->center_.x = 0;
    sq->center_.y = 0;
    shapes2[1] = (Shape*)sq;
    
    // moveShapes pukne jer nije ništa definirano za romb
    Rhomb* r = new Rhomb;
    r->type_ = Shape::rhomb;
    r->side_ = 2.0;
    r->center_.x = 2;
    r->center_.y = 2;
    shapes2[2] = (Shape*)r;
    
    moveShapes(shapes2, 3, 1, 3); // x = 1, y = 3 pomak
}
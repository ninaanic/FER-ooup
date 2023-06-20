#include <iostream>
//using namespace std;

class Base{
    public:
        //if in doubt, google "pure virtual"
            // A pure virtual function (or abstract function) in C++ is a virtual function for which we can have implementation, 
            // But we must override that function in the derived class, otherwise the derived class will also become abstract class 
        virtual void set(int x)=0;
        virtual int get()=0;
};

class CoolClass: public Base{
    public:
        virtual void set(int x) {
            x_=x;
        };
        virtual int get() {
            return x_;
        };
    private:
        int x_;
};

class PlainOldClass{
    public:
        void set(int x) {
            x_=x;
        };
        int get() {
            return x_;
        };
    private:
        int x_;
};

int main(){
    PlainOldClass poc;
    Base* pb = new CoolClass;
    poc.set(42);
    pb->set(42);
    return 0;
}  


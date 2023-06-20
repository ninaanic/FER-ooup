#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <vector>
#include <set>
#include <string.h>

using namespace std;

// kriterijska fja za usporedbu cijelih brojeva
int gt_int(int a, int b) {
    if (a > b) return 1;
    return 0;
}

// kriterijska fja za usporedbu znakova
int gt_char(char a, char b) {
    if (a > b) return 1;
    return 0;
}

// kriterijska fja za usporedbu stringova
int gt_str(string a, string b) {
    if (a.compare(b) >= 0) return 1;
    return 0;
}

template <typename Iterator, typename Predicate>
Iterator mymax(Iterator first, Iterator last, Predicate pred) {
    Iterator poc_elem = first;
    Iterator max_elem = first;

    while (first != last) {
        // u pred poslat derefernecirane objekte
        if (pred(*first, *max_elem) > 0) {
            max_elem = first;
        }
        ++first;
    }
    return max_elem;
}


int main(){
    // int 
    int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
    const int* maxint = mymax(&arr_int[0], &arr_int[sizeof(arr_int)/sizeof(*arr_int)], gt_int);
    cout << "Max value in arrat of ints: " << *maxint << "\n";

    // char
    string str = "Suncana strana ulice";
    char maxchar = *mymax(str.begin(), str.end(), gt_char); // dereferenciranje 
    cout << "Max value in string: " << maxchar << "\n";

    // string
    string arr_str[] = {
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"
    };
    string* maxstring = mymax(&arr_str[0], &arr_str[sizeof(arr_str)/sizeof(*arr_str)], gt_str);
    cout << "Max value in array of strings: " << *maxstring << "\n";

    // vector
    vector<int> vec;
    vec.push_back(1);
    vec.push_back(3);
    vec.push_back(5);
    vec.push_back(7);
    vec.push_back(4);
    vec.push_back(6);
    vec.push_back(9);
    vec.push_back(2);
    vec.push_back(0);
    vector<int>::iterator maxvec = mymax(vec.begin(), vec.end(), gt_int);
    cout << "Max value in vector: " << *maxvec << "\n";

    // set
    set<char> val;
    val.insert('a');
    val.insert('f');
    val.insert('Z');
    val.insert('k');
    val.insert('v');
    val.insert('W');
    val.insert('u');
    val.insert('l');
    set<char>::iterator maxval = mymax(val.begin(), val.end(), gt_char);
    cout << "Max value in set: " << *maxval << "\n";
}

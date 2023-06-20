#include <stdio.h>
#include <string.h>

// kriterijska fja za usporedbu cijelih brojeva
int gt_int(const void *a, const void *b) {
    const int *a_int = (const int *)a;  // cast void* -> int*
    const int *b_int = (const int *)b;

    if (*a_int > *b_int) return 1;
    return 0;
}

// kriterijska fja za usporedbu znakova, a > A
int gt_char(const void *a, const void *b) {
    const char *a_char = (const char *)a;
    const char *b_char = (const char *)b;

    if (*a_char > *b_char) return 1;
    return 0;
}

// kriterijska fja za usporedbu znakovnih nizova
int gt_str(const void *a, const void *b) {
    const char *a_str = *(const char **)a;
    const char *b_str = *(const char **)b;

    return strcmp(a_str, b_str); // ako se jednaki vraca 0, ako je a_str > b_str po ASCII vraca > 0, inace < 0 
}


// deklaracija fje mymax - vraca const void* pointer na najveći elem polja --> NBP
const void* mymax(
    const void *base,   // pointer na prvi element polja koje se pretrazuje
    size_t nmemb,       // broj elemenata polja
    size_t size,        // velicina elem polja
    int (*compar)(const void *, const void *)   // function pointer na fju koja radi usporedbu 2 elem polja
) {
    const void *poc_elem = base;
    const void *max_elem = base;

    // 0. elem već imamo u poc_elem, idemo od 1.
    for (size_t i = 1; i < nmemb; i++) {
        const void *elem = poc_elem + i*size;
        if (compar(elem, max_elem) > 0) {
            max_elem = elem;
        }
    }
    return max_elem;
};

int main() {
    int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
    size_t arr_int_nmemb = sizeof(arr_int) / sizeof(arr_int[0]);    // broj elemenata polja
    size_t arr_int_size = sizeof(int);                              // velicina elem polja
    const void *ptr_to_max_elem_arr_int = mymax(arr_int, arr_int_nmemb, arr_int_size, gt_int);
    int max_elem_value_arr_int = *(int *)ptr_to_max_elem_arr_int; // cast u int* i dereferenciranje 
    printf("Max value in arr_int: %d\n", max_elem_value_arr_int);


    char arr_char[]="Suncana strana ulice";
    size_t arr_char_nmemb = sizeof(arr_char) / sizeof(arr_char[0]);
    size_t arr_char_size = sizeof(char);
    const void *ptr_to_max_elem_arr_char = mymax(arr_char, arr_char_nmemb, arr_char_size, gt_char);
    char max_elem_value_arr_char = *(char *)ptr_to_max_elem_arr_char;
    printf("Max value in arr_char: %c\n", max_elem_value_arr_char);

    const char* arr_str[] = {
    "Gle", "malu", "vocku", "poslije", "kise",
    "Puna", "je", "kapi", "pa", "ih", "njise"
    };
    size_t arr_str_nmemb = sizeof(arr_str) / sizeof(arr_str[0]);
    size_t arr_str_size = sizeof(const char *);
    const void *ptr_to_max_elem_arr_str = mymax(arr_str, arr_str_nmemb, arr_str_size, gt_str);
    const char* max_elem_value_arr_str = *(const char **)ptr_to_max_elem_arr_str;
    printf("Max value in arr_str: %s\n", max_elem_value_arr_str);

    return 0;
}
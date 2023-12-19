#include "vector.h"

int main() // Here is a start:

{

        Vector<int> intVec{1,3,5,7,9};

        Vector<double> doubleVec{1.5,2.5,3.5,4.5};

        Vector<int> iv{intVec};

        Vector<double> dv{doubleVec};

        cout << "intVec" << intVec << endl;

// "intVec(1, 3, 5, 7, 9)"

        cout << "iv" << iv << endl;

// "iv(1, 3, 5, 7, 9)"

        cout << "doubleVec" << doubleVec << endl;

// "doubleVec(1.5, 2.5, 3.5, 4.5)"

cout << "dv" << dv << endl;


// "dv(1.5, 2.5, 3.5, 4.5)"


        // add at least one test case for each method defined in Vector

        // test constructor using size
        // test size()

        Vector<int> size10Vec(10);
        cout << size10Vec.size() << endl;

// "10"


        // test copy constructor
        // Already used copy constructor to make iv and dv

        cout << (iv == intVec) << endl;
        cout << (dv == doubleVec) << endl;

// "1"
// "1"

        
        // test operator[] (modification)

        iv[3] = 100;
        cout << "iv" << iv << endl;

// "iv(1, 3, 5, 100, 9)"


        // test operator[] (no modification)

        double elemAtInd1 = doubleVec[1];
        cout << elemAtInd1 << endl;
    
// "2.5"


        Vector<int> intVec2{2,4,6};
        Vector<double> doubleVec2{5.5, 6.5};
        Vector<int> emptyVec{};


        // test operator * (dot product)

        int dotProductInt = intVec * intVec2;
        double dotProductDouble = dv * doubleVec2;
        int zero = intVec * emptyVec;
        cout << dotProductInt << endl;
        cout << dotProductDouble << endl;
        cout << zero << endl;

// "44"
// "24.5"
// "0"


        // test operator + (add vectors)

        Vector<int> addedVecInt = intVec + intVec2;
        Vector<double> addedVecDouble = doubleVec + doubleVec2;
        Vector<int> addedEmptyVec = intVec + emptyVec;
        cout << addedVecInt << endl;
        cout << addedVecDouble << endl;
        cout << addedEmptyVec << endl;

// "(3, 7, 11, 7, 9)"
// "(7, 9, 3.5, 4.5)"
// "(1, 3, 5, 7, 9)"


        // test operator =

        Vector<int> testVec1{10,10,10};
        testVec1 = intVec;
        cout << testVec1 << endl;

// "(1, 3, 5, 7, 9)"


        // test operator ==

        Vector<int> testVec2{1,3,5};
        cout << (testVec2==intVec) << endl;
        Vector<int> testVec3{1,3,5,7,8};
        cout << (testVec3==intVec) << endl; 
        Vector<int> testVec4{1,3,5,7,9};
        cout << (testVec4==intVec) << endl; 

// "0"
// "0"
// "1"


        // test operator !=

        cout << (testVec2!=intVec) << endl;
        cout << (testVec3!=intVec) << endl; 
        cout << (testVec4!=intVec) << endl; 

// "1"
// "1"
// "0"


        // test operator * (multiplication by scalar)

        Vector<int> testVec5 = 2 * intVec;
        Vector<double> testVec6 = 3 * doubleVec;
        cout << testVec5 << endl;
        cout << testVec6 << endl;

// "(2, 6, 10, 14, 18)"
// "(4.5, 7.5, 10.5, 13.5)"


        // test operator + (addition by scalar)

        Vector<int> testVec7 = 10 + intVec;
        Vector<double> testVec8 = 5 + doubleVec;
        cout << testVec7 << endl;
        cout << testVec8 << endl;

// "(11, 13, 15, 17, 19)"
// "(6.5, 7.5, 8.5, 9.5)"


        // test operator <<
        // already tested from printing each test case

        // test destructor
        // Running valgrind should output "All heap blocks were freed -- no leaks are possible"

return 0;

}
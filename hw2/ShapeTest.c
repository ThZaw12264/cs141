#include <malloc.h>
#include <math.h>


typedef double (*double_method_type)(void *);
typedef void (*void_method_type)(void *);

typedef union {
    double_method_type double_method;
    void_method_type void_method;
} VirtualTableEntry;

typedef VirtualTableEntry* VTableType;


typedef struct {
    VTableType VPointer;
    const char* name;
} Shape;

void Shape_print(Shape* _this) {
    printf("%s() : 0\n", _this->name);
}

void Shape_draw(Shape* _this) {
   printf("N/A\n"); 
}

double Shape_area(Shape* _this) {
    return 0;
}

VirtualTableEntry Shape_VTable[] = {
    {.void_method = (void_method_type)&Shape_print},
    {.void_method = (void_method_type)&Shape_draw},
    {.double_method = (double_method_type)&Shape_area}
};

Shape* Shape_Shape(Shape* _this, const char* name) {
    _this->VPointer = Shape_VTable;
    _this->name = name;
    return _this;
}


typedef struct {
    VTableType VPointer;
    const char* name;
    int radius;
} Circle;

void Circle_print(Circle* _this) {
    printf("%s(%d) : %f\n", _this->name, _this->radius, _this->VPointer[2].double_method(_this));
}

void Circle_draw(Circle* _this) {
    printf("    ***    \n  *     *  \n*         *\n*         *\n  *     *  \n    ***    \n");
}

double Circle_area(Circle* _this) {
    return M_PI * _this->radius * _this->radius;
}

VirtualTableEntry Circle_VTable[] {
    {.void_method = (void_method_type)&Circle_print},
    {.void_method = (void_method_type)&Circle_draw},
    {.double_method = (double_method_type)&Circle_area}
};

Circle* Circle_Circle(Circle* _this, const char* name, const int radius) {
    Shape_Shape((Shape *)_this, name);
    _this->VPointer = Circle_VTable;
    _this->radius = radius;
    return _this;
}


typedef struct {
    VTableType VPointer;
    const char* name;
    int base;
    int height;
} Triangle;

void Triangle_print(Triangle* _this) {
    printf("%s(%d, %d) : %f\n", _this->name, _this->base, _this->height, _this->VPointer[2].double_method(_this));
}

void Triangle_draw(Triangle* _this) {
    printf("   *   \n  * *  \n *   * \n*******\n");
}

double Triangle_area(Triangle* _this) {
    return 0.5 * _this->base * _this->height;
}

VirtualTableEntry Triangle_VTable[] {
    {.void_method = (void_method_type)&Triangle_print},
    {.void_method = (void_method_type)&Triangle_draw},
    {.double_method = (double_method_type)&Triangle_area}
};

Triangle* Triangle_Triangle(Triangle* _this, const char* name, const int base, const int height) {
    Shape_Shape((Shape*)_this, name);
    _this->VPointer = Triangle_VTable;
    _this->base = base;
    _this->height = height;
    return _this;
}


typedef struct {
    VTableType VPointer;
    const char* name;
    int base;
} Square;

void Square_print(Square* _this) {
    printf("%s(%d) : %f\n", _this->name, _this->base, _this->VPointer[2].double_method(_this));
}

void Square_draw(Square* _this) {
    printf("**********\n*        *\n*        *\n*        *\n**********\n");
}

double Square_area(Square* _this) {
    return _this->base * _this->base;
}

VirtualTableEntry Square_VTable[] = {
    {.void_method = (void_method_type)&Square_print},
    {.void_method = (void_method_type)&Square_draw},
    {.double_method = (double_method_type)&Square_area}
};

Square* Square_Square(Square* _this, const char* name, const int base) {
    Shape_Shape((Shape *)_this, name);
    _this->VPointer = Square_VTable;
    _this->base = base;
    return _this;
}


typedef struct {
    VTableType VPointer;
    const char* name;
    int base;
    int height;
} Rectangle;

void Rectangle_print(Rectangle* _this) {
    printf("%s(%d, %d) : %f\n", _this->name, _this->base, _this->height, _this->VPointer[2].double_method(_this));
}

void Rectangle_draw(Rectangle* _this) {
    printf("******\n*    *\n*    *\n*    *\n*    *\n*    *\n******\n");
}

double Rectangle_area(Rectangle* _this) {
    return _this->base * _this->height;
}

VirtualTableEntry Rectangle_VTable[] = {
    {.void_method = (void_method_type)&Rectangle_print},
    {.void_method = (void_method_type)&Rectangle_draw},
    {.double_method = (double_method_type)&Rectangle_area}
};

Rectangle* Rectangle_Rectangle(Rectangle* _this, const char* name, const int base, const int height) {
    Square_Square((Square *)_this, name, base);
    _this->VPointer = Rectangle_VTable;
    _this->height = height;
    return _this;
}


void printAll(Shape* shapesArray[], int len) {
    for (int i = 0; i < len; i++) {
        shapesArray[i]->VPointer[0].void_method(shapesArray[i]);
    }
}

void drawAll(Shape* shapesArray[], int len) {
    for (int i = 0; i < len; i++) {
        shapesArray[i]->VPointer[1].void_method(shapesArray[i]);
    }
}

double totalArea(Shape* shapesArray[], int len) {
    double sum = 0;
    for (int i = 0; i < len; i++) {
        sum += shapesArray[i]->VPointer[2].double_method(shapesArray[i]);
    }
    return sum;
}

void deleteShapes(Shape* shapesArray[], int len) {
    for (int i = 0; i < len; i++) {
        free(shapesArray[i]);
    }
}


int main (int argc, char* argv[]) {
    int arg1 = strtol(argv[1], NULL, 10);
    int arg2 = strtol(argv[2], NULL, 10);
    Shape* shapes[] = {
        (Shape *) Triangle_Triangle((Triangle *) malloc(sizeof(Triangle)), "FirstTriangle", arg1, arg2),
        (Shape *) Triangle_Triangle((Triangle *) malloc(sizeof(Triangle)), "SecondTriangle", arg1 - 1, arg2 - 1),
        (Shape *) Circle_Circle((Circle *) malloc(sizeof(Circle)), "FirstCircle", arg1),
        (Shape *) Circle_Circle((Circle *) malloc(sizeof(Circle)), "SecondCircle", arg1 - 1),
        (Shape *) Square_Square((Square *) malloc(sizeof(Square)), "FirstSquare", arg1),
        (Shape *) Square_Square((Square *) malloc(sizeof(Square)), "SecondSquare", arg1 - 1),
        (Shape *) Rectangle_Rectangle((Rectangle *) malloc(sizeof(Rectangle)), "FirstRectangle", arg1, arg2),
        (Shape *) Rectangle_Rectangle((Rectangle *) malloc(sizeof(Rectangle)), "SecondRectangle", arg1 - 1, arg2 - 1),
    };
    printAll(shapes, 8);
    drawAll(shapes, 8);
    printf("Total : %f\n", totalArea(shapes, 8));
    deleteShapes(shapes, 8);
    return 0;
}

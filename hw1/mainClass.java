import java.io.*;
import java.lang.Math;

public class mainClass {

    public static void main(String args[]) {
        Picture picture = new Picture();

        picture.add(new Triangle("FirstTriangle", Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        picture.add(new Triangle("SecondTriangle", Integer.parseInt(args[0])-1, Integer.parseInt(args[1])-1));
        picture.add(new Circle("FirstCircle", Integer.parseInt(args[0])));
        picture.add(new Circle("SecondCircle", Integer.parseInt(args[0])-1));
        picture.add(new Square("FirstSquare", Integer.parseInt(args[0])));
        picture.add(new Square("SecondSquare", Integer.parseInt(args[0])-1));
        picture.add(new Rectangle("FirstRectangle", Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        picture.add(new Rectangle("SecondRectangle", Integer.parseInt(args[0])-1, Integer.parseInt(args[1])-1));

        picture.printAll();
        picture.drawAll();
        System.out.println("Total : " + picture.totalArea());
    }
}

abstract class Shape {

    String name;

    Shape(String newName) {
        name = newName;
    }

    abstract void print();
    abstract void draw();
    abstract double area();
}

class Circle extends Shape {

    int radius;
    
    Circle(String newName, int newRadius) {
        super(newName);
        radius = newRadius;
    }

    void print() {
        System.out.println(String.format("%s(%d) : %f", name, radius, this.area()));
    }

    void draw() {
        System.out.println("    ***    \n  *     *  \n*         *\n*         *\n  *     *  \n    ***    ");
    }

    double area() {
        return Math.PI * radius * radius;
    }
}

class Triangle extends Shape {

    int base;
    int height;

    Triangle(String newName, int newBase, int newHeight) {
        super(newName);
        base = newBase;
        height = newHeight;
    }

    void print() {
        System.out.println(String.format("%s(%d, %d) : %f", name, base, height, this.area()));
    }

    void draw() {
        System.out.println("   *   \n  * *  \n *   * \n*******");
    }

    double area() {
        return 0.5 * base * height;
    }
}

class Square extends Shape {
    
    int base;

    Square(String newName, int newBase) {
        super(newName);
        base = newBase;
    }

    void print() {
        System.out.println(String.format("%s(%d) : %f", name, base, this.area()));
    }

    void draw() {
        System.out.println("**********\n*        *\n*        *\n*        *\n**********");
    }

    double area() {
        return base * base;
    }
}

class Rectangle extends Square {
    
    int height;

    Rectangle(String newName, int newBase, int newHeight) {
        super(newName, newBase);
        height = newHeight;
    }

    void print() {
        System.out.println(String.format("%s(%d, %d) : %f", name, base, height, this.area()));
    }

    void draw() {
        System.out.println("******\n*    *\n*    *\n*    *\n*    *\n*    *\n******");
    }

    @Override
    double area() {
        return base * height;
    }
}

class ListNode {
    
    Shape shape;
    ListNode next;

    ListNode(Shape newShape, ListNode newNext) {
        shape = newShape;
        next = newNext;
    }
}

class Picture {
    
    ListNode head;

    Picture() {
        head = null;
    }

    void add(Shape sh) {
        head = new ListNode(sh, head);
    }

    void printAll() {
        for (ListNode l = head; l != null; l = l.next) {
            l.shape.print();
        }
    }

    void drawAll() {
        for (ListNode l = head; l != null; l = l.next) {
            l.shape.draw();
        }
    }

    double totalArea() {
        double sum = 0;
        for (ListNode l = head; l != null; l = l.next) {
            sum += l.shape.area();
        }
        return sum;
    }
}

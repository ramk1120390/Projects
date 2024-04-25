package com.Network.Network.java8.Lamda;


import java.awt.*;

interface Draw {
    public void draw();
}

class Shape implements Draw {
    public void draw() {
        System.out.println("Shape method Called");
    }
}

class Circle implements Draw {
    public void draw() {
        System.out.println("Circle method Called");
    }
}

public class Lamdaoperation2 {
    public static void main(String[] args) {
        Draw shape = () -> System.out.println("Shapemethod called without lambda");
        shape.draw();
        Draw shape1 = new Shape();
        shape1.draw();

    }
}

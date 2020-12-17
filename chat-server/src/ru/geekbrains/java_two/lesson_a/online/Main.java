package ru.geekbrains.java_two.lesson_a.online;

public class Main {
    private static class Animal {
        String name;

        void move(int i) {

        }

        void move() {
            System.out.println("walks on paws");
        }

    }

    private static class Cat extends Animal {
        void voice() {
            System.out.println("Meow");
        }

        @Override
        void move() {
            System.out.println("My own moves!");
        }

        @Override
        void move(int i) {
            super.move(i);
        }
    }

    public static void main(String[] args) {
        Cat c = new Cat();

        Animal[] zoo = {c};
        for (int i = 0; i < zoo.length; i++) {
            zoo[i].move();
            if (zoo[i] instanceof Cat) {
                ((Cat) zoo[i]).voice();
            }
        }

    }
}

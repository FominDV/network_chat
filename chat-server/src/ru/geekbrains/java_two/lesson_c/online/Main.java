package ru.geekbrains.java_two.lesson_c.online;

import java.util.*;

public class Main {

    enum Colors {
        RED("#FF0000"), // static Colors RED = new Colors(RED, "#FF0000");
        BLUE("#0000FF"),
        GREEN("#00FF00"),
        BLACK("#000000"),
        WHITE("#FFFFFF");

        private final String code;

        Colors(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    static class Cat {
        Colors color;

        public String getColor() {
            return color.getCode();
        }
    }

    private static class Box implements Comparable<Box> {
        private int width;
        private int height;
        private int marker;

        public Box(int width, int height, int marker) {
            this.width = width;
            this.height = height;
            this.marker = marker;
        }

        public Box(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("Box(%d, %d) %d", width, height, marker);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Box)) return false;
            Box box = (Box) obj;
            return this.width == box.width && this.height == box.height;
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, height);
        }

        private int square() {
            return width * height;
        }

        @Override
        public int compareTo(Box box) {
//            if (box == this) return 0;
//            int sq = square();
//            int boxSq = box.square();
//            if (sq < boxSq) {
//                return -1;
//            } else if (sq > boxSq) {
//                return 1;
//            } else {
//                return 0;
//            }
//
            return (box == this) ? 0 : square() - box.square();
        }
    }

    public static void main(String[] args) {
        //Mapping
        HashMap<String, Integer> map = new HashMap<>();
        map.put("January", 1);
        map.put("February", 2);
        map.put("March", 3);
        map.put("January", 4);

        System.out.println(map);
        System.out.println(map.get("January"));

        for (String s : map.keySet()) {
            System.out.println(s + " : " + map.get(s));
        }

        Set<Map.Entry<String, Integer>> set = map.entrySet();
        Iterator<Map.Entry<String, Integer>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

    }

    private static void treeExample() {
        TreeSet<Box> tree = new TreeSet<>();
        Box b0 = new Box(1, 1);
        Box b1 = new Box(2, 2);
        Box b2 = new Box(3, 3);
        Box b3 = new Box(4, 4);
        Box b4 = new Box(1, 1);
        tree.addAll(Arrays.asList(b0, b1, b2, b3, b4));
        System.out.println(tree);
    }

    private static void hashSetExample() {
        Set<String> set = new LinkedHashSet<>();
        set.add("January");
        set.add("February");
        set.add("March");
        System.out.println(set);
        set.add("March");
        System.out.println(set);

        Box b0 = new Box(1, 1, 1);
        Box b1 = new Box(2, 2, 2);
        Box b2 = new Box(3, 3, 3);
        Box b3 = new Box(4, 4, 4);
        Box b4 = new Box(1, 1, 5);
        Set<Box> set1 = new HashSet<>();
        set1.add(b0);
        set1.add(b1);
        set1.add(b2);
        set1.add(b3);
        set1.add(b4);
        System.out.println(set1);
    }

    private static void arrayListExamples() {
        LinkedList<String> list = new LinkedList<>();
        list.add("January");
        list.add("February");
        list.add("March");
        String s = "Ma";
        s += "rch";
        System.out.println(list);
        System.out.println(list.contains(s));
        list.remove("January");

        Box b0 = new Box(1, 1);
        Box b1 = new Box(2, 2);
        Box b2 = new Box(3, 3);
        Box b3 = new Box(4, 4);
        Box b4 = new Box(1, 1);
        ArrayList<Box> list1 = new ArrayList<>();
        list1.add(b0);
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);

        System.out.println(list1.toString());
        System.out.println(list1.contains(b4));

        for (int i = 0; i < list1.size(); i++) {
            System.out.print(list1.get(i) + " ");
        }
        System.out.println();

        for (Box b : list1) {
            System.out.print(b + " ");
        }
        System.out.println();

        Iterator<Box> iter = list1.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }
        System.out.println();
    }

    private static void enumsAndWrap() {
        Cat c = new Cat();
        c.color = Colors.RED;
        System.out.println("Cat's color is " + c.getColor());

        Colors[] colors = Colors.values();
        for (Colors color : colors) {
            System.out.print(color + ": ");
            System.out.println(color.getCode());
        }

        Character c0 = 'A';
        Integer i1 = 11; // immutable
        Integer i2 = 10; // do fun things at home))
        Byte b;
        Short s;
        Long l;
        Boolean boo;
        Float f;
        Double d;


        System.out.println(i1);
    }
}

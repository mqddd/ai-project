package com.example;

import java.util.*;

public class Test {


    public static void main(String[] args) {

        int[][] a = new int[][]{{1,2,3}, {2, 3, 5}};
        System.out.println(a.length);
        System.out.println(a[0].length);

        Set<Student> studentSet = new HashSet<>();
        Student student1 = new Student(10, "al0i", "aa");
        Student student2 = new Student(10, "ali", "aa");
        studentSet.add(student1);
        studentSet.add(student2);
//        System.out.println(studentSet.contains(new Student(100, "ali", "aa")));
        for (Student student : studentSet) {
//            System.out.println(student.name);
        }

        PriorityQueue<Student> students = new PriorityQueue<>();
        Student s1 = new Student(10, "m0", "aa");
        Student s2 = new Student(100, "m1", "12");
        Student s3 = new Student(1000, "m2", "22");
        students.add(s1);
        students.add(s2);
        students.add(s3);

        students.forEach(student -> System.out.println(student.score));

        System.out.println(students.contains(new Student(110, "m0", "123")));
//        System.out.println(students.poll().score);
//        System.out.println(students.poll().score);
//        System.out.println(students.poll().score);
    }
}

class Student implements Comparable<Student> {
    int score;
    String name;
    String name2;
    Student(int score, String name, String name2) {
        this.score = score;
        this.name = name;
        this.name2 = name2;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(score);
    }

    @Override
    public boolean equals(Object obj) {
        return ((Student) obj).name.equals(this.name);
    }

    @Override
    public int compareTo(Student student) {
        return Integer.compare(this.score, student.score);
    }
}

package com.cybertek.controller.controller;

import com.cybertek.controller.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ExamController {

    private static List<Student> students = new ArrayList<>();

    static {
        Student student1 = new Student(1, "Liridon", 1993);
        Student student2 = new Student(2, "Halil", 1995);
        Student student3 = new Student(3, "Smith", 1990);
        Student student4 = new Student(4, "John", 1991);
        Student student5 = new Student(5, "Donadoni", 1994);
        Student student6 = new Student(6, "Dona", 1992);

        students.addAll(Arrays.asList(student1, student2, student3, student4, student5, student6));

    }

    /**
     * Read all data.
     * @return
     */
    @GetMapping("/read-all")
    public String readAll(Model model) {
        model.addAttribute("students", students);
        model.addAttribute("message", "Students are retrieving with success");
        return "/";
    }

    /**
     * Read by id using path variable
     * record/id
     * @return
     */
    @GetMapping("read/{id}")
    public String readById(@PathVariable("id") Integer id, Model model) {
        Student foundStudent = students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);

        model.addAttribute("student", foundStudent);
        model.addAttribute("message", "Record is retrieving with success");
        return "/";
    }

    /**
     * Read by id using requestParam annotanion.
     * /record?id=1
     * @param id
     * @param model
     * @return current student.
     */
    @GetMapping("/read-record")
    public String readById2(@RequestParam("id") Integer id, Model model) {
        Student foundStudent = students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);

        model.addAttribute("student", foundStudent);
        model.addAttribute("message", "Record is retrieving with success");
        return "/";
    }

    /**
     * Create student, check if exist before creating.
     * @param student student
     * @param model ui model to gather data in UI.
     * @return
     * @throws Exception exception if student already exists.
     */
    public String create(@ModelAttribute("student") Student student, Model model) throws Exception{
        if(checkIfStudentExist(student.getName()) != null) {
            throw new Exception("This student already exists");
        }
        students.add(student);
        model.addAttribute("Student has been created with success");
        return "redirect:/";
    }

    /**
     * Update student. Check if student exist before updating it.
     * @param student student
     * @param model ui model to gather data in UI.
     * @return
     * @throws Exception
     */
    public String update(@ModelAttribute("student") Student student, Model model) throws Exception{
        if (checkIfStudentExist(student.getName()) == null) {
            throw new Exception("This student does not exist");
        }
        students.stream()
                .filter(student1 -> student1.getId().equals(student.getId()))
                .peek(student1 -> {
                    student1.setName(student.getName());
                    student1.setYearBorn(student.getYearBorn());
                }).collect(Collectors.toList());
        model.addAttribute("message", "Student has been updated with success");
        return "redirect:/";
    }

    /**
     * Delete student.
     * @param id
     * @param model
     * @return
     */
    public String delete(@PathVariable Integer id, Model model) {
        students.removeIf(student -> student.getId() == id);
        model.addAttribute("message", "Student has been deleted successfully");
        return "/";
    }


    /**
     * Check if user exists before adding as new one, or try to update it.
     * @param name think about like name is unique.
     * @return student.
     */
    private Student checkIfStudentExist(String name) {
        return students.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}

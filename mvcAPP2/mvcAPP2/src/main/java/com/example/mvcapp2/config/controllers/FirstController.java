package com.example.mvcapp2.config.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FirstController {

    @GetMapping("/hello")
    public String helloPage(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "surname", required = false) String surname, Model model)
    {
//        System.out.println("Привет " + name + " " + surname);
        model.addAttribute("message", "hello, " + name + " " + surname);

        return "first/hello";
    }

//    @GetMapping("/hello")
//    public String helloPage(HttpServletRequest request) {
//        String name = request.getParameter("name");
//        String surname = request.getParameter("surname");
//
//        System.out.println("Name from request: " + name);
//        System.out.println("Surname from request: " + surname);
//
//        System.out.println("Привет " + name + " " + surname);
//
//        return "first/hello";
//    }


    @GetMapping("/goodbye")
    public String goodbyePage()
    {
        return "first/goodbye";
    }


    @GetMapping("/calculator")
    public String calculate(@RequestParam(value = "a", required = false) String a,
                            @RequestParam(value = "b", required = false) String b,
                            @RequestParam(value = "action", required = false) String action, Model model)
    {
        try
        {
        int numA = Integer.parseInt(a);
        int numB = Integer.parseInt(b);
        switch (action) {
            case "addition":
                double result1 = numA + numB;
                model.addAttribute("message", "результат сложения равен : " + a + " + " + b + " = " + result1);
                break;
            case "substraction":
                double result2 = numA - numB;
                model.addAttribute("message", "результат вычитания равен: " + a + " - " + b + " = " + result2);
                break;
            case "multiplication":
                double result3 = numA * numB;
                model.addAttribute("message", "результат умножения равен: " + a + " * " + b + " = " + result3);
                break;
            case "division":
                double result4 = numA / (double)numB;
                model.addAttribute("message", "результат деления равен : " + a + " / " + b + " = " + result4);
                break;
            default:
                model.addAttribute("message", "не такого арифметического действия");
        }
        }
        catch (NumberFormatException e)
        {
            model.addAttribute("message", "введите корректные числа");
        }
        catch (ArithmeticException e)
        {
            model.addAttribute("message", "деление на ноль");
        }

        return "first/calculator";
    }


}

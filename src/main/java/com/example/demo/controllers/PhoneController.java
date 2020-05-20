package com.example.demo.controllers;

import com.example.demo.exceptions.PhoneDoesNotExistException;
import com.example.demo.models.Phone;
import com.example.demo.repositories.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class PhoneController {

    @Autowired
    private PhoneRepository phoneRepository;

    @GetMapping("/phones")
    public String phones(Model model) {
        Iterable<Phone> phones = phoneRepository.findAll();
        model.addAttribute("phones", phones);
        return "phones";
    }

    @GetMapping("/phone/add")
    public String phoneAdd(Model model) {
        return "phone_add";
    }

    @PostMapping("/phone/add")
    public String phonePostAdd(@RequestParam String fio, @RequestParam String num, Model model) {
        Phone phone = new Phone(fio, num);
        phoneRepository.save(phone);
        return "redirect:/phones";
    }

    @GetMapping("/phone/{id}")
    public String phoneDetails(@PathVariable(value = "id") long id, Model model) {
        if (!phoneRepository.existsById(id)) {
            return "redirect:/phones";
        }
        Optional<Phone> phone = phoneRepository.findById(id);
        ArrayList<Phone> res = new ArrayList<>();
        phone.ifPresent(res::add);
        model.addAttribute("phone", res);
        return "phone_details";
    }

    @GetMapping("/phone/{id}/edit")
    public String phoneEdit(@PathVariable(value = "id") long id, Model model) {
        if (!phoneRepository.existsById(id)) {
            return "redirect:/phones";
        }
        Optional<Phone> phone = phoneRepository.findById(id);
        ArrayList<Phone> res = new ArrayList<>();
        phone.ifPresent(res::add);
        model.addAttribute("phone", res);
        return "phone_edit";
    }

    @PostMapping("/phone/{id}/edit")
    public String phonePostUpdate(@PathVariable(value = "id") long id, @RequestParam String fio, @RequestParam String num, Model model) {
        Phone phone = phoneRepository.findById(id).orElseThrow(() -> new PhoneDoesNotExistException(id));
        phone.setFio(fio);
        phone.setNum(num);
        phoneRepository.save(phone);
        return "redirect:/phones";
    }

    @PostMapping("/phone/{id}/remove")
    public String phonePostDelete(@PathVariable(value = "id") long id, Model model) {
        Phone phone = phoneRepository.findById(id).orElseThrow(() -> new PhoneDoesNotExistException(id));
        phoneRepository.delete(phone);
        return "redirect:/phones";
    }
}
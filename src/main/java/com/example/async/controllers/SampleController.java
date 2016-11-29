package com.example.async.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("model")
public class SampleController {
    @ModelAttribute
    public SampleForm setUpSampleForm() {
        return new SampleForm("default", "default");
    }

    @GetMapping("confirm")
    public String set(@RequestParam("name") String name, TitleForm titleForm, SampleForm sampleForm, Model model) {
        Map<String, Object> map = model.asMap();
        map.forEach((k, v) -> {
            System.out.println("key : " + k + " value : " + v);
        });
        return "paramter name is " + name + " title is " + titleForm.getTitle();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SampleForm {
    private String name;
    private String title;
}

@ToString
@NoArgsConstructor
@AllArgsConstructor
class TitleForm {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


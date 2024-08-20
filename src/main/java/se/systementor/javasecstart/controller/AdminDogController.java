package se.systementor.javasecstart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.systementor.javasecstart.model.Dog;
import se.systementor.javasecstart.services.DogService;

import java.util.List;

@Controller
public class AdminDogController {
    @Autowired
    private DogService dogService;

    @GetMapping(path = "/admin/dogs")
    String list(Model model) {
        model.addAttribute("activeFunction", "home");
//        setupVersion(model);

        model.addAttribute("dogs", dogService.getPublicDogs());
        return "admin/dogs/list";
    }

    @GetMapping("/admin/searchedDogs")
    public String searchedDogs(Model model,
                               @RequestParam(defaultValue = "") String q,
                               @RequestParam(defaultValue = "name") String sortCol,
                               @RequestParam(defaultValue = "ASC") String sortOrder) {
        q = q.trim();
        System.out.println(q);
        System.out.println(sortCol);
        System.out.println(sortOrder);
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortCol);
        List<Dog> searchedDogList = dogService.findAllBySearchAndSortOrder(q, sort);
        model.addAttribute("dogs", searchedDogList);
        model.addAttribute("q", q);
        return "admin/dogs/list";
    }
}

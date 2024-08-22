package se.systementor.javasecstart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.systementor.javasecstart.services.DogService;

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
//    @RequestMapping(path = "/editDog/{name}/{breed}/{age}/{size}/{price}")
//    public String showEditDogPage(@PathVariable String name, @PathVariable String breed, @PathVariable String age,
//                                  @PathVariable String size, @PathVariable int price, Model model) {
//
//        model.addAttribute("name", name);
//        model.addAttribute("breed", breed);
//        model.addAttribute("age", age);
//        model.addAttribute("size", size);
//        model.addAttribute("price", price);
//        return "editDog";
//    }

    @RequestMapping(path = "/editDog/{id}")
    public String showEditDogPage(@PathVariable int id, Model model) {
     Dog dog = dogService.getDogById(id);

        model.addAttribute("id", dog.getId());
        model.addAttribute("name", dog.getName());
        model.addAttribute("breed", dog.getBreed());
        model.addAttribute("age", dog.getAge());
        model.addAttribute("size", dog.getSize());
        model.addAttribute("price", dog.getPrice());
        return "editDog";
    }

    @PostMapping("/updateDog")
    public String updateOrAddCustomer(@RequestParam int id, @RequestParam String name, @RequestParam String breed, @RequestParam String age,
                                      @RequestParam String size, @RequestParam int price) {
        Dog dog = dogService.getDogById(id);
        dog.setName(name);
        dog.setBreed(breed);
        dog.setAge(age);
        dog.setPrice(price);
        dog.setSize(size);
        dogService.saveDog(dog);
        return "home";
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

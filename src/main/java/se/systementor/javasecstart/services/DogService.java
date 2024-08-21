package se.systementor.javasecstart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.systementor.javasecstart.model.Dog;
import se.systementor.javasecstart.model.DogRepository;

import java.util.List;

@Service
public class DogService {
    @Autowired
    DogRepository dogRepository;

    public List<Dog> getPublicDogs() {
        return dogRepository.findAllBySoldToIsNull();
    }

    public List<Dog> findAllBySearchAndSortOrder(String searchWord, Sort sort) {
        int price;
        try {
            price = Integer.parseInt(searchWord);
        } catch (NumberFormatException e) {
            price = -1; // or any invalid price value that shouldn't match
        }
        if (price != -1) {
            return dogRepository.findAllByPrice(price, sort);
        } else {
            return dogRepository.findAllByAgeContainsOrBreedContainsOrNameContainsOrSizeContains(searchWord, searchWord, searchWord, searchWord, sort);
        }

    }
}

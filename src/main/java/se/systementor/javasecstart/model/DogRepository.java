package se.systementor.javasecstart.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DogRepository extends CrudRepository<Dog, Long> {

    List<Dog> findAllBySoldToIsNull();

    List<Dog> findAllByPrice(int price, Sort sort);

    List<Dog> findAllByAgeContainsOrBreedContainsOrNameContainsOrSizeContains
            (String age, String breed, String name, String size, Sort sort);

    Dog findDogById(int id);

}
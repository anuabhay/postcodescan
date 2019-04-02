package com.hes.postcodescan.data;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hes.YPScraper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
class PostCodeController {
    //private CarRepository repository;

    //public PostCodeController(CarRepository repository) {
       // this.repository = repository;
    //}

    @GetMapping("/postcodes")
    @CrossOrigin(origins = "http://localhost:4200")
    public Collection<Business> coolCars( @RequestParam("locations") String locations,
                                          @RequestParam("types") String types,
                                          @RequestParam("dummy") String dummy
    ) {
        ArrayList<Business> list = new ArrayList<Business>();
        try {

            list = YPScraper.getBusinessData(locations,types,dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

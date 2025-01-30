package com.example.uade.tpo.controller;

import com.example.uade.tpo.dtos.request.DiscountRequestDto;
import com.example.uade.tpo.dtos.request.DiscountUpdateRequestDto;
import com.example.uade.tpo.dtos.response.DiscountPercentageResponseDto;
import com.example.uade.tpo.dtos.response.DiscountResponseDto;
import com.example.uade.tpo.service.DiscountService;
import com.example.uade.tpo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private UserService userService;

    @GetMapping() //Get all discounts
    public ResponseEntity<List<DiscountResponseDto>> getAllDiscounts(@RequestHeader("Authorization") String token) {
        Boolean validate = userService.validateRole(token);
        if(validate){
            List<DiscountResponseDto> discounts = discountService.getAllDiscounts();
            return new ResponseEntity<>(discounts, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

    }


    @PostMapping ("/create")//Create discount
    public ResponseEntity<?> createDiscount(@RequestBody DiscountRequestDto discount,@RequestHeader("Authorization") String token) {
        boolean validate = userService.validateRole(token);
        if(validate){
            DiscountResponseDto newDiscount = discountService.createDiscount(discount);
            if(newDiscount == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);}
        else{
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @PutMapping("/update/{discountId}") //Update discount
    public ResponseEntity<?> updateDiscount
            (@PathVariable Long discountId,@RequestBody @Valid DiscountUpdateRequestDto discount) {
        DiscountResponseDto updatedDiscount = discountService.updateDiscount(discountId, discount);
        if(updatedDiscount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{discountId}") //Delete discount
    public ResponseEntity<?> deleteDiscount(@PathVariable Long discountId) {
        Boolean deleted = discountService.deleteDiscount(discountId);
        if(!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getDiscount/{code}")
    public ResponseEntity<DiscountPercentageResponseDto> getDiscount(@PathVariable String code){
        DiscountPercentageResponseDto discount = discountService.getDiscountPercentage(code);
        if(discount == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println(discount);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PutMapping("/activateDiscount/{percentage}/{id}")
    public ResponseEntity<?> activateDiscount(@PathVariable String percentage, @PathVariable Long id){
        try {
            discountService.applyDiscount(id, percentage);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}

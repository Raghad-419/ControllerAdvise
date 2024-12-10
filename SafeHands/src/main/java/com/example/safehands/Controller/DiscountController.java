package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Discount;
import com.example.safehands.Service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discount")
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/get")
    public ResponseEntity getDiscount(){
        return ResponseEntity.status(200).body(discountService.getDiscount());
    }


    @PostMapping("/add/{adminId}")
    public ResponseEntity addDiscount(@PathVariable Integer adminId , @RequestBody @Valid Discount discount){
        discountService.addDiscount(adminId,discount);
        return ResponseEntity.status(200).body(new ApiResponse("Discount added"));
    }

    @PutMapping("/update/{adminId}/{discountId}")
    public ResponseEntity updateDiscount(@PathVariable Integer adminId ,@PathVariable Integer discountId ,@RequestBody @Valid Discount discount, Errors errors) {
        discountService.updateDiscount(adminId, discountId, discount);
        return ResponseEntity.status(200).body(new ApiResponse("Discount updated"));
    }

    @DeleteMapping("/delete/{discountId}")
    public ResponseEntity deleteDiscount(@PathVariable Integer discountId){
        discountService.deleteDiscount(discountId);
        return ResponseEntity.status(200).body(new ApiResponse("Discount deleted"));
    }

}

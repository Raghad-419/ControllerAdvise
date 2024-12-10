package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.Admin;
import com.example.safehands.Model.Discount;
import com.example.safehands.Repository.AdminRepository;
import com.example.safehands.Repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final AdminRepository adminRepository;


    public List<Discount> getDiscount(){
        return discountRepository.findAll();
    }
    public void addDiscount(Integer adminId, Discount discount){
        Admin admin =adminRepository.findAdminById(adminId);
        if(admin==null) {
            throw new ApiException("Admin not found");
        }
        discountRepository.save(discount);

        }

        public void updateDiscount(Integer adminId,Integer discountId , Discount discount){
          Discount discount1 = discountRepository.findDiscountById(discountId);
          if(discount1==null){
              throw new ApiException("Discount not found");
          }
            Admin admin =adminRepository.findAdminById(adminId);
            if(admin==null) {
                throw new ApiException("Admin not found");
            }

            discount1.setAmount(discount.getAmount());
            discount1.setCode(discount.getCode());
            discount1.setExpirationDate(discount.getExpirationDate());

            discountRepository.save(discount1);
        }


        public void deleteDiscount(Integer discountId){
            Discount discount1 = discountRepository.findDiscountById(discountId);
            if(discount1==null){
                throw new ApiException("Discount not found");
            }
            discountRepository.delete(discount1);
        }


}

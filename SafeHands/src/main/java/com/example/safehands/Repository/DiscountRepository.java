package com.example.safehands.Repository;

import com.example.safehands.Model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    Discount findDiscountById(Integer id);

    Discount findDiscountByCode(String code);
}

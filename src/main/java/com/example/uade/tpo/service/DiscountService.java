package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.DiscountRequestDto;
import com.example.uade.tpo.dtos.response.DiscountResponseDto;
import com.example.uade.tpo.entity.Discount;
import com.example.uade.tpo.repository.IDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private IDiscountRepository discountRepository;

    public List<DiscountResponseDto> getDiscounts() {
        return discountRepository.findAll().stream().map
                (Mapper::convertToDiscountResponseDto).collect(Collectors.toList());
    }

    public Optional<DiscountResponseDto> getDiscount(Long discountId) {
        return discountRepository.findById(discountId).map(Mapper::convertToDiscountResponseDto);
    }

    public DiscountResponseDto createDiscount(DiscountRequestDto discountDto) {
        Discount discount = new Discount();
        List<Discount> discounts = discountRepository.findAll();
        for (Discount d : discounts) {
            if (d.getCode().equals(discountDto.getCode())) {
                return null;
            }
        }
        discount.setCode(discountDto.getCode());
        discount.setDiscountValue(discountDto.getDiscountValue());
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        return Mapper.convertToDiscountResponseDto(discountRepository.save(discount));
    }

    public DiscountResponseDto updateDiscount(Long discountId, DiscountRequestDto discountDetails) {
        Optional<Discount> discountOptional = discountRepository.findById(discountId);
        List<Discount> discounts = discountRepository.findAll();
        for (Discount d : discounts) {
            if (d.getCode().equals(discountDetails.getCode())) {
                return null;
            }
        }
        if (discountOptional.isPresent()) {
            Discount discount = discountOptional.get();
            discount.setCode(discountDetails.getCode());
            discount.setDiscountValue(discountDetails.getDiscountValue());
            discount.setStartDate(discountDetails.getStartDate());
            discount.setEndDate(discountDetails.getEndDate());
            return Mapper.convertToDiscountResponseDto(discountRepository.save(discount));
        }
        return null;
    }

    public Boolean deleteDiscount(Long discountId) {
        if (discountRepository.existsById(discountId)) {
            discountRepository.deleteById(discountId);
            return true;
        }
        return false;
    }

    public Boolean deleteDiscountByCode(String code) {
        if(discountRepository.existsByCode(code)) {
            discountRepository.deleteByCode(code);
            return true;
        }
        return false;
    }
}

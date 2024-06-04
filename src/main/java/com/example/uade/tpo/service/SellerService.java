package com.example.uade.tpo.service;

import com.example.uade.tpo.Utils.Mapper;
import com.example.uade.tpo.dtos.request.SellerRequestDto;
import com.example.uade.tpo.dtos.response.SellerResponseDto;
import com.example.uade.tpo.entity.Seller;
import com.example.uade.tpo.repository.ISellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SellerService {

    @Autowired
    ISellerRepository sellerRepository;

    public List<SellerResponseDto> getSellers() {
        return sellerRepository.findAll().stream().map(Mapper::convertToSellerResponseDto).collect(Collectors.toList());
    }

    public Optional<SellerResponseDto> getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId).map(Mapper::convertToSellerResponseDto);
    }

    public SellerResponseDto createSeller(SellerRequestDto sellerDto) {
        Seller seller = new Seller();
        List<Seller> sellers = sellerRepository.findAll();
        for (Seller s : sellers) {
            if (s.getUserId().equals(sellerDto.getUserId()) || s.getStoreName().equals(sellerDto.getStoreName())) {
                return null;
            }
        }
        seller.setUserId(sellerDto.getUserId());
        seller.setStoreName(sellerDto.getStoreName());
        seller.setAddress(sellerDto.getAddress());
        return Mapper.convertToSellerResponseDto(sellerRepository.save(seller));
    }

    public SellerResponseDto updateSeller(Long sellerId, SellerRequestDto sellerDetails) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
        List<Seller> sellers = sellerRepository.findAll();
        for (Seller s : sellers) {
            if (s.getUserId().equals(sellerDetails.getUserId()) || s.getStoreName().equals(sellerDetails.getStoreName())) {
                return null;
            }
        }
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            seller.setUserId(sellerDetails.getUserId());
            seller.setStoreName(sellerDetails.getStoreName());
            seller.setAddress(sellerDetails.getAddress());
            return Mapper.convertToSellerResponseDto(sellerRepository.save(seller));
        }
        return null;
    }

    public Boolean deleteSeller(Long sellerId) {
        if (sellerRepository.existsById(sellerId)) {
            sellerRepository.deleteById(sellerId);
            return true;
        }
        return false;
    }
}

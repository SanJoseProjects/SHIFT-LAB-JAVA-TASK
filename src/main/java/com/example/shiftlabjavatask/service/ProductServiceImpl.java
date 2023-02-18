package com.example.shiftlabjavatask.service;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.dto.ProductIdDto;
import com.example.shiftlabjavatask.repository.*;
import com.example.shiftlabjavatask.repository.entity.*;
import com.example.shiftlabjavatask.repository.entity.enums.DesktopPcFormFactor;
import com.example.shiftlabjavatask.repository.entity.enums.LaptopSize;
import com.example.shiftlabjavatask.repository.entity.enums.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final DesktopDescriptionRepository desktopDescriptionRepository;
    private final HardDriveDescriptionRepository hardDriveDescriptionRepository;
    private final LaptopDescriptionRepository laptopDescriptionRepository;
    private final ScreenDescriptionRepository screenDescriptionRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, DesktopDescriptionRepository desktopDescriptionRepository, HardDriveDescriptionRepository hardDriveDescriptionRepository, LaptopDescriptionRepository laptopDescriptionRepository, ScreenDescriptionRepository screenDescriptionRepository) {
        this.productRepository = productRepository;
        this.desktopDescriptionRepository = desktopDescriptionRepository;
        this.hardDriveDescriptionRepository = hardDriveDescriptionRepository;
        this.laptopDescriptionRepository = laptopDescriptionRepository;
        this.screenDescriptionRepository = screenDescriptionRepository;
    }


    @Override
    public ProductIdDto createProduct(ProductDto productDto) {
        Product product = productRepository.findBySerialNumber(productDto.getSerialNumber());

        if (product != null)
            return null;

        product = new Product();
        product.setProducer(productDto.getProducer());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setSerialNumber(productDto.getSerialNumber());
        product.setType(ProductType.valueOf(productDto.getType()));

        switch (product.getType()){
            case DESKTOP_PC:
                DesktopDescription desktopDescription = new DesktopDescription();
                desktopDescription.setProduct(product);
                desktopDescription.setFormFactor(DesktopPcFormFactor.valueOf(productDto.getDescription()));
                desktopDescriptionRepository.save(desktopDescription);
                break;

            case LAPTOP:
                LaptopDescription laptopDescription = new LaptopDescription();
                laptopDescription.setProduct(product);
                laptopDescription.setSize(LaptopSize.valueOf(productDto.getDescription()));
                laptopDescriptionRepository.save(laptopDescription);
                break;

            case SCREEN:
                ScreenDescription screenDescription = new ScreenDescription();
                screenDescription.setProduct(product);
                screenDescription.setDiagonal(Double.parseDouble(productDto.getDescription()));
                screenDescriptionRepository.save(screenDescription);
                break;

            case HARD_DRIVE:
                HardDriveDescription hardDriveDescription = new HardDriveDescription();
                hardDriveDescription.setProduct(product);
                hardDriveDescription.setCapacity(Double.parseDouble(productDto.getDescription()));
                hardDriveDescriptionRepository.save(hardDriveDescription);
                break;

            default:
                return null;
        }


        return new ProductIdDto(productRepository.save(product).getId());
    }
}

package com.example.shiftlabjavatask.service;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.dto.ProductIdDto;
import com.example.shiftlabjavatask.repository.*;
import com.example.shiftlabjavatask.repository.entity.*;
import com.example.shiftlabjavatask.repository.entity.enums.DesktopPcFormFactor;
import com.example.shiftlabjavatask.repository.entity.enums.LaptopSize;
import com.example.shiftlabjavatask.repository.entity.enums.ProductType;
import jakarta.transaction.Transactional;
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

        saveProductDescription(product, productDto.getDescription());

        return new ProductIdDto(productRepository.save(product).getId());
    }

    @Override
    @Transactional
    public void updateProduct(ProductDto productDto)
    {
        Product product = null;
        Product oldProductData = null;
        if (productRepository.findById(productDto.getId()).isPresent()) {
            product = productRepository.findById(productDto.getId()).get();
            oldProductData = new Product(productRepository.findById(productDto.getId()).get());
        }

        if (product != null) {
            product.setProducer(productDto.getProducer());
            product.setSerialNumber(productDto.getSerialNumber());
            product.setPrice(productDto.getPrice());
            product.setType(ProductType.valueOf(productDto.getType()));
            product.setQuantity(productDto.getQuantity());

            updateProductDescription(product, oldProductData, productDto.getDescription());
            productRepository.save(product);
        }

    }

    @Override
    @Transactional
    public void deleteProduct(ProductIdDto productIdDto) {
        Product product;
        if (productRepository.findById(productIdDto.getId()).isPresent())
        {
            product = productRepository.findById(productIdDto.getId()).get();
            deleteProductDescription(product);
            productRepository.delete(product);
        }
    }


    private void saveProductDescription(Product product, String description) {
        switch (product.getType()) {
            case DESKTOP_PC -> {
                DesktopDescription desktopDescription = new DesktopDescription();
                desktopDescription.setProduct(product);
                desktopDescription.setFormFactor(DesktopPcFormFactor.valueOf(description));
                desktopDescriptionRepository.save(desktopDescription);
            }
            case LAPTOP -> {
                LaptopDescription laptopDescription = new LaptopDescription();
                laptopDescription.setProduct(product);
                laptopDescription.setSize(LaptopSize.valueOf(description));
                laptopDescriptionRepository.save(laptopDescription);
            }
            case SCREEN -> {
                ScreenDescription screenDescription = new ScreenDescription();
                screenDescription.setProduct(product);
                screenDescription.setDiagonal(Double.parseDouble(description));
                screenDescriptionRepository.save(screenDescription);
            }
            case HARD_DRIVE -> {
                HardDriveDescription hardDriveDescription = new HardDriveDescription();
                hardDriveDescription.setProduct(product);
                hardDriveDescription.setCapacity(Double.parseDouble(description));
                hardDriveDescriptionRepository.save(hardDriveDescription);
            }
        }
    }

    private void updateProductDescription(Product product, Product oldProductData, String description) {
        switch (product.getType()) {
            case DESKTOP_PC -> {
                if (!oldProductData.getType().equals(ProductType.DESKTOP_PC))
                    deleteProductDescription(oldProductData);

                DesktopDescription desktopDescription = new DesktopDescription();
                if (desktopDescriptionRepository.findById(product.getId()).isPresent())
                        desktopDescription = desktopDescriptionRepository.findById(product.getId()).get();

                desktopDescription.setProduct(product);
                desktopDescription.setFormFactor(DesktopPcFormFactor.valueOf(description));
                desktopDescriptionRepository.save(desktopDescription);
            }
            case LAPTOP -> {
                if (!oldProductData.getType().equals(ProductType.LAPTOP))
                    deleteProductDescription(oldProductData);

                LaptopDescription laptopDescription = new LaptopDescription();
                if (laptopDescriptionRepository.findById(product.getId()).isPresent())
                    laptopDescription = laptopDescriptionRepository.findById(product.getId()).get();

                laptopDescription.setProduct(product);
                laptopDescription.setSize(LaptopSize.valueOf(description));
                laptopDescriptionRepository.save(laptopDescription);
            }
            case SCREEN -> {
                if (!oldProductData.getType().equals(ProductType.SCREEN))
                    deleteProductDescription(oldProductData);

                ScreenDescription screenDescription = new ScreenDescription();
                if (screenDescriptionRepository.findById(product.getId()).isPresent())
                       screenDescription = screenDescriptionRepository.findById(product.getId()).get();

                screenDescription.setProduct(product);
                screenDescription.setDiagonal(Double.parseDouble(description));
                screenDescriptionRepository.save(screenDescription);
            }
            case HARD_DRIVE -> {
                if (!oldProductData.getType().equals(ProductType.HARD_DRIVE))
                    deleteProductDescription(oldProductData);

                HardDriveDescription hardDriveDescription = new HardDriveDescription();
                if (hardDriveDescriptionRepository.findById(product.getId()).isPresent())
                    hardDriveDescription = hardDriveDescriptionRepository.findById(product.getId()).get();

                hardDriveDescription.setProduct(product);
                hardDriveDescription.setCapacity(Double.parseDouble(description));
                hardDriveDescriptionRepository.save(hardDriveDescription);
            }
        }
    }

    private void deleteProductDescription(Product oldProductData)
    {
        switch (oldProductData.getType()) {
            case DESKTOP_PC -> desktopDescriptionRepository.delete(desktopDescriptionRepository.getReferenceById(oldProductData.getId()));
            case LAPTOP -> laptopDescriptionRepository.delete(laptopDescriptionRepository.getReferenceById(oldProductData.getId()));
            case SCREEN -> screenDescriptionRepository.delete(screenDescriptionRepository.getReferenceById(oldProductData.getId()));
            case HARD_DRIVE -> hardDriveDescriptionRepository.delete(hardDriveDescriptionRepository.getReferenceById(oldProductData.getId()));
        }
    }
}

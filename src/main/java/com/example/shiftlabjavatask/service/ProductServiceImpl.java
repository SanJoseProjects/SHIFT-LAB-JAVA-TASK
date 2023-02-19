package com.example.shiftlabjavatask.service;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.dto.ProductIdDto;
import com.example.shiftlabjavatask.exception.*;
import com.example.shiftlabjavatask.mapper.ProductMapper;
import com.example.shiftlabjavatask.repository.*;
import com.example.shiftlabjavatask.repository.entity.*;
import com.example.shiftlabjavatask.repository.entity.enums.DesktopPcFormFactor;
import com.example.shiftlabjavatask.repository.entity.enums.LaptopSize;
import com.example.shiftlabjavatask.repository.entity.enums.ProductType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final DesktopDescriptionRepository desktopDescriptionRepository;
    private final HardDriveDescriptionRepository hardDriveDescriptionRepository;
    private final LaptopDescriptionRepository laptopDescriptionRepository;
    private final ScreenDescriptionRepository screenDescriptionRepository;
    private final MessageSource messageSource;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, DesktopDescriptionRepository desktopDescriptionRepository, HardDriveDescriptionRepository hardDriveDescriptionRepository, LaptopDescriptionRepository laptopDescriptionRepository, ScreenDescriptionRepository screenDescriptionRepository, MessageSource messageSource) {
        this.productRepository = productRepository;
        this.desktopDescriptionRepository = desktopDescriptionRepository;
        this.hardDriveDescriptionRepository = hardDriveDescriptionRepository;
        this.laptopDescriptionRepository = laptopDescriptionRepository;
        this.screenDescriptionRepository = screenDescriptionRepository;
        this.messageSource = messageSource;
    }


    @Override
    public ProductIdDto createProduct(ProductDto productDto) {
        Product product = productRepository.findBySerialNumber(productDto.getSerialNumber());

        if (product != null)
            throw new ProductSerialNumberIsBusy(messageSource.getMessage("product.serial_number.is_busy", null, Locale.getDefault()));

        checkProducts(productDto);

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
    public void updateProduct(ProductDto productDto) {
        Product product = null;
        Product oldProductData = null;

        if (productDto.getId() == null)
            throw new ProductIdIsEmpty(messageSource.getMessage("product.id.is_empty", null, Locale.getDefault()));

        if (productRepository.findById(productDto.getId()).isPresent()) {
            product = productRepository.findById(productDto.getId()).get();
            oldProductData = new Product(productRepository.findById(productDto.getId()).get());
        }

        if (product == null)
            throw new ProductNotFound(messageSource.getMessage("product.not_found", null, Locale.getDefault()));

        checkProducts(productDto);

        product.setProducer(productDto.getProducer());
        product.setSerialNumber(productDto.getSerialNumber());
        product.setPrice(productDto.getPrice());
        product.setType(ProductType.valueOf(productDto.getType()));
        product.setQuantity(productDto.getQuantity());

        updateProductDescription(product, oldProductData, productDto.getDescription());
        productRepository.save(product);

    }

    @Override
    @Transactional
    public void deleteProduct(ProductIdDto productIdDto) {
        Product product = null;
        if (productRepository.findById(productIdDto.getId()).isPresent()) {
            product = productRepository.findById(productIdDto.getId()).get();
            deleteProductDescription(product);
            productRepository.delete(product);
        }

        if (product == null)
            throw new ProductNotFound(messageSource.getMessage("product.not_found", null, Locale.getDefault()));
    }

    @Override
    public List<ProductDto> findAllByType(String type) {
        if (Arrays.stream(ProductType.values()).noneMatch(el -> el.toString().equals(type)))
            throw new ProductTypeNotCorrect(messageSource.getMessage("product.type.not_correct", null, Locale.getDefault()));

        ProductType productType = ProductType.valueOf(type);
        List<ProductDto> products = new ArrayList<>();
        switch (productType) {
            case DESKTOP_PC -> {
                List<DesktopDescription> desktopDescriptions = desktopDescriptionRepository.findAll().stream().toList();
                for (DesktopDescription desktopDescription : desktopDescriptions) {
                    ProductDto product = ProductMapper.INSTANCE.productToProductDto(
                            productRepository.findById(desktopDescription.getProduct().getId()).get(),
                            desktopDescription.getFormFactor().toString());
                    products.add(product);
                }
            }

            case LAPTOP -> {
                List<LaptopDescription> laptopDescriptions = laptopDescriptionRepository.findAll().stream().toList();
                for (LaptopDescription laptopDescription : laptopDescriptions) {
                    ProductDto product = ProductMapper.INSTANCE.productToProductDto(
                            productRepository.findById(laptopDescription.getProduct().getId()).get(),
                            laptopDescription.getSize().toString());
                    products.add(product);
                }
            }

            case SCREEN -> {
                List<ScreenDescription> screenDescriptions = screenDescriptionRepository.findAll().stream().toList();
                for (ScreenDescription screenDescription : screenDescriptions) {
                    ProductDto product = ProductMapper.INSTANCE.productToProductDto(
                            productRepository.findById(screenDescription.getProduct().getId()).get(),
                            String.valueOf(screenDescription.getDiagonal()));
                    products.add(product);
                }
            }

            case HARD_DRIVE -> {
                List<HardDriveDescription> hardDriveDescriptions = hardDriveDescriptionRepository.findAll().stream().toList();
                for (HardDriveDescription hardDriveDescription : hardDriveDescriptions) {
                    ProductDto product = ProductMapper.INSTANCE.productToProductDto(
                            productRepository.findById(hardDriveDescription.getProduct().getId()).get(),
                            String.valueOf(hardDriveDescription.getCapacity()));
                    products.add(product);
                }
            }
        }

        return products;
    }

    @Override
    @Transactional
    public ProductDto findById(Long id) {
        Product product = null;
        if (productRepository.findById(id).isPresent())
            product = productRepository.findById(id).get();

        if (product == null)
            throw new ProductNotFound(messageSource.getMessage("product.not_found", null, Locale.getDefault()));

        switch (product.getType()) {
            case DESKTOP_PC -> {
                return ProductMapper.INSTANCE.productToProductDto(product,
                        desktopDescriptionRepository.findByProductId(id).getFormFactor().toString());
            }

            case LAPTOP -> {
                return ProductMapper.INSTANCE.productToProductDto(product,
                        laptopDescriptionRepository.findByProductId(id).getSize().toString());
            }

            case SCREEN -> {
                return ProductMapper.INSTANCE.productToProductDto(product,
                        String.valueOf(screenDescriptionRepository.findByProductId(id).getDiagonal()));
            }

            case HARD_DRIVE -> {
                return ProductMapper.INSTANCE.productToProductDto(product,
                        String.valueOf(hardDriveDescriptionRepository.findByProductId(id).getCapacity()));
            }
        }

        return null;
    }


    private void saveProductDescription(Product product, String description) {
        switch (product.getType()) {
            case DESKTOP_PC -> {
                if (Arrays.stream(DesktopPcFormFactor.values()).noneMatch(el -> el.toString().equals(description)))
                    throw new ProductDesktopPcTypeNotCorrect(messageSource.getMessage("product.desktop_pc.type.not_correct", null, Locale.getDefault()));

                DesktopDescription desktopDescription = new DesktopDescription();
                desktopDescription.setProduct(product);
                desktopDescription.setFormFactor(DesktopPcFormFactor.valueOf(description));
                desktopDescriptionRepository.save(desktopDescription);
            }
            case LAPTOP -> {
                if (Arrays.stream(LaptopSize.values()).noneMatch(el -> el.toString().equals(description)))
                    throw new ProductLaptopSizeNotCorrect(messageSource.getMessage("product.laptop.size.not_correct", null, Locale.getDefault()));

                LaptopDescription laptopDescription = new LaptopDescription();
                laptopDescription.setProduct(product);
                laptopDescription.setSize(LaptopSize.valueOf(description));
                laptopDescriptionRepository.save(laptopDescription);
            }
            case SCREEN -> {
                if (!isNumeric(description) || Double.parseDouble(description) <= 0)
                    throw new ProductScreenDiagonalNotCorrect(messageSource.getMessage("product.scree.diagonal.not_correct", null, Locale.getDefault()));

                ScreenDescription screenDescription = new ScreenDescription();
                screenDescription.setProduct(product);
                screenDescription.setDiagonal(Double.parseDouble(description));
                screenDescriptionRepository.save(screenDescription);
            }
            case HARD_DRIVE -> {
                if (!isNumeric(description) || Double.parseDouble(description) <= 0)
                    throw new ProductHardDriveSizeNotCorrect(messageSource.getMessage("product.hard_drive.capacity.not_correct", null, Locale.getDefault()));

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
                if (Arrays.stream(DesktopPcFormFactor.values()).noneMatch(el -> el.toString().equals(description)))
                    throw new ProductDesktopPcTypeNotCorrect(messageSource.getMessage("product.desktop_pc.type.not_correct", null, Locale.getDefault()));

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
                if (Arrays.stream(LaptopSize.values()).noneMatch(el -> el.toString().equals(description)))
                    throw new ProductLaptopSizeNotCorrect(messageSource.getMessage("product.laptop.size.not_correct", null, Locale.getDefault()));


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
                if (!isNumeric(description) || Double.parseDouble(description) <= 0)
                    throw new ProductScreenDiagonalNotCorrect(messageSource.getMessage("product.scree.diagonal.not_correct", null, Locale.getDefault()));


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
                if (!isNumeric(description) || Double.parseDouble(description) <= 0)
                    throw new ProductHardDriveSizeNotCorrect(messageSource.getMessage("product.hard_drive.capacity.not_correct", null, Locale.getDefault()));

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

    private void deleteProductDescription(Product oldProductData) {
        switch (oldProductData.getType()) {
            case DESKTOP_PC -> desktopDescriptionRepository.delete(desktopDescriptionRepository.findByProductId(oldProductData.getId()));
            case LAPTOP -> laptopDescriptionRepository.delete(laptopDescriptionRepository.findByProductId(oldProductData.getId()));
            case SCREEN -> screenDescriptionRepository.delete(screenDescriptionRepository.findByProductId(oldProductData.getId()));
            case HARD_DRIVE -> hardDriveDescriptionRepository.delete(hardDriveDescriptionRepository.findByProductId(oldProductData.getId()));
        }
    }

    private void checkProducts(ProductDto productDto) {
        if (productDto.getSerialNumber() == null)
            throw new ProductSerialNumberIsEmpty(messageSource.getMessage("product.serial_number.is_empty", null, Locale.getDefault()));

        if (Arrays.stream(ProductType.values()).noneMatch(el -> el.toString().equals(productDto.getType())))
            throw new ProductTypeNotCorrect(messageSource.getMessage("product.type.not_correct", null, Locale.getDefault()));

        if (productDto.getDescription() == null)
            throw new ProductDescriptionIsEmpty(messageSource.getMessage("product.description.is_empty", null, Locale.getDefault()));

        if (productDto.getPrice() <= 0)
            throw new ProductPriceNotCorrect(messageSource.getMessage("product.price.not_correct", null, Locale.getDefault()));

        if (productDto.getQuantity() < 0)
            throw new ProductQuantityNotCorrect(messageSource.getMessage("product.quantity.not_correct", null, Locale.getDefault()));
    }

    private boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

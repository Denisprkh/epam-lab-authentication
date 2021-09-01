package com.epam.esm.service.impl;

import com.epam.esm.dao.purchase.PurchaseDao;
import com.epam.esm.dto.RequestPurchaseDto;
import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.dto.mapper.impl.RequestPurchaseDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseGiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponsePurchaseDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.controller.util.Pagination;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;
    private final PaginationContextBuilder paginationContextBuilder;
    private final ResponsePurchaseDtoMapper responsePurchaseDtoMapper;
    private final RequestPurchaseDtoMapper requestPurchaseDtoMapper;
    private final ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper;
    private final GiftCertificateService giftCertificateService;
    private final UserService userService;

    public PurchaseServiceImpl(PurchaseDao purchaseDao, PaginationContextBuilder paginationContextBuilder,
                               ResponsePurchaseDtoMapper responsePurchaseDtoMapper, RequestPurchaseDtoMapper requestPurchaseDtoMapper,
                               ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper,
                               GiftCertificateService giftCertificateService,
                               UserService userService) {
        this.purchaseDao = purchaseDao;
        this.paginationContextBuilder = paginationContextBuilder;
        this.responsePurchaseDtoMapper = responsePurchaseDtoMapper;
        this.requestPurchaseDtoMapper = requestPurchaseDtoMapper;
        this.responseGiftCertificateDtoMapper = responseGiftCertificateDtoMapper;
        this.giftCertificateService = giftCertificateService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ResponsePurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto) {
        userExists(requestPurchaseDto.getUserId());
        Purchase purchaseForCreation = setPurchaseParameters(requestPurchaseDto);
        Purchase createdPurchase = purchaseDao.create(purchaseForCreation);
        return responsePurchaseDtoMapper.toDto(createdPurchase);
    }

    private Purchase setPurchaseParameters(RequestPurchaseDto requestPurchaseDto) {
        Purchase purchase = requestPurchaseDtoMapper.toModel(requestPurchaseDto);
        purchase.setGiftCertificates(definePurchaseCertificates(requestPurchaseDto));
        purchase.setCost(countPurchaseCost(purchase));
        purchase.setPurchaseDate(LocalDateTime.now());
        return purchase;
    }

    private void userExists(Integer userId) {
        userService.findUserById(userId);
    }

    private BigDecimal countPurchaseCost(Purchase purchase) {
        return purchase.getGiftCertificates().stream().map(GiftCertificate::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<GiftCertificate> definePurchaseCertificates(RequestPurchaseDto requestPurchaseDto) {
        return requestPurchaseDto.getCertificatesId().stream().map(giftCertificateId -> responseGiftCertificateDtoMapper
                .toModel(giftCertificateService.findGiftCertificateById(giftCertificateId))).collect(Collectors.toList());
    }

    @Override
    public ResponsePurchaseDto findPurchaseById(Integer id) {
        return responsePurchaseDtoMapper.toDto(purchaseDao.findById(id));
    }

    @Override
    public List<ResponsePurchaseDto> findAllPurchases(Pagination pagination) {
        int startOfRecords = paginationContextBuilder.defineStartOfRecords(pagination);
        int recordsPerPageAmount = paginationContextBuilder.defineRecordsPerPageAmount(pagination.getSize());
        return purchaseDao.findAll(startOfRecords, recordsPerPageAmount).stream()
                .map(responsePurchaseDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ResponsePurchaseDto> findPurchasesByUserId(Integer userId) {
        return purchaseDao.findPurchasesByUserId(userId).stream().map(responsePurchaseDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Long findQuantity() {
        return purchaseDao.findQuantity();
    }
}

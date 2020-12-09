package com.epam.esm.service.impl;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.dto.RequestOrderDto;
import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.dto.mapper.impl.RequestOrderDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseGiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseOrderDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final PaginationContextBuilder paginationContextBuilder;
    private final ResponseOrderDtoMapper responseOrderDtoMapper;
    private final RequestOrderDtoMapper requestOrderDtoMapper;
    private final ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper;
    private final GiftCertificateService giftCertificateService;
    private final UserService userService;

    public OrderServiceImpl(OrderDao orderDao, PaginationContextBuilder paginationContextBuilder,
                            ResponseOrderDtoMapper responseOrderDtoMapper, RequestOrderDtoMapper requestOrderDtoMapper,
                            ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper,
                            GiftCertificateService giftCertificateService,
                            UserService userService) {
        this.orderDao = orderDao;
        this.paginationContextBuilder = paginationContextBuilder;
        this.responseOrderDtoMapper = responseOrderDtoMapper;
        this.requestOrderDtoMapper = requestOrderDtoMapper;
        this.responseGiftCertificateDtoMapper = responseGiftCertificateDtoMapper;
        this.giftCertificateService = giftCertificateService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ResponseOrderDto createOrder(RequestOrderDto requestOrderDto) {
        userExists(requestOrderDto.getUserId());
        Order orderForCreation = setOrderParameters(requestOrderDto);
        Order createdOrder = orderDao.create(orderForCreation);
        return responseOrderDtoMapper.toDto(createdOrder);
    }

    private Order setOrderParameters(RequestOrderDto requestOrderDto) {
        Order order = requestOrderDtoMapper.toModel(requestOrderDto);
        order.setGiftCertificates(defineOrderCertificates(requestOrderDto));
        order.setCost(countOrderCost(order));
        order.setPurchaseDate(LocalDateTime.now());
        return order;
    }

    private void userExists(Integer userId) {
        userService.findUserById(userId);
    }

    private BigDecimal countOrderCost(Order order) {
        return order.getGiftCertificates().stream().map(GiftCertificate::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<GiftCertificate> defineOrderCertificates(RequestOrderDto requestOrderDto) {
        return requestOrderDto.getCertificatesId().stream().map(giftCertificateId -> responseGiftCertificateDtoMapper
                .toModel(giftCertificateService.findGiftCertificateById(giftCertificateId))).collect(Collectors.toList());
    }

    @Override
    public ResponseOrderDto findOrderById(Integer id) {
        return responseOrderDtoMapper.toDto(orderDao.findById(id));
    }

    @Override
    public List<ResponseOrderDto> findAllOrders(Pagination pagination) {
        int startOfRecords = paginationContextBuilder.defineStartOfRecords(pagination);
        int recordsPerPageAmount = paginationContextBuilder.defineRecordsPerPageAmount(pagination.getSize());
        return orderDao.findAll(startOfRecords, recordsPerPageAmount).stream()
                .map(responseOrderDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ResponseOrderDto> findOrdersByUserId(Integer userId) {
        return orderDao.findOrdersByUserId(userId).stream().map(responseOrderDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Long findQuantity() {
        return orderDao.findQuantity();
    }
}

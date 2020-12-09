package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificateCriteria;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import com.epam.esm.dto.RequestGiftCertificateDto;
import com.epam.esm.dto.ResponseGiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.util.pagination.PaginationResourceResolver;
import com.epam.esm.util.PaginationParameter;
import com.epam.esm.validation.ValidationGroup.Create;
import com.epam.esm.validation.ValidationGroup.Update;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller used to manipulate CRUD operations on
 * {@code GiftCertificate} data
 */
@RestController
@RequestMapping("/giftCertificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final NavigationLinkBuilder<ResponseGiftCertificateDto> responseGiftCertificateDtoLinkBuilder;
    private final PaginationResourceResolver paginationResourceResolver;

    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     NavigationLinkBuilder<ResponseGiftCertificateDto> responseGiftCertificateDtoLinkBuilder,
                                     PaginationResourceResolver paginationResourceResolver) {
        this.giftCertificateService = giftCertificateService;
        this.responseGiftCertificateDtoLinkBuilder = responseGiftCertificateDtoLinkBuilder;
        this.paginationResourceResolver = paginationResourceResolver;
    }

    /**
     * Creates new {@code GiftCertificate} in the database.
     * If new tags are passed during creation, they are
     * added to the database as well.
     * If resource with such name is already exists {@code HttpStatus.BAD_REQUEST} is returned.
     *
     * @param requestGiftCertificateDto {@code GiftCertificateDto} with values for {@code GiftCertificate} creation.
     * @return Created gift certificate.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseGiftCertificateDto> createGiftCertificate(@Validated(Create.class) @RequestBody
                                                                                    RequestGiftCertificateDto requestGiftCertificateDto) {
        ResponseGiftCertificateDto createdCertificate = giftCertificateService.createGiftCertificate(requestGiftCertificateDto);
        return new ResponseEntity<>(responseGiftCertificateDtoLinkBuilder.buildLinks(createdCertificate), HttpStatus.CREATED);
    }

    /**
     * Returns gift certificate with the requested id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id id of the requested certificate.
     * @return gift certificate with the requested id.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseGiftCertificateDto> findGiftCertificateById(@PathVariable Integer id) {
        ResponseGiftCertificateDto foundByIdCertificate = giftCertificateService.findGiftCertificateById(id);
        return new ResponseEntity<>(responseGiftCertificateDtoLinkBuilder.buildLinks(foundByIdCertificate), HttpStatus.OK);
    }

    /**
     * Deletes gift certificate with the specified id.
     * If no resources with such id are exists {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id {@code GiftCertificate}'s id.
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable Integer id) {
        giftCertificateService.deleteGiftCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updates gift certificate.
     * If resource with updated name is already exists {@code HttpStatus.BAD_REQUEST} is returned.
     *
     * @param giftCertificateDto {@code RequestGiftCertificateDto} with updated values.
     * @param id                 {@code GiftCertificate}'s id.
     * @return certificate with updated values.
     */
    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseGiftCertificateDto> updateGiftCertificate(@Validated(Update.class)
                                                                            @RequestBody RequestGiftCertificateDto giftCertificateDto,
                                                                            @PathVariable Integer id) {
        ResponseGiftCertificateDto updatedCertificate = giftCertificateService.updateGiftCertificate(giftCertificateDto, id);
        return new ResponseEntity<>(responseGiftCertificateDtoLinkBuilder.buildLinks(updatedCertificate), HttpStatus.OK);
    }


    /**
     * Returns all certificates corresponding to given page and number of records per page.
     *
     * @param giftCertificateCriteria object with values to sort and filter certificates with.
     * @param pagination              page number and page size are stored there.
     * @param params                  for storing page info in hateoas.
     * @return found  certificates.
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public RepresentationModel<?> findAllGiftCertificates(GiftCertificateCriteria giftCertificateCriteria, @RequestParam Map<String, String> params,
                                                          @Valid Pagination pagination) {
        long giftCertificatesQuantity = giftCertificateService.findQuantity(giftCertificateCriteria);
        List<ResponseGiftCertificateDto> giftCertificates = giftCertificateService.findAllGiftCertificates(giftCertificateCriteria, pagination).stream()
                .map(responseGiftCertificateDtoLinkBuilder::buildLinks)
                .collect(Collectors.toList());
        Map<String, Long> pageInfo = paginationResourceResolver.constructPageInfoStructure(pagination, giftCertificatesQuantity);
        List<Link> links = paginationResourceResolver.buildPaginationLinks(
                methodOn(GiftCertificateController.class).findAllGiftCertificates(giftCertificateCriteria, params, pagination), pagination, giftCertificatesQuantity, params);
        CollectionModel<ResponseGiftCertificateDto> collectionModel = CollectionModel.of(giftCertificates);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(pageInfo, LinkRelation.of(PaginationParameter.PARAM_PAGE)).build();
    }

}

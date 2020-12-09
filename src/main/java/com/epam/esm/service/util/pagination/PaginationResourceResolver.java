package com.epam.esm.service.util.pagination;

import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.linkbuilder.LinkName;
import com.epam.esm.util.PaginationParameter;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Component
public class PaginationResourceResolver {

    private static final Long RECORDS_PER_PAGE_DEFAULT_VALUE = 10L;
    private static final Long DEFAULT_PAGE_NUM = 1L;

    public List<Link> buildPaginationLinks(Object invocationValue, Pagination pagination,
                                           long totalRecordsAmount, Map<String, String> params) {
        List<Link> links = new ArrayList<>();
        long currentPage = checkCurrentPageCorrect(pagination.getPage());
        long totalPagesNumber = countTotalPagesNumber(totalRecordsAmount, pagination.getSize());
        if (totalRecordsAmount != 0) {
            if (currentPage < totalPagesNumber) {
                params.put(PaginationParameter.PARAM_PAGE, String.valueOf(currentPage + 1));
                links.add(linkTo(invocationValue).withRel(LinkName.NEXT));
            }
            if (currentPage > DEFAULT_PAGE_NUM) {
                params.put(PaginationParameter.PARAM_PAGE, String.valueOf(currentPage - 1));
                links.add(linkTo(invocationValue).withRel(LinkName.PREVIOUS));
            }
        }
        return links;
    }

    public Map<String, Long> constructPageInfoStructure(Pagination pagination, long totalRecordsAmount) {
        long size = pagination.getSize();
        long totalPagesNumber = countTotalPagesNumber(totalRecordsAmount, size);
        long currentPage = checkCurrentPageCorrect(pagination.getPage());
        Map<String, Long> pageInfo = new HashMap<>();
        pageInfo.put(PaginationParameter.TOTAL_PAGE_NUMBER, totalPagesNumber);
        pageInfo.put(PaginationParameter.CURRENT_PAGE, currentPage);
        pageInfo.put(PaginationParameter.RECORDS_PER_PAGE, defineNumberOfRecordsPerPage(size));
        pageInfo.put(PaginationParameter.NUMBER_OF_ELEMENTS, totalRecordsAmount);
        return pageInfo;
    }

    private long countTotalPagesNumber(long totalRecordsAmount, long size) {
        double recordsPerPage = defineNumberOfRecordsPerPage(size);
        return (long) Math.ceil(totalRecordsAmount / recordsPerPage);
    }

    private long defineNumberOfRecordsPerPage(long size) {
        if (size > 0) {
            return size;
        }
        return RECORDS_PER_PAGE_DEFAULT_VALUE;
    }

    private long checkCurrentPageCorrect(long currentPage) {
        if (currentPage >= DEFAULT_PAGE_NUM) {
            return currentPage;
        }
        return DEFAULT_PAGE_NUM;
    }
}

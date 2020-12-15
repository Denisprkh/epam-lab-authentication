package com.epam.esm.service.util.pagination;

import com.epam.esm.controller.util.Pagination;
import org.springframework.stereotype.Component;

@Component
public class PaginationContextBuilder {

    private static final Integer RECORDS_PER_PAGE_DEFAULT_VALUE = 10;
    private static final Integer DEFAULT_PAGE = 1;

    public Integer defineStartOfRecords(Pagination pagination) {
        int page = pagination.getPage();
        int size = pagination.getSize();
        int pageNum = page > 0 ? page : DEFAULT_PAGE;
        int pageSize = defineRecordsPerPageAmount(size);
        return (pageNum - 1) * pageSize;
    }

    public Integer defineRecordsPerPageAmount(int size) {
        if (size > 0) {
            return size;
        }
        return RECORDS_PER_PAGE_DEFAULT_VALUE;
    }
}

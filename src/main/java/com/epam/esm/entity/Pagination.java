package com.epam.esm.entity;

import com.epam.esm.util.ResourceBundleErrorMessage;

import javax.validation.constraints.NotNull;

public class Pagination {

    @NotNull(message = ResourceBundleErrorMessage.PAGE_PARAMETER_IS_REQUIRED)
    private Integer page;

    @NotNull(message = ResourceBundleErrorMessage.SIZE_PARAMETER_IS_REQUIRED)
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pagination that = (Pagination) o;

        if (page != null ? !page.equals(that.page) : that.page != null) return false;
        return size != null ? size.equals(that.size) : that.size == null;
    }

    @Override
    public int hashCode() {
        int result = page != null ? page.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pagination{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}

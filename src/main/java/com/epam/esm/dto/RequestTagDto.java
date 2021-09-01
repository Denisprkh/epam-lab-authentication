package com.epam.esm.dto;

import com.epam.esm.util.ResourceBundleErrorMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RequestTagDto {

    @NotBlank(message = ResourceBundleErrorMessage.TAG_NAME_IS_REQUIRED)
    @Size(min = 2, max = 30, message = ResourceBundleErrorMessage.TAG_NAME_FORMAT)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequestTagDto() {
    }

    public RequestTagDto(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestTagDto requestTagDto = (RequestTagDto) o;

        return name != null ? name.equals(requestTagDto.name) : requestTagDto.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TagDto{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

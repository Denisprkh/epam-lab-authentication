package com.epam.esm.dto;

import com.epam.esm.util.ResourceBundleErrorMessage;
import com.epam.esm.validation.ValidationGroup.Update;
import com.epam.esm.validation.ValidationGroup.Create;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class RequestGiftCertificateDto {

    @NotBlank(message = ResourceBundleErrorMessage.CERTIFICATE_NAME_FORMAT, groups = Create.class)
    @Pattern(regexp = "[\\w]{1,10}.{2,255}", message = ResourceBundleErrorMessage.CERTIFICATE_NAME_FORMAT)
    private String name;

    @NotBlank(message = ResourceBundleErrorMessage.CERTIFICATE_DESCRIPTION_FORMAT, groups = Create.class)
    @Pattern(regexp = "[\\w]{1,10}.{2,255}", message = ResourceBundleErrorMessage.CERTIFICATE_DESCRIPTION_FORMAT, groups =
            {Create.class, Update.class})
    private String description;

    @NotNull(message = ResourceBundleErrorMessage.CERTIFICATE_PRICE_IS_REQUIRED, groups = Create.class)
    @Digits(integer = 5, fraction = 2, message = ResourceBundleErrorMessage.CERTIFICATE_PRICE_FORMAT,
            groups = {Create.class, Update.class})
    private BigDecimal price;

    @NotNull(message = ResourceBundleErrorMessage.CERTIFICATE_TAG_IS_REQUIRED, groups = Create.class)
    @Size(min = 1, message = ResourceBundleErrorMessage.CERTIFICATE_TAG_IS_REQUIRED)
    private List<RequestTagDto> tags;

    @NotNull(message = ResourceBundleErrorMessage.CERTIFICATE_DURATION_IS_REQUIRED, groups = Create.class)
    @Pattern(regexp = "^PT\\d{1,3}[H]\\d{1,2}M$", message = ResourceBundleErrorMessage.CERTIFICATE_DURATION_FORMAT,
            groups = {Create.class, Update.class})
    private String duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<RequestTagDto> getTags() {
        return tags;
    }

    public void setTags(List<RequestTagDto> tags) {
        this.tags = tags;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreateGiftCertificateDto{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", tags=").append(tags);
        sb.append(", duration='").append(duration).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

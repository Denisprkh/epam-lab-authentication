package com.epam.esm.service.util.linkbuilder;

import org.springframework.hateoas.RepresentationModel;

public interface NavigationLinkBuilder<T extends RepresentationModel<? extends T>> {

    T buildLinks(T resource);

}

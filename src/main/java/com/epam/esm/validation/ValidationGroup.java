package com.epam.esm.validation;

import javax.validation.groups.Default;

public class ValidationGroup {

    private ValidationGroup() {
    }

    public interface Create extends Default {
    }

    public interface Replace extends Default {
    }

    public interface Update extends Default {
    }

    public interface Delete extends Default {
    }

}

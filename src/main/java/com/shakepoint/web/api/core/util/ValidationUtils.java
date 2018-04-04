package com.shakepoint.web.api.core.util;

import com.shakepoint.web.api.data.dto.request.admin.NewProductRequest;

public class ValidationUtils {
    public static boolean validateProduct(NewProductRequest product){
        if (product.getName() == null || product.getName().isEmpty())
            return false;
        if (product.getLogoUrl() == null || product.getLogoUrl().isEmpty()) {
            //check if there is a file present
            return false;
        }
        return true;
    }
}

package com.shakepoint.web.api.core.util;

import com.shakepoint.web.api.core.exception.MandatoryFieldException;
import com.shakepoint.web.api.data.dto.request.admin.NewProductRequest;

public class ValidationUtils {
    public static void validateProduct(NewProductRequest product)throws MandatoryFieldException{
        if (product.getName() == null || product.getName().isEmpty()){
            throw new MandatoryFieldException("name");
        } else if (product.getLogoUrl() == null || product.getLogoUrl().isEmpty()) {
            throw new MandatoryFieldException("logoUrl");
        } else if (product.getDescription() == null || product.getDescription().isEmpty()){
            throw new MandatoryFieldException("description");
        } else if (product.getEngineUseTime() == null || product.getEngineUseTime().isEmpty()) {
            throw new MandatoryFieldException("engineUseTime");
        } else if (product.getNutritionalDataImage() == null || product.getNutritionalDataImage().length == 0){
            throw new MandatoryFieldException("file");
        } else if (product.getProductType() == null || product.getProductType().isEmpty()){
            throw new MandatoryFieldException("productType");
        } else if (product.getPrice() <= 0){
            throw new MandatoryFieldException("price");
        }
    }
}

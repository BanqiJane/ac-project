package xyz.acproject.router_flux.annotation.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import xyz.acproject.router_flux.annotation.XSSCHECK;
import xyz.acproject.utils.valid.XSSUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author Jane
 * @ClassName XSSCheckValidator
 * @Description TODO
 * @date 2021/6/9 9:54
 * @Copyright:2021
 */
//
public class XSSCheckValidator implements ConstraintValidator<XSSCHECK, Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) return true;

        if(value instanceof String) {
            if(StringUtils.isEmpty((String)value)) return true;
            return XSSUtils.isValid((String)value);
        }


        if(value instanceof Collection) {
            Collection<String> collection = (Collection<String>) value;
            if(!CollectionUtils.isEmpty(collection)) {
                for(String ele: collection) {
                    boolean validate = XSSUtils.isValid(ele);
                    if(!validate) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

}
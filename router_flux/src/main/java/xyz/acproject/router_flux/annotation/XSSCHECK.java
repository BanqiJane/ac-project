package xyz.acproject.router_flux.annotation;

import xyz.acproject.router_flux.annotation.validator.XSSCheckValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Jane
 * @ClassName XSSCHECK
 * @Description TODO
 * @date 2021/6/9 9:53
 * @Copyright:2021
 */
@Documented
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {XSSCheckValidator.class} )
public @interface XSSCHECK {

    String message() default "char_not_validate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

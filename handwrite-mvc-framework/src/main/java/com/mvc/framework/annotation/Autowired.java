package com.mvc.framework.annotation;

import javax.xml.bind.Element;
import java.lang.annotation.*;

/**
 * @author study
 * @create 2019-11-06 20:31
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
//    String value();
}

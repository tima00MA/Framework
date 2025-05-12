package ma.fs.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark a class as a component/bean.
 * create instances
 */
@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime via reflection
@Target(ElementType.TYPE)           // Can only be used on classes (not methods or fields)
public @interface Component {


    String id() default "";
}
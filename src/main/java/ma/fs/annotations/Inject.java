package ma.fs.annotations;

import java.lang.annotation.*;


//Marks where a dependency should be injected (constructor, setter, or field).
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
public @interface Inject {
}
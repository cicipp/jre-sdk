package co.buybuddy.annotation.visibility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Shows that the annotated construct is not applicable for multi-threaded usage, in other words,
 * it is not thread-safe.
 * <p>
 * This methodology references Goetz et al, 'Java Concurrency in Practice'.
 *
 * @see ThreadSafe
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface NonThreadSafe {
}

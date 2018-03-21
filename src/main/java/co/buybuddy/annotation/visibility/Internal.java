package co.buybuddy.annotation.visibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks the internal APIs which ought to be used only in the boundaries of the implementation of the software
 * development kit. Client-side developers are expected to not depend on constructs annotated with this annotation
 * since they might be changed without an explicit notice.
 */
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface Internal {
}

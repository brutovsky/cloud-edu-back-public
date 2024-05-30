package com.nakytniak.backend.annotations;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(
        value = "",
        produces = "",
        consumes = "",
        authorizations = @Authorization(""),
        extensions = @Extension(name = "", properties = @ExtensionProperty(name = "", value = ""))
)
public @interface CustomApiOperation {
    String value();

    String produces() default "";

    String consumes() default "";

    Authorization[] authorizations() default @Authorization("");

    Extension[] extensions() default @Extension(
            name = "x-google-backend",
            properties = {
                    @ExtensionProperty(name = "address", value = "https://cloud-edu-backend-app-qgvd3vvgha-uc.a.run.app"),
                    @ExtensionProperty(name = "path_translation", value = "APPEND_PATH_TO_ADDRESS")
            }
    );
}

package com.github.hyota.asciiartboardreader.di.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Activityライフサイクルと生存期間を合わせるスコープ.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface ActivityScope {
}

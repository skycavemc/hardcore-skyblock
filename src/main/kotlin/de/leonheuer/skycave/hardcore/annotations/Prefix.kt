package de.leonheuer.skycave.hardcore.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Prefix(val value: String)
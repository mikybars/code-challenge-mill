package com.github.mikybars.challenge;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.containAnyMethodsThat;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.GeneralCodingRules.DEPRECATED_API_SHOULD_NOT_BE_USED;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.OLD_DATE_AND_TIME_CLASSES_SHOULD_NOT_BE_USED;

import com.github.mikybars.challenge.prices.adapters.in.rest.ApiUtil;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.github.mikybars.challenge", importOptions = DoNotIncludeTests.class)
class BestPracticesTest {

  static final DescribedPredicate<JavaClass> openApiGeneratedInterfaces =
      simpleNameEndingWith("Api")
          .and(containAnyMethodsThat(annotatedWith(RequestMapping.class)))
          .as("OpenAPI generated interfaces");

  @ArchTest
  ArchRule noFieldInjection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  ArchRule deprecatedApiShouldNotBeUsed = DEPRECATED_API_SHOULD_NOT_BE_USED;

  @ArchTest
  ArchRule noOldJavaApis = OLD_DATE_AND_TIME_CLASSES_SHOULD_NOT_BE_USED;

  @ArchTest
  void noGenericExceptions(JavaClasses javaClasses) {
    NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(
        javaClasses.that(are(not(belongToAnyOf(
            // TODO: skip generation of this file by OpenApi
            ApiUtil.class
        )))
    ));
  }

  @ArchTest
  ArchRule apiFirst = classes().that()
      .areAnnotatedWith(RestController.class).should().implement(openApiGeneratedInterfaces);
}

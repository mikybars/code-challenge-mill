package com.github.mikybars.challenge;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.importer.ImportOption.OnlyIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import com.tngtech.archunit.lang.syntax.elements.GivenMethodsConjunction;
import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "com.github.mikybars.challenge", importOptions = OnlyIncludeTests.class)
class TestingConventionsTest {

  static final DescribedPredicate<HasName> areTestClasses =
      nameEndingWith("Test").or(nameEndingWith("IT")).as("belong to tests");
  static final GivenClassesConjunction testClasses = classes().that(areTestClasses);
  static final GivenMethodsConjunction testMethods = methods().that()
      .areDeclaredInClassesThat(areTestClasses).and().areAnnotatedWith(Test.class);

  @ArchTest
  ArchRule noPublicClassesOrMethods = CompositeArchRule
      .of(testClasses.should().notBePublic())
      .and(testMethods.should().notBePublic());
}

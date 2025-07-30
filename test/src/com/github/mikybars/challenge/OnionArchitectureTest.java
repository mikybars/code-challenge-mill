package com.github.mikybars.challenge;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;
import static com.tngtech.archunit.lang.conditions.ArchConditions.accessClassesThat;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.github.mikybars.challenge", importOptions = DoNotIncludeTests.class)
class OnionArchitectureTest {

  @ArchTest
  ArchRule onionArchitectureIsRespected = onionArchitecture()
      .withOptionalLayers(true)
      .domainModels("..domain..")
      .applicationServices("..application..")
      .adapter("rest", "..adapters.in.rest..")
      .adapter("persistence", "..adapters.out.persistence..");

  @ArchTest
  ArchRule useCasesShouldNotDependOnOneAnother = noClasses().that()
      .implement(nameEndingWith("UseCase"))
      .should(accessClassesThat(nameEndingWith("UseCase")));
}

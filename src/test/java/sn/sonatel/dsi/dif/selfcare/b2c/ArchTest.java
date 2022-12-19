package sn.sonatel.dsi.dif.selfcare.b2c;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    @Disabled("Not yet implemented")
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sn.sonatel.dsi.dif.selfcare.b2c");
        /* noClasses()
            .that()
            .resideInAnyPackage("sn.sonatel.dsi.dif.selfcare.b2c.service..")
            .or()
            .resideInAnyPackage("sn.sonatel.dsi.dif.selfcare.b2c.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..sn.sonatel.dsi.dif.selfcare.b2c.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);*/
    }
}

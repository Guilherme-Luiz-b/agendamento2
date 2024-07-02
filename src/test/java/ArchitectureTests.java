import br.util.annotation.IgnoreMaxLinesCheck;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.LineNumberAttribute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.lang.reflect.Method;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "br.chronos")
public class ArchitectureTests {

    public static ArchCondition<JavaClass> satisfyEntitiesAnnotationsProperties() {
        return new ArchCondition<>("be annotated with @Table(name = 'tableName') and @Entity") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasTableAnnotation = false;
                boolean tableNameNotEmpty = false;
                boolean hasEntityAnnotation = false;
                boolean entityNameIsEmpty = false;
                boolean isSapClass;

                JavaClass originalClass = javaClass;

                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    isSapClass = javaClass.getSuperclass().get().getName().contains("COLUMN_INPUT");

                    while (!isSapClass && !javaClass.getSuperclass().get().getName().equals("java.lang.Object") && !javaClass.getSuperclass().get().getName().contains("java.lang.Enum")) {
                        if (javaClass.getSuperclass().get().getName().equals("br.chronos.sap.jco.JCoItem")) {
                            isSapClass = true;
                        } else {
                            javaClass = javaClass.getSuperclass().get().toErasure();
                        }
                    }

                    if (!isSapClass) {
                        for (JavaAnnotation<JavaClass> annotation : javaClass.getAnnotations()) {
                            if (annotation.getRawType().isAssignableTo(Table.class)) {
                                hasTableAnnotation = true;

                                if (!annotation.getProperties().get("name").toString().isEmpty()) {
                                    tableNameNotEmpty = true;
                                }
                            }

                            if (annotation.getRawType().isAssignableTo(Entity.class)) {
                                hasEntityAnnotation = true;

                                if (annotation.getProperties().get("name").toString().isEmpty()) {
                                    entityNameIsEmpty = true;
                                }
                            }
                        }

                        if (!hasTableAnnotation) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + originalClass.getSimpleName() + "' - should be annotated with @Table"));
                        }

                        if (!hasEntityAnnotation) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + originalClass.getSimpleName() + "' - should be annotated with @Entity"));
                        }

                        if (hasTableAnnotation && !tableNameNotEmpty) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + originalClass.getSimpleName() + "' - @Table must have 'name' property"));
                        }

                        if (hasEntityAnnotation && !entityNameIsEmpty) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + originalClass.getSimpleName() + "' - @Entity must haven't 'name' property"));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyIdFieldConditions() {
        return new ArchCondition<>("Verify Id field exists with Long type and be annotated with @Id and @GeneratedValue(strategy = GenerationType.IDENTITY)") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean idHasRequiredAnnotations = false;
                boolean isSapClass;

                JavaClass originalClass = javaClass;

                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    isSapClass = javaClass.getSuperclass().get().getName().contains("COLUMN_INPUT");

                    while (!isSapClass && !javaClass.getSuperclass().get().getName().equals("java.lang.Object") && !javaClass.getSuperclass().get().getName().contains("java.lang.Enum")) {
                        if (javaClass.getSuperclass().get().getName().equals("br.chronos.sap.jco.JCoItem")) {
                            isSapClass = true;
                        } else {
                            javaClass = javaClass.getSuperclass().get().toErasure();
                        }
                    }

                    if (!isSapClass) {
                        for (JavaField field : javaClass.getFields()) {
                            if (!idHasRequiredAnnotations) {
                                idHasRequiredAnnotations = field.getName().equals("id") && field.getType().getName().equals(Long.class.getName()) && field.isAnnotatedWith(Id.class) &&
                                        field.isAnnotatedWith(GeneratedValue.class) && field.getAnnotationOfType(GeneratedValue.class).strategy() == GenerationType.IDENTITY;
                            }
                        }

                        if (!idHasRequiredAnnotations) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + originalClass.getSimpleName() + "' - id field should be Long and be annotated with @Id and @GeneratedValue(strategy = GenerationType.IDENTITY)"));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyFieldsShouldHaveJakartaAnnotation() {
        return new ArchCondition<>("have Jakarta annotation") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    String superClassName = javaClass.getSuperclass().get().getName();

                    if (!superClassName.contains("COLUMN_INPUT") && !superClassName.equals("br.chronos.sap.jco.JCoItem")) {
                        for (JavaField field : javaClass.getFields()) {
                            boolean hasJakartaAnnotation = false;

                            for (JavaAnnotation<JavaField> annotation : field.getAnnotations()) {
                                if (annotation.getRawType().getName().startsWith("javax.persistence.")) {
                                    hasJakartaAnnotation = true;
                                }
                            }

                            if (!hasJakartaAnnotation) {
                                events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Field: '" + field.getName() + "' - should be annotated with Jakarta annotation"));
                            }
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyRepositoryConditions() {
        return new ArchCondition<>("be interface and extends JpaRepository") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    boolean extendsJpaRepository = javaClass.getInterfaces().stream().anyMatch(interfaceType -> interfaceType.toErasure().isAssignableTo("org.springframework.data.jpa.repository.JpaRepository"));

                    if (!javaClass.isInterface() && !extendsJpaRepository) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should be interface and extends JpaRepository"));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllersMethodsShouldHaveWebAnnotation() {
        return new ArchCondition<>("have Web annotation") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaMethod method : javaClass.getMethods()) {
                        boolean hasSpringAnnotation = false;

                        if (!method.getAnnotations().isEmpty()) {
                            for (JavaAnnotation<JavaMethod> annotation : method.getAnnotations()) {
                                if (annotation.getRawType().getPackage().getName().startsWith("org.springframework.web.bind.annotation")) {
                                    hasSpringAnnotation = true;
                                }

                                if (!hasSpringAnnotation) {
                                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - should have Web annotation"));
                                }
                            }
                        } else {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - should have Web annotation"));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyMethodsMustNotExceedMaxLines(int maxLines) {
        return new ArchCondition<>("method lines must not exceed " + maxLines + " lines") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    if (!javaClass.isInterface() && !javaClass.getName().contains("SAPService")) {
                        try {
                            CtClass ctClass = ClassPool.getDefault().get(javaClass.getName());

                            for (Method method : javaClass.reflect().getDeclaredMethods()) {
                                if (!method.isAnnotationPresent(IgnoreMaxLinesCheck.class) && !method.getName().contains("lambda$")) {
                                    CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName());

                                    int methodLines = ((LineNumberAttribute) ctMethod.getMethodInfo().getCodeAttribute().getAttribute(LineNumberAttribute.tag)).tableLength();

                                    if (methodLines > maxLines) {
                                        events.add(SimpleConditionEvent.violated(method, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - lines must not exceed " + maxLines + " lines"));
                                    }
                                }
                            }
                        } catch (NotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllersMethodsIsPublic() {
        return new ArchCondition<>("be public methods") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaMethod method : javaClass.getMethods()) {
                        if (!method.getModifiers().contains(JavaModifier.PUBLIC)) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - should be public"));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyFieldsIsPrivate() {
        return new ArchCondition<>("be private fields") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (!javaClass.getSimpleName().endsWith("Builder") && javaClass.getSuperclass().isPresent()) {
                    String superClassName = javaClass.getSuperclass().get().getName();

                    if (!superClassName.contains("COLUMN_INPUT") && !superClassName.equals("br.chronos.sap.jco.JCoItem")) {
                        for (JavaField field : javaClass.getFields()) {
                            if (!field.getModifiers().contains(JavaModifier.PRIVATE)) {
                                events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Field: '" + field.getName() + "' - should be private"));
                            }
                        }
                    }
                }

            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllersMethodsReturnResponseEntity() {
        return new ArchCondition<>("return ResponseEntity") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaMethod method : javaClass.getMethods()) {
                        if (!method.getReturnType().toErasure().isAssignableTo(ResponseEntity.class)) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - should return ResponseEntity"));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyRepositoriesParametersMethodsHasParamAnnotation() {
        return new ArchCondition<>("has @Param in parameters") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                for (JavaMethod method : javaClass.getMethods()) {
                    boolean hasQueryAnnotation = false;

                    for (JavaAnnotation<JavaMethod> annotation : method.getAnnotations()) {
                        if (annotation.getRawType().getName().equals(Query.class.getName())) {
                            hasQueryAnnotation = true;
                        }
                    }

                    if (hasQueryAnnotation) {
                        boolean allParamsHaveParamAnnotation = true;

                        for (JavaParameter parameter : method.getParameters()) {
                            boolean hasParamAnnotation = false;
                            String paramName = method.reflect().getParameters()[parameter.getIndex()].getName();

                            if (!parameter.getAnnotations().isEmpty()) {
                                for (JavaAnnotation<JavaParameter> paramAnnotation : parameter.getAnnotations()) {
                                    if (paramAnnotation.getRawType().getName().equals(Param.class.getName())) {
                                        hasParamAnnotation = true;
                                        break;
                                    }
                                }

                                if (!hasParamAnnotation) {
                                    allParamsHaveParamAnnotation = false;
                                }

                                if (!allParamsHaveParamAnnotation) {
                                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - Parameter - '" + paramName + "' - should has @Param"));
                                }
                            } else if (!parameter.getType().toErasure().isAssignableTo(Pageable.class)) {
                                events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - Method: '" + method.getName() + "' - Parameter - '" + paramName + "' - should has @Param"));
                            }
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyServiceAnnotations() {
        return new ArchCondition<>("be annotated with @Service and not be annotated with @Transactional") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasServiceAnnotation = false;
                boolean hasTransactionalAnnotation = false;

                if (!javaClass.isInterface() && javaClass.getSuperclass().isPresent()) {
                    if (!javaClass.getSuperclass().get().getName().contains("COLUMN_INPUT")) {
                        for (JavaAnnotation<JavaClass> annotation : javaClass.getAnnotations()) {
                            if (annotation.getRawType().isAssignableTo(Service.class)) {
                                hasServiceAnnotation = true;
                            }

                            if (annotation.getRawType().isAssignableTo(Transactional.class)) {
                                hasTransactionalAnnotation = true;
                            }
                        }

                        if (!hasServiceAnnotation) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should be annotated with @Service"));
                        }

                        if (hasTransactionalAnnotation) {
                            events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should not be annotated with @Transactional"));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyRepositoryHasCorrectEndingName() {
        return new ArchCondition<>("ends with Repository") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes") && !javaClass.getSimpleName().endsWith("Repository")) {
                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should ends with Repository"));
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllerHasCorrectEndingName() {
        return new ArchCondition<>("ends with Controller") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes") && !javaClass.getSimpleName().endsWith("Controller")) {
                    events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should ends with Controller"));
                }
            }
        };
    }

    public static ArchCondition<JavaClass> satisfyControllerAnnotations() {
        return new ArchCondition<>("be annotated with @RestController and @RequestMapping") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                boolean hasRestControllerAnnotation = false;
                boolean hasRequestMappingAnnotation = false;

                if (javaClass.getSource().isPresent() && !javaClass.getSource().get().getUri().getPath().contains("test-classes")) {
                    for (JavaAnnotation<JavaClass> annotation : javaClass.getAnnotations()) {
                        if (annotation.getRawType().isAssignableTo(RestController.class)) {
                            hasRestControllerAnnotation = true;
                        }

                        if (annotation.getRawType().isAssignableTo(RequestMapping.class)) {
                            hasRequestMappingAnnotation = true;
                        }
                    }

                    if (!hasRestControllerAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should be annotated with @RestController"));
                    }

                    if (!hasRequestMappingAnnotation) {
                        events.add(SimpleConditionEvent.violated(javaClass, "Class: '" + javaClass.getSimpleName() + "' - should be annotated with @RequestMapping"));
                    }
                }
            }
        };
    }

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyRepositoryHasCorrectEndingName = classes()
            .that().resideInAPackage("..repository..")
            .should(satisfyRepositoryHasCorrectEndingName())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShouldHaveSimpleNameEndingWith_Service = classes()
            .that().resideInAPackage("..service..")
            .and().haveSimpleNameEndingWith("Service")
            .should().haveSimpleNameEndingWith("Service")
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllerHasCorrectEndingName = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllerHasCorrectEndingName())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShouldOnlyBeAccessedBy_Service_Test_Security = classes()
            .that().resideInAPackage("..repository..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..test..", "..security..")
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShouldOnlyBeAccessedBy_Service_Controller_Test_Security = classes()
            .that().resideInAPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..controller..", "..test..", "..security..")
            .allowEmptyShould(true);


    @ArchTest
    public static final ArchRule entitiesShould_SatisfyEntitiesAnnotationsProperties = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyEntitiesAnnotationsProperties())
            .allowEmptyShould(true);


    @ArchTest
    public static final ArchRule entitiesShould_satisfyIdFieldAnnotations = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyIdFieldConditions())
            .allowEmptyShould(true);


    @ArchTest
    public static final ArchRule entitiesShould_SatisfyFieldsShouldHaveJakartaAnnotation = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyFieldsShouldHaveJakartaAnnotation())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyRepositoryConditions = classes()
            .that().resideInAPackage("..repository..")
            .should(satisfyRepositoryConditions())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShould_SatisfyServiceAnnotations = classes()
            .that().resideInAPackage("..service..")
            .and().haveSimpleNameEndingWith("Service")
            .should(satisfyServiceAnnotations())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShouldBeAnnotatedWith_RequestMapping_RestController = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllerAnnotations())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllersShouldHaveWebAnnotation = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllersMethodsShouldHaveWebAnnotation())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule enumsShouldBe_Enum = classes()
            .that().resideInAPackage("..enums..")
            .should().beEnums()
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule dtosShouldNotBeAnnotatedWith_Entity_Table = classes()
            .that().resideInAPackage("..dto..")
            .should().notBeAnnotatedWith(Entity.class)
            .andShould().notBeAnnotatedWith(Table.class)
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllerShould_SatisfyMethodsMustNotExceedMaxLines = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyMethodsMustNotExceedMaxLines(5))
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule servicesShould_SatisfyMethodsMustNotExceedMaxLines = classes()
            .that().resideInAPackage("..service..")
            .should(satisfyMethodsMustNotExceedMaxLines(15))
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllersMethodsIsPublic = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllersMethodsIsPublic())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule entitiesShould_SatisfyFieldsIsPrivate = classes()
            .that().resideInAPackage("..entity..")
            .should(satisfyFieldsIsPrivate())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule dtosShould_SatisfyFieldsIsPrivate = classes()
            .that().resideInAPackage("..dto..")
            .should(satisfyFieldsIsPrivate())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule controllersShould_SatisfyControllersMethodsReturnResponseEntity = classes()
            .that().resideInAPackage("..controller..")
            .should(satisfyControllersMethodsReturnResponseEntity())
            .allowEmptyShould(true);

    @ArchTest
    public static final ArchRule repositoriesShould_SatisfyRepositoriesParametersMethodsHasParamAnnotation = classes()
            .that().resideInAPackage("..repository..")
            .should(satisfyRepositoriesParametersMethodsHasParamAnnotation())
            .allowEmptyShould(true);

}
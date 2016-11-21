package com.ensi.pcd.web.rest.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by yosr on 5/1/16.
 */

public class UML2 {
    public static boolean DEBUG = true;

    protected static final ResourceSet RESOURCE_SET = new ResourceSetImpl();

    protected static void out(String output) {

        if (DEBUG) {
            System.out.println(output);
        }
    }

    protected static void err(String error) {
        System.err.println(error);
    }

    // register the resource factories used when load the model
    protected static void registerResourceFactories() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
            UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
    }

    /******       Creation methods
     *
     ******/
    protected static Model createModel(String name) {
        Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(name);

        out("Model '" + model.getQualifiedName() + "' created.");

        return model;
    }

    protected static org.eclipse.uml2.uml.Package createPackage(
        org.eclipse.uml2.uml.Package nestingPackage, String name) {
        org.eclipse.uml2.uml.Package package_ = nestingPackage
            .createNestedPackage(name);

        out("Package '" + package_.getQualifiedName() + "' created.");

        return package_;
    }

    protected static PrimitiveType createPrimitiveType(
        org.eclipse.uml2.uml.Package package_, String name) {
        PrimitiveType primitiveType = (PrimitiveType) package_
            .createOwnedPrimitiveType(name);

        out("Primitive type '" + primitiveType.getQualifiedName()
            + "' created.");

        return primitiveType;
    }

    protected static org.eclipse.uml2.uml.Actor createActor(
        org.eclipse.uml2.uml.Model package_, String name) {
        Actor actor = (Actor) package_.createPackagedElement(
            name, UMLPackage.eINSTANCE.getActor());

        out("Actor '" + actor.getQualifiedName()
            + "' created.");

        return actor;
    }

    protected static org.eclipse.uml2.uml.UseCase createUseCase(
        org.eclipse.uml2.uml.Model package_, String name){

        UseCase _useCase = (UseCase) package_.createPackagedElement(
            name, UMLPackage.eINSTANCE.getUseCase());

        out("Use Case '" + _useCase.getQualifiedName()
            + "' created.");

        return _useCase;
    }

    protected static Generalization createGeneralization(
        Classifier specificClassifier, Classifier generalClassifier) {
        Generalization generalization = specificClassifier.createGeneralization(generalClassifier);

        out("Generalization " + specificClassifier.getQualifiedName() + " ->> "
            + generalClassifier.getQualifiedName() + " created.");

        return generalization;
    }

    protected static Association createAssociation(Type type1,
                                                   boolean end1IsNavigable, AggregationKind end1Aggregation,
                                                   String end1Name, int end1LowerBound, int end1UpperBound,
                                                   Type type2, boolean end2IsNavigable,
                                                   AggregationKind end2Aggregation, String end2Name,
                                                   int end2LowerBound, int end2UpperBound) {

        Association association = type1.createAssociation(end1IsNavigable,
            end1Aggregation, end1Name, end1LowerBound, end1UpperBound, type2,
            end2IsNavigable, end2Aggregation, end2Name, end2LowerBound,
            end2UpperBound);

        StringBuffer sb = new StringBuffer();

        sb.append("Association ");

        if (null == end1Name || 0 == end1Name.length()) {
            sb.append('{');
            sb.append(type1.getQualifiedName());
            sb.append('}');
        } else {
            sb.append("'");
            sb.append(type1.getQualifiedName());
            sb.append(NamedElement.SEPARATOR);
            sb.append(end1Name);
            sb.append("'");
        }

        sb.append(" [");
        sb.append(end1LowerBound);
        sb.append("..");
        sb.append(LiteralUnlimitedNatural.UNLIMITED == end1UpperBound
            ? "*"
            : String.valueOf(end1UpperBound));
        sb.append("] ");

        sb.append(end2IsNavigable
            ? '<'
            : '-');
        sb.append('-');
        sb.append(end1IsNavigable
            ? '>'
            : '-');
        sb.append(' ');

        if (null == end2Name || 0 == end2Name.length()) {
            sb.append('{');
            sb.append(type2.getQualifiedName());
            sb.append('}');
        } else {
            sb.append("'");
            sb.append(type2.getQualifiedName());
            sb.append(NamedElement.SEPARATOR);
            sb.append(end2Name);
            sb.append("'");
        }

        sb.append(" [");
        sb.append(end2LowerBound);
        sb.append("..");
        sb.append(LiteralUnlimitedNatural.UNLIMITED == end2UpperBound
            ? "*"
            : String.valueOf(end2UpperBound));
        sb.append("]");

        sb.append(" created.");

        out(sb.toString());

        return association;
    }

    protected static org.eclipse.uml2.uml.Extend createExtend(String name,
                                                              UseCase extendedCase){
        Extend extend = createExtend("",null);
        extend.setExtendedCase(extendedCase);
        extend.setName(name);

        out("Extend '" + extend.getQualifiedName()
            + "' created.");

        return extend;
    }

    protected static void save(org.eclipse.uml2.uml.Package package_, URI uri) {
        Resource resource = RESOURCE_SET.createResource(uri);
        EList contents = resource.getContents();

        contents.add(package_);

        for (Iterator allContents = UMLUtil.getAllContents(package_, true,
            false); allContents.hasNext();) {

            EObject eObject = (EObject) allContents.next();

            if (eObject instanceof Element) {
                contents.addAll(((Element) eObject).getStereotypeApplications());
            }
        }

        try {
            resource.save(null);

            out("Done.");
        } catch (IOException ioe) {
            err(ioe.getMessage());
        }
    }
}

package com.ensi.pcd.web.rest.util;

/**
 * Created by yosr on 5/1/16.
 */

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UseCase;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
/**
 * Created by yosr on 29/3/16.
 *
 * Modified by oumaima on 10/04/16
 *
 **/


//for saveasxmi
//for the hashtable

public class TransformationUtil extends UML2 {

    private final static Logger log = LoggerFactory.getLogger(TransformationUtil.class);
    /*public TransformationUtil() throws JDOMException, IOException {
    }*/

    public static Model XMLToXMI(String xml) throws JDOMException, IOException {
        registerResourceFactories();
        //Creating model
        Model UCD = createModel("UseCaseDiag");

        //out("Creating primitive types...");
        //PrimitiveType intPrimitiveType = createPrimitiveType(UCD, "int");

//-------------------------------     getting xml elements from jsuml2  -------------------------------
        //defining the SAX builder
        SAXBuilder builder = new SAXBuilder();
         //Document doc = (Document) builder.build(xmlFile);

        //String xml = "<umldiagrams><UMLUseCaseDiagram name=\"Use case diagram\" backgroundNodes=\"#bbffff\"><UMLUseCase id=\"UMLUseCase_2\" x=\"485\" y=\"346\" width=\"193.993944\" height=\"40\" backgroundColor=\"#bbffff\" lineColor=\"#294253\" lineWidth=\"1\" tagValues=\"\"><superitem id=\"stereotypes\" visibleSubComponents=\"true\"/><item id=\"name\" value=\"Validate diagram\"/></UMLUseCase><UMLUseCase id=\"UMLUseCase_1\" x=\"488\" y=\"246\" width=\"147.667032\" height=\"40\" backgroundColor=\"#bbffff\" lineColor=\"#294253\" lineWidth=\"1\" tagValues=\"\"><superitem id=\"stereotypes\" visibleSubComponents=\"true\"/><item id=\"name\" value=\"Draw diagram\"/></UMLUseCase><UMLActor id=\"UMLActor_0\" x=\"233\" y=\"233\" width=\"50\" height=\"70\" backgroundColor=\"#bbffff\" tagValues=\"\"><superitem id=\"stereotypes\" visibleSubComponents=\"true\"/><item id=\"name\" value=\"user\"/></UMLActor><UMLCommunication id=\"UMLCommunication_0\" side_A=\"UMLUseCase_1\" side_B=\"UMLActor_0\"><point x=\"488.02179049035465\" y=\"266.48586954119725\"/><point x=\"283\" y=\"267.83543619328685\"/><superitem id=\"stereotype\" visibleSubComponents=\"true\"/><item id=\"multiplicityA\" value=\"\"/><item id=\"multiplicityB\" value=\"\"/></UMLCommunication><UMLCommunication id=\"UMLCommunication_1\" side_A=\"UMLActor_0\" side_B=\"UMLUseCase_2\"><point x=\"283\" y=\"275.56179906520856\"/><point x=\"527.3620387336983\" y=\"349.47446450796593\"/><superitem id=\"stereotype\" visibleSubComponents=\"true\"/><item id=\"multiplicityA\" value=\"\"/><item id=\"multiplicityB\" value=\"\"/></UMLCommunication></UMLUseCaseDiagram></umldiagrams>";
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        try{
        Document doc = builder.build(stream);

        //getting the root element: umldiagrams -->message
        org.jdom2.Element rootnode = doc.getRootElement();

        //getting the child(type of the UML diagram) --USECase
        org.jdom2.Element typediag = rootnode.getChild("UMLUseCaseDiagram");

        //getting the child's children--UseCase Diagram elements
        List<org.jdom2.Element> umlcases = typediag.getChildren("UMLUseCase");
        List<org.jdom2.Element> umlactors = typediag.getChildren("UMLActor");
        List<org.jdom2.Element> umlcommunications = typediag.getChildren("UMLCommunication");
        List<org.jdom2.Element> umlextends = typediag.getChildren("UMLExtended");
        List<org.jdom2.Element> umlincludes = typediag.getChildren("UMLInclude");
        List<org.jdom2.Element> umlGeneralizations = typediag.getChildren("UMLGeneralization");

        //working on umlcases

        //creating the hashtable for uml use cases
        Hashtable<String, UseCase> htusecases = new Hashtable<String, UseCase>();

        for (int i = 0; i <= (umlcases.size() - 1); i++) {
            org.jdom2.Element umlcase = (org.jdom2.Element) umlcases.get(i);

            //creating the matrix of usecases
            String[][] attr_case = new String[umlcases.size() + 1][4];

            //creating the matrix
            for (int j = 1; j < 4; j++) {
                if (j == 1) {
                    attr_case[i][j] = umlcase.getAttributeValue("id");
                }

                if (j == 2) {
                    attr_case[i][j] = umlcase.getAttributeValue("backgroundColor");
                }

                if (j == 3) {
                    //getting the name of the usecase (its value)
                    org.jdom2.Element usecaseitem = umlcase.getChild("item");
                    attr_case[i][j] = usecaseitem.getAttributeValue("value");

                    //Creating use cases
                    org.eclipse.uml2.uml.UseCase usecase1 = createUseCase(UCD, attr_case[i][j]);
                    System.out.println("usecase created");

                    //inserting into the hashtable of usecases
                    htusecases.put(attr_case[i][1], usecase1);
                }
            }
        }
        //-------------------------              to add variables

        //working on umlactors

        //creating the hashtable for actors
        Hashtable<String, Actor> htactors = new Hashtable<String, Actor>();

        for (int a = 0; a <= (umlactors.size() - 1); a++) {
            org.jdom2.Element umlactor = (org.jdom2.Element) umlactors.get(a);

            //creating the matrix of actors
            String[][] attr_actor = new String[umlactors.size() + 1][3 + 1];


            for (int j = 1; j < 4; j++) {
                if (j == 1) {

                    attr_actor[a][j] = umlactor.getAttributeValue("id");

                }
                if (j == 2) {
                    attr_actor[a][j] = umlactor.getAttributeValue("backgroundColor");

                }
                if (j == 3) {
                    //getting the name of the actor (its value)
                    org.jdom2.Element actoritem = umlactor.getChild("item");
                    attr_actor[a][j] = actoritem.getAttributeValue("value");

                    //Creating actors
                    if(attr_actor[a][j] != null){
                        org.eclipse.uml2.uml.Actor actor1 = createActor(UCD, attr_actor[a][j]);
                        //inserting into the hashtable of usecases
                        htactors.put(attr_actor[a][1], actor1);
                    }
                }
            }
        }

        //working on umlcommunications (ep:associations)
        for (int c = 0; c <= (umlcommunications.size() - 1); c++) {
            org.jdom2.Element umlcommunication = (org.jdom2.Element) umlcommunications.get(c);
            String communicationside_B = umlcommunication.getAttributeValue("side_B");
            String communicationside_A = umlcommunication.getAttributeValue("side_A");
            String umlcommunicationid = umlcommunication.getAttributeValue("id");

            //knowing the type of each side

            if (htactors.containsKey(communicationside_A)) {
                if (htusecases.containsKey(communicationside_B)) {
                    createAssociation(htactors.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                        htusecases.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);

                }

                if (htactors.containsKey(communicationside_B)) {
                    createAssociation(htactors.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                        htactors.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);
                }
            }

            if (htusecases.containsKey(communicationside_A)) {
                if (htactors.containsKey(communicationside_B)) {
                    createAssociation(htusecases.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                        htactors.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);
                }

                if (htusecases.containsKey(communicationside_B)) {
                    createAssociation(htusecases.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                        htusecases.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);
                }
            }
        }

        // createAssociation(side_A, true, AggregationKind.NONE_LITERAL, "", 0, 1,
        //side_B, false, AggregationKind.NONE_LITERAL, "", 0, 1);

//---------------------------------------------------!!!!actor1 is not a string
        //working on umlextends
        for (int k = 0; k <= (umlextends.size() - 1); k++) {
            org.jdom2.Element umlextend = (org.jdom2.Element) umlextends.get(k);
            String extendside_B = umlextend.getAttributeValue("side_B");
            String extendside_A = umlextend.getAttributeValue("side_A");
            String extendid = umlextend.getAttributeValue("id");

            if (htusecases.containsKey(extendside_A)) {
                if (htusecases.containsKey(extendside_B)) {
                    UseCase usecase1 = htusecases.get(extendside_A);
                    usecase1.createExtensionPoint("ExtPt");
                    UseCase usecase11 = htusecases.get(extendside_B);
                    usecase11.createExtend("Extend", usecase1);
                    //usecase11.valida
                }
            }
        }

        //working on umlincludes
        for (int k = 0; k <= (umlincludes.size() - 1); k++) {
            org.jdom2.Element umlinclude = (org.jdom2.Element) umlincludes.get(k);
            String extendside_B = umlinclude.getAttributeValue("side_B");
            String extendside_A = umlinclude.getAttributeValue("side_A");
            String includeid = umlinclude.getAttributeValue("id");

            //----------!!!!!!!!!!not so sure because usecase1 is not supposed to be string->to be checked
				   /*the one written by yosr:
				    * Creating includes
				    *     usecase1.createInclude("inc111",usecase2);  -----param2: use case included
				    */
            //knowing the type of each side
            //include is not definef for an actor

            if (htusecases.containsKey(extendside_A)) {
                if (htusecases.containsKey(extendside_B)) {

                    UseCase usecase1 = htusecases.get(extendside_A);
                    UseCase usecase11 = htusecases.get(extendside_B);
                    usecase1.createInclude("inc111", usecase11);
                }
            }
        }
        //Creating includes
        //   extendside_A.createInclude(side_A,side_B);  //param2: use case included

        //working on umlGeneralizations
        for (int n = 0; n <= (umlGeneralizations.size() - 1); n++) {
            org.jdom2.Element umlGeneralization = (org.jdom2.Element) umlGeneralizations.get(n);
            String Generalizationside_B = umlGeneralization.getAttributeValue("side_B");
            String Generalizationside_A = umlGeneralization.getAttributeValue("side_A");
            String Generalizationsid = umlGeneralization.getAttributeValue("id");

            //knowing the type of each side

            /*if (htactors.containsKey(Generalizationside_A)) {
                if (htusecases.containsKey(Generalizationside_B)) {
                    createGeneralization(htactors.get(Generalizationside_A), htusecases.get(Generalizationside_B));

                }

                if (htactors.containsKey(Generalizationside_A)) {
                    createGeneralization(htactors.get(Generalizationside_A), htactors.get(Generalizationside_B));
                }
            }

            if (htusecases.containsKey(Generalizationside_A)) {
                if (htactors.containsKey(Generalizationside_B)) {
                    createGeneralization(htusecases.get(Generalizationside_A), htactors.get(Generalizationside_B));

                }
                if (htusecases.containsKey(Generalizationside_B)) {
                    createGeneralization(htusecases.get(Generalizationside_A), htusecases.get(Generalizationside_B));
                }
            }*/
            if(htactors.containsKey(Generalizationside_A))
            {
                if(htusecases.containsKey(Generalizationside_B))
                {
                    out("A use case can't be generalized/ inherited from a use case!");

                }

                if(htactors.containsKey(Generalizationside_A))
                {
                    createGeneralization(htactors.get(Generalizationside_A),htactors.get(Generalizationside_B));
                }
            }

            if(htusecases.containsKey(Generalizationside_A))
            {
                if(htactors.containsKey(Generalizationside_B))
                {
                    out("A use case can't be generalized/ inherited from a use case!");
                }
                if(htusecases.containsKey(Generalizationside_B))
                {
                    createGeneralization(htusecases.get(Generalizationside_A),htusecases.get(Generalizationside_B));
                }

            }
        }
        }
        catch (JDOMException e) {
            // handle JDOMException
        }
        catch (IOException e) {
            // handle IOException
        }
        /*model = UCD;
        Hashtable<Model, String> modelErrors = new Hashtable<Model, String>();
        modelErrors.put(model, errorsReport);*/
        return UCD;
    }

    public static String XMLToXMIErrors(String xml) throws JDOMException, IOException {
        registerResourceFactories();

        //
        String errorsReport = null;
        //Creating model
        Model UCD = createModel("UseCaseDiag");

        //out("Creating primitive types...");
        //PrimitiveType intPrimitiveType = createPrimitiveType(UCD, "int");

//-------------------------------     getting xml elements from jsuml2  -------------------------------
        //defining the SAX builder
        SAXBuilder builder = new SAXBuilder();
        //Document doc = (Document) builder.build(xmlFile);

        //String xml = "<umldiagrams><UMLUseCaseDiagram name=\"Use case diagram\" backgroundNodes=\"#bbffff\"><UMLUseCase id=\"UMLUseCase_2\" x=\"485\" y=\"346\" width=\"193.993944\" height=\"40\" backgroundColor=\"#bbffff\" lineColor=\"#294253\" lineWidth=\"1\" tagValues=\"\"><superitem id=\"stereotypes\" visibleSubComponents=\"true\"/><item id=\"name\" value=\"Validate diagram\"/></UMLUseCase><UMLUseCase id=\"UMLUseCase_1\" x=\"488\" y=\"246\" width=\"147.667032\" height=\"40\" backgroundColor=\"#bbffff\" lineColor=\"#294253\" lineWidth=\"1\" tagValues=\"\"><superitem id=\"stereotypes\" visibleSubComponents=\"true\"/><item id=\"name\" value=\"Draw diagram\"/></UMLUseCase><UMLActor id=\"UMLActor_0\" x=\"233\" y=\"233\" width=\"50\" height=\"70\" backgroundColor=\"#bbffff\" tagValues=\"\"><superitem id=\"stereotypes\" visibleSubComponents=\"true\"/><item id=\"name\" value=\"user\"/></UMLActor><UMLCommunication id=\"UMLCommunication_0\" side_A=\"UMLUseCase_1\" side_B=\"UMLActor_0\"><point x=\"488.02179049035465\" y=\"266.48586954119725\"/><point x=\"283\" y=\"267.83543619328685\"/><superitem id=\"stereotype\" visibleSubComponents=\"true\"/><item id=\"multiplicityA\" value=\"\"/><item id=\"multiplicityB\" value=\"\"/></UMLCommunication><UMLCommunication id=\"UMLCommunication_1\" side_A=\"UMLActor_0\" side_B=\"UMLUseCase_2\"><point x=\"283\" y=\"275.56179906520856\"/><point x=\"527.3620387336983\" y=\"349.47446450796593\"/><superitem id=\"stereotype\" visibleSubComponents=\"true\"/><item id=\"multiplicityA\" value=\"\"/><item id=\"multiplicityB\" value=\"\"/></UMLCommunication></UMLUseCaseDiagram></umldiagrams>";
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        try{
            Document doc = builder.build(stream);

            //getting the root element: umldiagrams -->message
            org.jdom2.Element rootnode = doc.getRootElement();

            //getting the child(type of the UML diagram) --USECase
            org.jdom2.Element typediag = rootnode.getChild("UMLUseCaseDiagram");

            //getting the child's children--UseCase Diagram elements
            List<org.jdom2.Element> umlcases = typediag.getChildren("UMLUseCase");
            List<org.jdom2.Element> umlactors = typediag.getChildren("UMLActor");
            List<org.jdom2.Element> umlcommunications = typediag.getChildren("UMLCommunication");
            List<org.jdom2.Element> umlextends = typediag.getChildren("UMLExtended");
            List<org.jdom2.Element> umlincludes = typediag.getChildren("UMLInclude");
            List<org.jdom2.Element> umlGeneralizations = typediag.getChildren("UMLGeneralization");

            //working on umlcases

            //creating the hashtable for uml use cases
            Hashtable<String, UseCase> htusecases = new Hashtable<String, UseCase>();

            for (int i = 0; i <= (umlcases.size() - 1); i++) {
                org.jdom2.Element umlcase = (org.jdom2.Element) umlcases.get(i);

                //creating the matrix of usecases
                String[][] attr_case = new String[umlcases.size() + 1][4];

                //creating the matrix
                for (int j = 1; j < 4; j++) {
                    if (j == 1) {
                        attr_case[i][j] = umlcase.getAttributeValue("id");
                    }

                    if (j == 2) {
                        attr_case[i][j] = umlcase.getAttributeValue("backgroundColor");
                    }

                    if (j == 3) {
                        //getting the name of the usecase (its value)
                        org.jdom2.Element usecaseitem = umlcase.getChild("item");
                        attr_case[i][j] = usecaseitem.getAttributeValue("value");

                        //Creating use cases
                        org.eclipse.uml2.uml.UseCase usecase1 = createUseCase(UCD, attr_case[i][j]);
                        System.out.println("usecase created");

                        //inserting into the hashtable of usecases
                        htusecases.put(attr_case[i][1], usecase1);
                    }
                }
            }
            //-------------------------              to add variables

            //working on umlactors

            //creating the hashtable for actors
            Hashtable<String, Actor> htactors = new Hashtable<String, Actor>();

            for (int a = 0; a <= (umlactors.size() - 1); a++) {
                org.jdom2.Element umlactor = (org.jdom2.Element) umlactors.get(a);

                //creating the matrix of actors
                String[][] attr_actor = new String[umlactors.size() + 1][3 + 1];


                for (int j = 1; j < 4; j++) {
                    if (j == 1) {

                        attr_actor[a][j] = umlactor.getAttributeValue("id");

                    }
                    if (j == 2) {
                        attr_actor[a][j] = umlactor.getAttributeValue("backgroundColor");

                    }
                    if (j == 3) {
                        //getting the name of the actor (its value)
                        org.jdom2.Element actoritem = umlactor.getChild("item");
                        attr_actor[a][j] = actoritem.getAttributeValue("value");

                        //Creating actors
                        if(attr_actor[a][j] != null){
                            org.eclipse.uml2.uml.Actor actor1 = createActor(UCD, attr_actor[a][j]);
                            //inserting into the hashtable of usecases
                            htactors.put(attr_actor[a][1], actor1);
                        } else{
                            errorsReport = errorsReport +"\n  Actor must have a name";
                        }

                    }
                }
            }

            //working on umlcommunications (ep:associations)
            for (int c = 0; c <= (umlcommunications.size() - 1); c++) {
                org.jdom2.Element umlcommunication = (org.jdom2.Element) umlcommunications.get(c);
                String communicationside_B = umlcommunication.getAttributeValue("side_B");
                String communicationside_A = umlcommunication.getAttributeValue("side_A");
                String umlcommunicationid = umlcommunication.getAttributeValue("id");

                //knowing the type of each side

                if (htactors.containsKey(communicationside_A)) {
                    if (htusecases.containsKey(communicationside_B)) {
                        createAssociation(htactors.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                            htusecases.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);

                    }

                    if (htactors.containsKey(communicationside_B)) {
                        createAssociation(htactors.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                            htactors.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);
                    }
                }

                if (htusecases.containsKey(communicationside_A)) {
                    if (htactors.containsKey(communicationside_B)) {
                        createAssociation(htusecases.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                            htactors.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);
                    }

                    if (htusecases.containsKey(communicationside_B)) {
                        createAssociation(htusecases.get(communicationside_A), true, AggregationKind.NONE_LITERAL, "", 0, 1,
                            htusecases.get(communicationside_B), false, AggregationKind.NONE_LITERAL, "", 0, 1);
                    }
                }
            }

            // createAssociation(side_A, true, AggregationKind.NONE_LITERAL, "", 0, 1,
            //side_B, false, AggregationKind.NONE_LITERAL, "", 0, 1);

//---------------------------------------------------!!!!actor1 is not a string
            //working on umlextends
            for (int k = 0; k <= (umlextends.size() - 1); k++) {
                org.jdom2.Element umlextend = (org.jdom2.Element) umlextends.get(k);
                String extendside_B = umlextend.getAttributeValue("side_B");
                String extendside_A = umlextend.getAttributeValue("side_A");
                String extendid = umlextend.getAttributeValue("id");

                if (htusecases.containsKey(extendside_A)) {
                    if (htusecases.containsKey(extendside_B)) {
                        UseCase usecase1 = htusecases.get(extendside_A);
                        usecase1.createExtensionPoint("ExtPt");
                        UseCase usecase11 = htusecases.get(extendside_B);
                        usecase11.createExtend("Extend", usecase1);
                    }
                    if(htactors.containsKey(extendside_B)){
                        errorsReport = errorsReport + "error use case cannot be extended from an actor";
                    }
                }
                if (htactors.containsKey(extendside_A)){
                    if (htusecases.containsKey(extendside_B)) {
                    errorsReport = errorsReport + "error an actor cannot b extended from a use case";
                    }
                    if (htactors.containsKey(extendside_B)){
                        errorsReport = errorsReport + "error an actor cannot be extended from an actor";
                    }
                }
            }

            //working on umlincludes
            for (int k = 0; k <= (umlincludes.size() - 1); k++) {
                org.jdom2.Element umlinclude = (org.jdom2.Element) umlincludes.get(k);
                String extendside_B = umlinclude.getAttributeValue("side_B");
                String extendside_A = umlinclude.getAttributeValue("side_A");
                String includeid = umlinclude.getAttributeValue("id");

                //----------!!!!!!!!!!not so sure because usecase1 is not supposed to be string->to be checked
				   /*the one written by yosr:
				    * Creating includes
				    *     usecase1.createInclude("inc111",usecase2);  -----param2: use case included
				    */
                //knowing the type of each side
                //include is not definef for an actor

                if (htusecases.containsKey(extendside_A)) {
                    if (htusecases.containsKey(extendside_B)) {

                        UseCase usecase1 = htusecases.get(extendside_A);
                        UseCase usecase11 = htusecases.get(extendside_B);
                        usecase1.createInclude("inc111", usecase11);
                    }
                    if (htactors.containsKey(extendside_B)) {
                        String nom = htactors.get(extendside_B).getLabel();
                        errorsReport = errorsReport + "\n Error a use case cannot include an actor "+ nom;
                    }
                }
                if (htactors.containsKey(extendside_A)) {
                    if (htusecases.containsKey(extendside_B)) {
                        errorsReport = errorsReport + "\n Error an actor cannot include a use case";
                    }
                    if (htactors.containsKey(extendside_B)) {
                        errorsReport = errorsReport + "\n Error an actor cannot include an actor";
                    }
                }
            }
            //Creating includes
            //   extendside_A.createInclude(side_A,side_B);  //param2: use case included

            //working on umlGeneralizations
            for (int n = 0; n <= (umlGeneralizations.size() - 1); n++) {
                org.jdom2.Element umlGeneralization = (org.jdom2.Element) umlGeneralizations.get(n);
                String Generalizationside_B = umlGeneralization.getAttributeValue("side_B");
                String Generalizationside_A = umlGeneralization.getAttributeValue("side_A");
                String Generalizationsid = umlGeneralization.getAttributeValue("id");

                //knowing the type of each side

            /*if (htactors.containsKey(Generalizationside_A)) {
                if (htusecases.containsKey(Generalizationside_B)) {
                    createGeneralization(htactors.get(Generalizationside_A), htusecases.get(Generalizationside_B));

                }

                if (htactors.containsKey(Generalizationside_A)) {
                    createGeneralization(htactors.get(Generalizationside_A), htactors.get(Generalizationside_B));
                }
            }

            if (htusecases.containsKey(Generalizationside_A)) {
                if (htactors.containsKey(Generalizationside_B)) {
                    createGeneralization(htusecases.get(Generalizationside_A), htactors.get(Generalizationside_B));

                }
                if (htusecases.containsKey(Generalizationside_B)) {
                    createGeneralization(htusecases.get(Generalizationside_A), htusecases.get(Generalizationside_B));
                }
            }*/
                if(htactors.containsKey(Generalizationside_A))
                {
                    if(htusecases.containsKey(Generalizationside_B))
                    {

                        errorsReport = errorsReport + "\n Error use case cannot be generalized/ inherited from an actor";

                    }

                    if(htactors.containsKey(Generalizationside_B))
                    {
                        createGeneralization(htactors.get(Generalizationside_A),htactors.get(Generalizationside_B));
                    }
                }

                if(htusecases.containsKey(Generalizationside_A))
                {
                    if(htactors.containsKey(Generalizationside_B))
                    {
                        errorsReport = errorsReport + "\n Error A actor cannot be generalized/ inherited from a use case!";

                    }
                    if(htusecases.containsKey(Generalizationside_B))
                    {
                        createGeneralization(htusecases.get(Generalizationside_A),htusecases.get(Generalizationside_B));
                    }

                }
            }
        }
        catch (JDOMException e) {
            // handle JDOMException
        }
        catch (IOException e) {
            // handle IOException
        }

        return errorsReport;
    }


    //public static void saveUCD(Model UCD){

      //  save(UCD, URI.createURI("./").appendSegment("TestUCD").appendFileExtension(UMLResource.FILE_EXTENSION));
        //log.debug("saviiiiiiiiiiiiiiiing done !");
    //}

    public static String validateUCD (Model UCD) throws JDOMException, IOException {
        Diagnostic validate = Diagnostician.INSTANCE.validate(UCD);
        String errors = null;
        if (validate.getSeverity() != Diagnostic.OK) {
            log.debug(validate.getMessage());
            log.debug("Exception in: " + validate.getException());
            for ( Diagnostic child : validate.getChildren () )
            {
                log.debug("  "+ child.getMessage());
                errors = errors + "\n" +child.getMessage();
            }
            /*if (validate.getSeverity() == Diagnostic.WARNING) {
                log.debug("warniiiiiiiiing bark !!");
            }*/
        return errors;
        }
        else {
            log.debug("diag not error");
            return null;
        }
    }
}

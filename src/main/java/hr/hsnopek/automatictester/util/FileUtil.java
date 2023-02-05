package hr.hsnopek.automatictester.util;

import hr.hsnopek.automatictester.core.model.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static String readTextFromFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private static Test parseFile(String path) throws JAXBException {
        File xmlFile = new File(path);
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Test.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Test) jaxbUnmarshaller.unmarshal(xmlFile);
        }
        catch (JAXBException e) {
            throw e;
        }
    }

    public static void saveFileToLocation(Test test, String path) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Test.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            jaxbMarshaller.marshal(test, new File(path));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}

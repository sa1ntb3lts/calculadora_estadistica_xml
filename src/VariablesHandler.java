import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class VariablesHandler extends DefaultHandler {

    private String variableBuscada;
    private List<Double> valores;
    private boolean esVariable = false;
    private StringBuilder contenido;

    public VariablesHandler(String variableBuscada) {
        super();
        this.variableBuscada = variableBuscada;
        this.valores = new ArrayList<>();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals(variableBuscada)) {
            esVariable = false;
            try {
                double valor = Double.parseDouble(contenido.toString().trim());
                valores.add(valor);
            } catch (NumberFormatException e) {
                System.out.println("Error: La variable '" + variableBuscada + "' no es numerica.");
                System.exit(1);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (esVariable) {
            contenido.append(ch, start, length);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals(variableBuscada)) {
            esVariable = true;
            contenido = new StringBuilder();
        }
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e);
    }

    public List<Double> getValores() {
        return valores;
    }
}
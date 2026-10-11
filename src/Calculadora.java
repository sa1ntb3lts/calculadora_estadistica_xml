import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.List;

public class Calculadora {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No se proporcionaron argumentos requeridos");
            System.out.println("Uso: java mx.unison.Calculadora <archivo.xml> <variable1> [variable2]");
            System.exit(1);
        }

        if (args.length == 1) {
            System.out.println("Debes incluir al menos el nombre de una variable");
            System.exit(2);
        }

        String archivoXml = args[0];
        String var1 = args[1];
        String var2 = (args.length > 2) ? args[2] : null;

        try {
            File file = new File(archivoXml);
            if (!file.exists()) {
                System.out.println("Error: No se puede abrir el documento XML: " + archivoXml);
                System.exit(3);
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            VariablesHandler handler1 = new VariablesHandler(var1);
            saxParser.parse(file, handler1);
            List<Double> valores1 = handler1.getValores();

            if (valores1.isEmpty()) {
                System.out.println("Error: La variable '" + var1 + "' no existe en el documento.");
                System.exit(4);
            }

            imprimirEstadisticas(var1, valores1);

            if (var2 != null) {
                VariablesHandler handler2 = new VariablesHandler(var2);
                saxParser.parse(file, handler2);
                List<Double> valores2 = handler2.getValores();

                if (valores2.isEmpty()) {
                    System.out.println("Error: La variable '" + var2 + "' no existe en el documento.");
                    System.exit(4);
                }

                imprimirEstadisticas(var2, valores2);
                calcularCorrelacion(valores1, valores2, var1, var2);
            }

        } catch (Exception e) {
            System.out.println("Ocurrio un error al procesar el archivo: " + e.getMessage());
            System.exit(5);
        }
    }

    private static void imprimirEstadisticas(String nombreVar, List<Double> valores) {
        int n = valores.size();
        double suma = 0;
        double max = valores.get(0);
        double min = valores.get(0);

        for (double v : valores) {
            suma += v;
            if (v > max) max = v;
            if (v < min) min = v;
        }

        double media = suma / n;

        double sumaCuadrados = 0;
        for (double v : valores) {
            sumaCuadrados += Math.pow(v - media, 2);
        }
        double varianza = sumaCuadrados / n;
        double desviacion = Math.sqrt(varianza);

        System.out.println("\n--- Estadisticas para: " + nombreVar + " ---");
        System.out.println("Media: " + media);
        System.out.println("Desviacion estandar: " + desviacion);
        System.out.println("Varianza: " + varianza);
        System.out.println("Valor maximo: " + max);
        System.out.println("Valor minimo: " + min);
    }

    private static void calcularCorrelacion(List<Double> x, List<Double> y, String v1, String v2) {
        int n = Math.min(x.size(), y.size());
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
            sumY2 += yi * yi;
        }

        double numerador = (n * sumXY) - (sumX * sumY);
        double denominador = Math.sqrt((n * sumX2 - (sumX * sumX)) * (n * sumY2 - (sumY * sumY)));

        double correlacion = (denominador == 0) ? 0 : numerador / denominador;

        System.out.println("\n--------------------------------------------------");
        System.out.println("Coeficiente de correlacion entre " + v1 + " y " + v2 + ": " + correlacion);
        System.out.println("--------------------------------------------------");
    }
}
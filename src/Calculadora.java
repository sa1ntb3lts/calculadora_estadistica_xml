public class Calculadora {
	public static void main(String[] args) {
		
		if (args.length == 0) {
            System.out.println("No ser proporcionarion argumentos requeridos");
            System.exit(1);
        }

        if (args.length == 1) {
            System.out.println("Debes incluir al menos el nombre de una variable");
            System.exit(2);
        }
	}
}
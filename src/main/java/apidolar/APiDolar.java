package apidolar;

import gui.Pantalla;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APiDolar {

    public static void main(String[] args) {
        // URL en la que basamos nuestra API
        String url = "https://dolarhoy.com/cotizaciondolarblue";
            Pantalla pant = new Pantalla();
            pant.setVisible(true);
            pant.setLocationRelativeTo(null);
        // Realizamos la solicitud HTTP a la página
        try {
            // Creamos la URL y establecemos la conexión
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Establecemos el método de la solicitud y el User-Agent
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Lee la respuesta de la solicitud
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Imprime el contenido completo para depuración
            System.out.println(response.toString());

            // Obtiene el contenido de la página
            String pageContent = response.toString();

            // Tomamos el valor de <div class="value"> simulando una peticion CURL 
            Pattern pattern = Pattern.compile("<div class=\"value\">([^<]+)");
            Matcher matcher = pattern.matcher(pageContent);

            // Asignamos variables para almacenar los valores de venta y compra
            String venta = null;
            String compra = null;

            // Buscar las cotizaciones en el HTML
            int count = 0;
            while (matcher.find()) {
                if (count == 0) {
                    venta = matcher.group(1).trim();
                } else if (count == 1) {
                    compra = matcher.group(1).trim();
                }
                count++;
            }

            // Verificamos si se han encontrado ambos valores
            if (venta != null && compra != null) {
                // Eliminar el signo '$' y convertir las comas a puntos
                venta = venta.replace("$", "").replace(",", ".");
                compra = compra.replace("$", "").replace(",", ".");

                // Se pueden usar las variables para el uso de datos en consola
                double ventaValue = Double.parseDouble(venta);
                double compraValue = Double.parseDouble(compra);
                
                pant.actualizarValores(compra, venta);

            } else {
                System.out.println("No se encontraron los valores de cotización.");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
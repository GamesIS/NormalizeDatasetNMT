import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

public class Main {

    public static void main(String[] args) {
        try (FileReader fr = new FileReader("data/ukr.txt")) {
            try (FileWriter writer = new FileWriter("data/result_ukr.txt", true)) {
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                StringBuilder toTranslate = new StringBuilder();
                toTranslate.append(line.split("\t")[1]).append("\n");
                int i = 1;
                int j = 1;
                while (line != null) {
                    toTranslate.append(line.split("\t")[1]).append("\n");
                    line = reader.readLine();
                    i++;
                    if (i == 30 || line == null) {
                        i = 0;
                        writer.append(translate(toTranslate.toString(), "uk-ru"));
                        System.out.println("Translated " + j * 30);
                        j++;
                        Thread.sleep(2000);
                        toTranslate = new StringBuilder();
                        writer.flush();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String translate(String input, String lang) {
        try {
            String urlStr = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20191027T210412Z.1aa89118fe54d395.0268af03e47b7c30de1d807e42f33c2976e589e4";
            URL urlObj = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") + "&lang=" + lang);
            String res = convertStreamToString(connection.getInputStream());
            int start = res.indexOf("<text>");
            int end = res.indexOf("</text>");
            return res.substring(start + 6, end);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

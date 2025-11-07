/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ceccigiuliapapercutclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author cecci.giulia
 */
public class SMTPClient {

    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    private javax.swing.JTextArea atxLog;

    public SMTPClient(String server, javax.swing.JTextArea atxLog) throws FileNotFoundException, IOException {
        
        this.socket = new Socket(server, 25);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.atxLog = atxLog;

        String risposta = reader.readLine();
        atxLog.append("SERVER: " + risposta + "\n");
    }

    private String readServerResponse() throws IOException {
        // Legge una o più righe di risposta dal server
        String risposta = "";
        String riga;

        while ((riga = reader.readLine()) != null) {
            risposta += riga + "\n";
            // Se la quarta posizione non è '-' significa che la risposta è finita
            if (riga.length() > 3 && riga.charAt(3) != '-') {
                break;
            }
        }

        return risposta.trim();
    }

    private SMTPResponse sendCommand(String command) throws IOException {
   
        writer.write(command + "\r\n");
        writer.flush();

        atxLog.append("CLIENT: " + command+ "\n");
        String risposta = readServerResponse();
        atxLog.append("SERVER: " + risposta+ "\n");

        SMTPResponseParser parser = new SMTPResponseParser();
        SMTPResponse response = parser.parse(risposta);
        return response;
    }



    public SMTPResponse helo(String server) throws IOException{
        return sendCommand("HELO " + server);
    }
    
    public SMTPResponse from (String from) throws IOException{
         return sendCommand("MAIL FROM:<" + from + ">");
    }
    
    public SMTPResponse to (String to) throws IOException{
        return sendCommand("RCPT TO:<" + to + ">");
    }
    
    public SMTPResponse data (String subject, String message) throws IOException{
        SMTPResponse r = sendCommand("DATA");
        if (r.getCode() == 354) { // Se il server è pronto a ricevere
            writer.write("SUBJECT: " + subject + "\r\n");
            writer.write(message + "\r\n");
            writer.write(".\r\n");
            writer.flush();

            String risposta = reader.readLine();
            atxLog.append("SERVER: " + risposta + "\n");
            
            SMTPResponseParser parser = new SMTPResponseParser();
            r = parser.parse(risposta);
        }
        return r;
    }
    
    public SMTPResponse quit () throws IOException{
        SMTPResponse response = sendCommand("QUIT");
        closeConnection();
        return response;
    }
    
    private void closeConnection () throws IOException{
        reader.close();
        writer.close();
        socket.close();
    }
    
    public SMTPResponse getLastResponse () throws IOException{
        String risposta = readServerResponse();
        SMTPResponseParser parser = new SMTPResponseParser();
        return parser.parse(risposta);
    }
    
    public String getResponse () throws IOException{
        SMTPResponse resp = getLastResponse();
        return resp.getRawResponse();
        
    }
    
    
}

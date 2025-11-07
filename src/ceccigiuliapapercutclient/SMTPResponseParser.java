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
import java.net.Socket;

/**
 *
 * @author cecci.giulia
 */
public class SMTPResponseParser {

    public SMTPResponse parse(String rawResponse) {
        if (rawResponse == null || rawResponse.isEmpty()) {
            return new SMTPResponse(0, "Nessuna risposta", "");
        }

        int code = 0;
        String message = "";

        try {
            code = Integer.parseInt(rawResponse.substring(0, 3));
            if (rawResponse.length() > 4) {
                message = rawResponse.substring(4);
            } else {
                message = "";
            }

        } catch (Exception e) {
            message = "Errore nel parsing della risposta";
        }

        return new SMTPResponse(code, message, rawResponse);
    }

    public SMTPResponse parseMultiLine(String rawResponse) {
        String[] lines = rawResponse.split("\n");
        String allLines = "";
        int code = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            allLines += line + "\n";

            if (line.length() >= 3) {
                try {
                    code = Integer.parseInt(line.substring(0, 3));
                } catch (NumberFormatException e) {
                    code = 0;
                }
            }
            // Se non c’è il trattino dopo il codice, è l’ultima riga
            if (line.length() > 3 && line.charAt(3) != '-') {
                break;
            }
        }

        return new SMTPResponse(code, "Risposta multi-linea", allLines);
    }

    public boolean isSuccessCode(int code) {
        boolean isSuccessCode = false;
        if(code >= 200 && code < 300){
            isSuccessCode = true;
        }
        return isSuccessCode;
    }

    public boolean isErrorCode(int code) {
        boolean isErrorCode = false;
        if(code >= 400){
            isErrorCode = true;
        }
        return isErrorCode;

    }

    public boolean isIntermediateCode(int code) {
        boolean isIntermediateCode = false;
        if(code >= 300 && code < 400){
            isIntermediateCode = true;
        }
        return isIntermediateCode;
    }

    public String getResponseType(int code) {
        String type ="";
        if(isSuccessCode(code)){
            type = "Successo";
        }else if (isIntermediateCode(code)) {
            type = "Intermedio";
        }else if (isErrorCode(code)) {
            type = "Errore";
        }
        return type;
    }
}

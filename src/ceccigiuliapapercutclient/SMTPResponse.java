/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ceccigiuliapapercutclient;

/**
 *
 * @author cecci.giulia
 */
public class SMTPResponse {
    private int code;
    private String message;
    private String rawResponse;

    public SMTPResponse(int code, String message, String rawResponse) {
        this.code = code;
        this.message = message;
        this.rawResponse = rawResponse;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRawResponse() {
        return rawResponse;
    }
    
    public Boolean isSuccess(){
        boolean isSuccessCode = false;
        if(code >= 200 && code < 300){
            isSuccessCode = true;
        }
        return isSuccessCode;
        
    }
    
    public Boolean isError(){
        boolean isErrorCode = false;
        if(code >= 400){
            isErrorCode = true;
        }
        return isErrorCode;

    }
    
    public Boolean isIntermediate(){
        boolean isIntermediateCode = false;
        if(code >= 300 && code < 400){
            isIntermediateCode = true;
        }
        return isIntermediateCode;
    }

    @Override
    public String toString() {
        return "SMTPResponse{" + "code=" + code + ", message=" + message + ", rawResponse=" + rawResponse + '}';
    }
    
    
    public String toDetailedString() {
        return "Il codice del messaggio è: "+code+"\n Il messaggio è : "+message+"\n la risposta completa è : "+rawResponse;
    }
}

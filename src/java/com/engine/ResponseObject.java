/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine;

import com.soap.data.ResponseQuoteData;
import java.util.ArrayList;

/**
 *
 * @author nail yusupov
 */
class ResponseObject {
    
    Integer StatusCode;
    String Message;
    ArrayList<ResponseQuoteData> Resources;
    
    public ResponseObject(){
        StatusCode = 1;
        Message = "200 OK";
        Resources = null;
    }
    
}

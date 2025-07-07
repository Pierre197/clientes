package com.pierre.clientes.domain.model.shared;

public enum TransactionCode {
    CREATE("102"),
    GET("002");

    private final String code;

    TransactionCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}

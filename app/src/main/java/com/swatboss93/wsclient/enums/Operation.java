package com.swatboss93.wsclient.enums;

/**
 * Created by swatboss93 on 24/08/16.
 */
public enum Operation {
    INSERT(0), UPDATE(1), DELETE(2);

    private final int operacao;
    Operation(int operationValue){
        this.operacao = operationValue;
    }
    public int getValorOperacao(){
        return this.operacao;
    }
}
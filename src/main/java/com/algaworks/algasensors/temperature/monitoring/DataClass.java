package com.algaworks.algasensors.temperature.monitoring;

import lombok.Data;

@Data
public class DataClass {
    private String data;

    public static void main(String[] args) {
        var variavel = new DataClass();
        variavel.setData("abc");
        System.out.println(variavel.getData());
    }

}

package com.pierre.clientes.domain.util;

import com.pierre.clientes.domain.model.shared.UseCaseTransactionCode;

public class TransactionCodeResolver {

    public static String resolveTransactionCode(Object useCaseInstance){
        Class<?> clazz = useCaseInstance.getClass();
        UseCaseTransactionCode annotation = clazz.getAnnotation(UseCaseTransactionCode.class);

        if (annotation != null){
            return annotation.value().getCode();
        }else {
            throw new IllegalStateException("El UseCase " + clazz.getSimpleName() + " no tiene @UseCaseTransactionCode definido");
        }
    }
}

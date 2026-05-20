package com.fadergs.salalilas.backend.util;

public class CpfValidator {
    private CpfValidator() {}

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        // Remove qualquer não número
        String digits = cpf.replaceAll("\\D", "");

        if (digits.length() != 11) return false;

        // Rejeita se todos os digitos forem os mesmo (ex: 111.111.111-11)
        if (digits.chars().distinct().count() == 1) return false;

        // Verificação primeiro dígito
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * (10 - i);
        }
        int first = 11 - (sum % 11);
        if (first >= 10) first = 0;
        if (first != Character.getNumericValue(digits.charAt(9))) return false;

        // Verificação segundo dígito
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * (11 - i);
        }
        int second = 11 - (sum % 11);
        if (second >= 10) second = 0;
        return second == Character.getNumericValue(digits.charAt(10));
    }

    public static String sanitize(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }
}

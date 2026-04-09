package com.industria;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    // Formatadores reutilizáveis
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat NUMBER_FORMAT;

    static {
        NUMBER_FORMAT = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        NUMBER_FORMAT.setMinimumFractionDigits(2);
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }

    public static void main(String[] args) {

        // 3.1 – Inserindo todos os funcionários"

        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios
                .add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios
                .add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        // 3.2 – Remover o funcionário "João"

        funcionarios.removeIf(f -> f.getNome().equals("João"));

        // 3.3 – Imprimir todos os funcionários formatados

        System.out.println("=".repeat(65));
        System.out.println("  3.3 – LISTA DE FUNCIONÁRIOS");
        System.out.println("=".repeat(65));
        funcionarios.forEach(f -> imprimirFuncionario(f));

        // 3.4 – Aumento de 10% no salário

        funcionarios.forEach(
                f -> f.setSalario(f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP)));

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.4 – FUNCIONÁRIOS APÓS AUMENTO DE 10%");
        System.out.println("=".repeat(65));
        funcionarios.forEach(f -> imprimirFuncionario(f));

        // 3.5 – Agrupar funcionários por função em um Map

        Map<String, List<Funcionario>> porFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        // 3.6 – Imprimir funcionários agrupados por função

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.6 – FUNCIONÁRIOS AGRUPADOS POR FUNÇÃO");
        System.out.println("=".repeat(65));
        porFuncao.forEach((funcao, lista) -> {
            System.out.println("\n  Função: " + funcao);
            lista.forEach(f -> imprimirFuncionario(f));
        });

        // 3.8 – Funcionários que fazem aniversário em outubro (10) ou dezembro (12)

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.8 – ANIVERSARIANTES DE OUTUBRO E DEZEMBRO");
        System.out.println("=".repeat(65));
        funcionarios.stream()
                .filter(f -> {
                    int mes = f.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(f -> imprimirFuncionario(f));

        // 3.9 – Funcionário com maior idade

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.9 – FUNCIONÁRIO MAIS VELHO");
        System.out.println("=".repeat(65));
        funcionarios.stream()
                .min(Comparator.comparing(Pessoa::getDataNascimento))
                .ifPresent(f -> {
                    int idade = Period.between(f.getDataNascimento(), LocalDate.now()).getYears();
                    System.out.printf("  Nome: %-15s Idade: %d anos%n", f.getNome(), idade);
                });

        // 3.10 – Funcionários em ordem alfabética

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.10 – FUNCIONÁRIOS EM ORDEM ALFABÉTICA");
        System.out.println("=".repeat(65));
        funcionarios.stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .forEach(f -> imprimirFuncionario(f));

        // 3.11 – Total dos salários

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.11 – TOTAL DOS SALÁRIOS");
        System.out.println("=".repeat(65));
        System.out.println("  Total: R$ " + NUMBER_FORMAT.format(totalSalarios));

        // 3.12 – Salários mínimos por funcionário

        BigDecimal salarioMinimo = new BigDecimal("1212.00");

        System.out.println("\n" + "=".repeat(65));
        System.out.println("  3.12 – SALÁRIOS EM MÚLTIPLOS DO SALÁRIO MÍNIMO (R$ 1.212,00)");
        System.out.println("=".repeat(65));
        funcionarios.forEach(f -> {
            BigDecimal multiplo = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.printf("  %-12s → %s salários mínimos%n", f.getNome(), NUMBER_FORMAT.format(multiplo));
        });

        System.out.println("\n" + "=".repeat(65));
    }

    private static void imprimirFuncionario(Funcionario f) {
        System.out.printf("  Nome: %-12s | Nascimento: %s | Salário: R$ %-12s | Função: %s%n",
                f.getNome(),
                f.getDataNascimento().format(DATE_FORMAT),
                NUMBER_FORMAT.format(f.getSalario()),
                f.getFuncao());
    }
}

package ui;

import business.SSUC.Turno;
import business.SSUtilizadores.Aluno;
import business.LNFacade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final LNFacade lnFacade;
    private final Scanner scanner;

    public Menu() {
        this.lnFacade = new LNFacade();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("Bem-vindo ao sistema de gestão de turnos!");
        System.out.print("Insira o seu ID: ");
        String id = scanner.nextLine();
        System.out.print("Insira a sua password: ");
        String password = scanner.nextLine();

        if (!lnFacade.login(id, password)) {
            System.out.println("Credenciais inválidas. Sistema a encerrar.");
            return;
        }

        if (id.startsWith("d")) { // Identifica um diretor 
            menuDiretor(id);
        } else if (id.startsWith("A")) { // Identifica um aluno 
            menuAluno(id);
        } else {
            System.out.println("Tipo de utilizador desconhecido. Sistema a encerrar.");
        }
    }

    private void menuDiretor(String diretorId) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Menu do Diretor ===");
            System.out.println("1. Registar Aluno");
            System.out.println("2. Importar Alunos");
            System.out.println("3. Definir Restrição para UC");
            System.out.println("4. Gerar Horários");
            System.out.println("5. Consultar Horário de Aluno");
            System.out.println("6. Ver Alunos");
            System.out.println("7. Alocação Manual");
            System.out.println("8. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> registarAluno();
                case 2 -> importarAlunos();
                case 3 -> definirRestricoes();
                case 4 -> gerarHorarios();
                case 5 -> consultarHorarioAluno();
                case 6 -> verAlunos();
                case 7 -> alocacaoManual();
                case 8 -> running = false;
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void menuAluno(String alunoId) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Menu do Aluno ===");
            System.out.println("1. Consultar Horário");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> consultarHorario(alunoId);
                case 2 -> running = false;
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public void registarAluno() {
        System.out.print("ID: ");
        String id = scanner.nextLine();

        if (lnFacade.alunoExiste(id)) {
            System.out.println("Erro: Um aluno com este ID já está registado.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Curso: ");
        String curso = scanner.nextLine();
        System.out.print("Estatuto (true/false): ");
        Boolean estatuto = null;
        while (estatuto == null) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                estatuto = Boolean.parseBoolean(input);
            } else {
                System.out.print("Entrada inválida. Digite 'true' ou 'false': ");
            }
        }

        System.out.print("Média: ");
        Float media = null;
        while (media == null) {
            try {
                media = Float.parseFloat(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite novamente: ");
            }
        }

        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        List<Integer> ucs = new ArrayList<>();
        System.out.println("Insira as UCs (digite 'fim' para terminar):");
        while (true) {
        String input = scanner.nextLine(); 
        if (input.equalsIgnoreCase("fim")) { 
            break;
        }
        try {
            Integer uc = Integer.parseInt(input); 
            ucs.add(uc); 
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite um número inteiro ou 'fim' para terminar.");
        }
    }

        lnFacade.registarAluno(id, nome, curso, estatuto, media, senha, ucs);
        System.out.println("Aluno registado com sucesso.");
    }

    private void definirRestricoes() {
        System.out.print("ID da UC: ");
        Integer idUC = null;
        while (idUC == null) {
            try {
                idUC = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número válido para o ID da UC: ");
            }
        }

        try{
            List<Turno> turnos = lnFacade.getTurnosUc(idUC);
            if(!turnos.isEmpty()) {
                System.out.println("Turnos associados à UC " + idUC);
                for(Turno t : turnos) {
                    System.out.printf("Id: %s | Tipo: %s | Dia: %s | Horas: %s-%s | Capacidade: %s | Lugares Reservados: %d\n",
                    t.getId(),
                    t.getTipo(),
                    t.getDiaSemana(),
                    t.getHoraInicio(),
                    t.getHoraFim(),
                    t.getCapacidade(),
                    t.getLugaresReservadosEstatuto());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter turnos" + e.getMessage());
        }

        System.out.print("ID do Turno: ");
        String idTurno = scanner.nextLine();

        System.out.println("Escolha a restrição a aplicar:");
        System.out.println("1. Definir Capacidade do Turno");
        System.out.println("2. Reservar Lugares para Alunos com Estatuto");
        System.out.print("Escolha uma opção: ");
        int opcao = Integer.parseInt(scanner.nextLine());

        switch (opcao) {
            case 1 -> definirCapacidadeTurno(idUC, idTurno);
            case 2 -> reservarLugaresEstatuto(idUC, idTurno);
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private void definirCapacidadeTurno(Integer idUC, String idTurno) {
        System.out.print("Capacidade do Turno: ");
        int capacidade = 0;
        while (capacidade <= 0) {
            try {
                capacidade = Integer.parseInt(scanner.nextLine());
                if (capacidade <= 0) {
                    System.out.print("Entrada inválida. Digite um número positivo para a capacidade: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número válido para a capacidade: ");
            }
        }
        
        try {
            Boolean restricao = lnFacade.definirCapacidadeTurno(idUC, idTurno, capacidade);
            if (restricao == true) {
                System.out.println("Capacidade do turno definida com sucesso.");
            }
            else {
                System.out.println("Não existe nenhum turno com o id " + idTurno + " associado à UC " + idUC);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao definir capacidade: " + e.getMessage());
        }
    }

    private void reservarLugaresEstatuto(Integer idUC, String idTurno) {
        System.out.print("Lugares a reservar para alunos com estatuto: ");
        int lugares = 0;
        while (lugares <= 0) {
            try {
                lugares = Integer.parseInt(scanner.nextLine());
                if (lugares <= 0) {
                    System.out.print("Entrada inválida. Digite um número positivo para os lugares: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número válido para os lugares: ");
            }
        }

        try {
            Boolean restricao = lnFacade.reservarLugaresEstatuto(idUC, idTurno, lugares);
            if (restricao == true) {
                System.out.println("Lugares reservados com sucesso.");
            }
            else {
                System.out.println("Não existe nenhum turno com o id " + idTurno + " associado à UC " + idUC);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao definir capacidade: " + e.getMessage());
        }
    }

    private void gerarHorarios() {
        try {
            lnFacade.gerarHorarios();
            System.out.println("Horários gerados com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao gerar horários: " + e.getMessage());
        }
    }

    // para o diretor
    private void consultarHorarioAluno() {
        System.out.print("Insira o ID do aluno: ");
        String alunoId = scanner.nextLine();
        
        String horarioStr = lnFacade.consultarHorario(alunoId);
        System.out.println("Horário do Aluno:\n" + horarioStr);
    }

    // para o aluno
    private void consultarHorario(String alunoId) {
        String horarioStr = lnFacade.consultarHorario(alunoId);
        System.out.println("Horário do Aluno:\n" + horarioStr);
    }

    private void verAlunos() {
        List<Aluno> alunos = lnFacade.getAlunos(); // Obtém a lista de alunos
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno registado no sistema.");
        } else {
            System.out.println("\n=== Lista de Alunos ===");
            for (Aluno aluno : alunos) {
                System.out.println(String.format(
                    "ID: %s | Nome: %s | Curso: %s | Estatuto: %s | Média: %.2f",
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getCurso(),
                    aluno.getEstatuto() ? "TE" : "NENHUM",
                    aluno.getMedia()
                ));
            }
        }
    }

    private void importarAlunos() {
        System.out.print("Caminho do arquivo: ");
        String filePath = scanner.nextLine();
        try {
            lnFacade.importarAlunos(filePath);
        } catch (Exception e) {
            System.out.println("Erro ao importar alunos: " + e.getMessage());
        }
    }

    private void alocacaoManual() {
        boolean continuar = true;
    
        while (continuar) {
            System.out.print("Insira o Id do aluno ou sair para voltar ao menu: ");
            String alunoId = scanner.nextLine();
            if (alunoId.equalsIgnoreCase("sair")) return;
    
            try {
                // Obtém os turnos disponíveis para o aluno
                List<Turno> turnosDisponiveis = lnFacade.alocarManualmente(alunoId, null);
    
                if (turnosDisponiveis.isEmpty()) {
                    System.out.println("Nenhum turno disponível para o aluno.");
                    continue;
                }
    
                System.out.println("\nTurnos disponíveis:");
                for (int i = 0; i < turnosDisponiveis.size(); i++) {
                    Turno turno = turnosDisponiveis.get(i);
                    System.out.printf("[%d] Turno ID: %s | UC: %s | Tipo: %s | Dia: %s | Duração: %s - %s | Capacidade Atual: %d\n",
                            i + 1, turno.getId(), turno.getUcNome(), turno.getTipo(), turno.getDiaSemana(),
                            turno.getHoraInicio(), turno.getHoraFim(),
                            turno.getSala().getCapacidade());
                }
    
                while (true) {
                    System.out.print("\nEscolha o número do turno para alocar o aluno (ou 0 para sair): ");
                    int escolha;
                    try {
                        escolha = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Tente novamente.");
                        continue;
                    }
    
                    if (escolha == 0) break;
                    if (escolha < 1 || escolha > turnosDisponiveis.size()) {
                        System.out.println("Escolha inválida. Tente novamente.");
                        continue;
                    }
    
                    Turno turnoSelecionado = turnosDisponiveis.get(escolha - 1);
                    lnFacade.alocarManualmente(alunoId, turnoSelecionado.getId());
                    return;
                }
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
    }        
}
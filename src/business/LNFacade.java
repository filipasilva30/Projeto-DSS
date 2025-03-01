package business;

import business.SSUC.Turno;
import business.SSUC.UC;
import business.SSUtilizadores.Aluno;

import business.SSUC.UCFacade;
import business.SSUtilizadores.UtilizadoresFacade;
import business.SSHorario.HorarioFacade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LNFacade {
    private final UCFacade ucFacade = new UCFacade();
    private final UtilizadoresFacade utilizadoresFacade = new UtilizadoresFacade();
    private final HorarioFacade horarioFacade = new HorarioFacade();

    public boolean login(String id, String password) {
        return utilizadoresFacade.login(id, password);
    }

    public void registarAluno(String id, String nome, String curso, Boolean estatuto, Float media, String senha, List<Integer> ucs) {
        utilizadoresFacade.registarAluno(id, nome, curso, estatuto, media, senha, ucs);
    }

    public Boolean alunoExiste(String id) {
        return utilizadoresFacade.alunoExiste(id);
    }

    public List<Turno> alocarManualmente(String alunoId, String turnoId) {
        List<Turno> turnosDisponiveis = new ArrayList<>();
    
        try {
            // Verifica se o aluno existe
            if (!utilizadoresFacade.alunoExiste(alunoId)) {
                System.out.println("Erro: Aluno não encontrado.");
                return turnosDisponiveis; // lista vazia
            }
    
            // Obtém as UCs do aluno
            List<Integer> ucsAluno = ucFacade.getUCsAluno(alunoId);
            if (ucsAluno.isEmpty()) {
                System.out.println("Erro: O aluno não está inscrito em nenhuma UC.");
                return turnosDisponiveis; // lista vazia
            }
    
            // Obtém turnos já ocupados pelo aluno para evitar conflitos
            Set<String> slotsOcupados = new HashSet<>();
            Map<String, Set<String>> tiposAlocadosPorUC = new HashMap<>(); // Mapa dos tipos já alocados por UC
            List<Turno> turnosAluno = horarioFacade.getTurnosAluno(alunoId); // Turnos do aluno

            for (Turno t : turnosAluno) {
                String slot = t.getDiaSemana() + t.getHoraInicio() + t.getHoraFim();
                slotsOcupados.add(slot);

                // Procura os tipos de turnos (T ou TP) já alocados por UC
                tiposAlocadosPorUC.putIfAbsent(t.getUcNome(), new HashSet<>());
                tiposAlocadosPorUC.get(t.getUcNome()).add(t.getTipo());
            }
    
            // Lista de turnos disponíveis para todas as UCs do aluno
            for (Integer ucId : ucsAluno) {
                List<Turno> turnosUC = ucFacade.getTurnosUc(ucId);
                for (Turno turno : turnosUC) {
                    int alunosNoTurno = horarioFacade.getAlunosTurno(Integer.parseInt(turno.getId()));
                    String slotTurno = turno.getDiaSemana() + turno.getHoraInicio() + turno.getHoraFim();
    
                    if (alunosNoTurno >= turno.getCapacidade() || slotsOcupados.contains(slotTurno)) {   // Vê se turno está cheio ou se faz conflito com outro
                        continue;
                    }
    
                    // Verifica se o aluno já está alocado num turno do mesmo tipo nessa UC
                    Set<String> tiposJaAlocados = tiposAlocadosPorUC.getOrDefault(turno.getUcNome(), new HashSet<>());
                    if (tiposJaAlocados.contains(turno.getTipo())) {
                        continue;
                    }
    
                    // Adiciona o turno como disponível
                    turnosDisponiveis.add(turno);
                }
            }

            // verifica se o turno fornecido está disponivel
            if (turnoId != null) {
                Turno turno = turnosDisponiveis.stream()
                                               .filter(t -> t.getId().equals(turnoId))
                                               .findFirst()
                                               .orElse(null);
    
                if (turno == null) {
                    System.out.println("Erro: Turno não disponível para alocação.");
                    return turnosDisponiveis;
                }
    
                // Adiciona o aluno ao turno
                horarioFacade.adicionarHorario(alunoId, Integer.parseInt(turnoId));

                // Atualiza os tipos alocados para a UC
                tiposAlocadosPorUC.putIfAbsent(turno.getUcNome(), new HashSet<>());
                tiposAlocadosPorUC.get(turno.getUcNome()).add(turno.getTipo());
                
                System.out.println("Aluno adicionado ao turno com sucesso!");
            }
    
            return turnosDisponiveis;
        } catch (SQLException e) {
            System.out.println("Erro de acesso à base de dados: " + e.getMessage());
            return turnosDisponiveis; // lista vazia
        }
    }

    public void gerarHorarios() {
        try {
            List<Aluno> alunos = utilizadoresFacade.getAlunos();

            for (Aluno aluno : alunos) {
                boolean eEstatuto = aluno.getEstatuto();
                Set<String> horariosOcupados = new HashSet<>();
                List<Integer> ucsAluno = ucFacade.getUCsAluno(aluno.getId());

                for (Integer ucId : ucsAluno) {
                    List<Turno> turnos = ucFacade.getTurnosUc(ucId);

                    boolean turnoTAlocado = false;
                    boolean turnoTPAlocado = false;

                    for (Turno turno : turnos) {
                        String tipo = turno.getTipo();

                        // Garante que ainda precisa de alocar este tipo de turno
                        if ((tipo.equalsIgnoreCase("T") && turnoTAlocado) || 
                            (tipo.equalsIgnoreCase("TP") && turnoTPAlocado)) {
                                continue; // Avança para o próximo turno
                        }

                        String slot = turno.getDiaSemana() + turno.getHoraInicio() + turno.getHoraFim();
                        if (!horariosOcupados.contains(slot)) {
                            int alunosInscritos = horarioFacade.getAlunosTurno(Integer.parseInt(turno.getId()));
                            int lugares = turno.getLugaresReservadosEstatuto();
                            if (eEstatuto && lugares > 0)  {
                                horarioFacade.adicionarHorario(aluno.getId(), Integer.parseInt(turno.getId()));
                                horariosOcupados.add(slot);
                                turno.setLugaresReservadosEstatuto(lugares - 1);
                                ucFacade.atualizarLugares(Integer.parseInt(turno.getId()), lugares);
                            } else if (!eEstatuto && alunosInscritos < turno.getCapacidade()) {
                                horarioFacade.adicionarHorario(aluno.getId(), Integer.parseInt(turno.getId()));
                                horariosOcupados.add(slot);
                            } else {
                                horarioFacade.adicionarHorario(aluno.getId(), Integer.parseInt(turno.getId()));
                                horariosOcupados.add(slot);
                            }

                            // Marca o turno como alocado
                            if (tipo.equalsIgnoreCase("T")) {
                                turnoTAlocado = true;
                            } else if (tipo.equalsIgnoreCase("TP")) {
                                turnoTPAlocado = true;
                            }

                            // Avança para o próximo se ambos já foram alocados
                            if (turnoTAlocado && turnoTPAlocado) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gerar horários: " + e.getMessage());
        }
    }

    public void importarAlunos(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true; // Ignora o cabeçalho

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(";"); 
                if (values.length < 6 || values.length > 7) {
                    System.out.println("Linha inválida ignorada: " + line);
                    continue;
                }

                String id = values[0].trim();
                String nome = values[1].trim();
                String curso = values[2].trim();
                String password = values[3].trim();
                Float media = Float.parseFloat(values[4].trim());
                String[] ucs = values[5].split("\\|"); 
                Boolean estatuto = false;
                if(values.length == 7) {
                    estatuto = !values[6].trim().isEmpty() && !values[6].trim().equalsIgnoreCase("NENHUMA");
                }

                List<Integer> ucIds = new ArrayList<>();
                Map<Integer, UC> todasUCs = ucFacade.getUCs();

                // Itera pelos nomes das UCs do CSV
                for (String ucNome : ucs) {
                    ucNome = ucNome.trim();
                    boolean ucEncontrada = false;

                    for (Map.Entry<Integer, UC> entry : todasUCs.entrySet()) {
                        String nomeBD = entry.getValue().getNome().trim();

                        if (nomeBD.equalsIgnoreCase(ucNome)) {
                            ucIds.add(entry.getKey());
                            ucEncontrada = true;
                            break; 
                        }
                    }

                    if (!ucEncontrada) {
                        System.out.println("Aviso: UC '" + ucNome + "' não encontrada na base de dados.");
                    }
                }

                // Registar o aluno e as suas UCs
                if (!utilizadoresFacade.alunoExiste(id)) {
                    utilizadoresFacade.registarAluno(id, nome, curso, estatuto, media, password, ucIds);
                    System.out.println("Aluno " + nome + " registado com sucesso.");
                } else {
                    System.out.println("Aluno com ID " + id + " já existe. Ignorado.");
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro no formato dos dados: " + e.getMessage());
        }
        System.out.println("Importação concluída com sucesso!");
    }

    public String consultarHorario(String alunoId) {
        return horarioFacade.consultarHorario(alunoId);
    }

    public List<Aluno> getAlunos() {
        return utilizadoresFacade.getAlunos();
    }

    public Boolean reservarLugaresEstatuto(Integer idUC, String idTurno, int lugares) throws SQLException {
        return ucFacade.reservarLugaresEstatuto(idUC, idTurno, lugares);
    }

    public Boolean definirCapacidadeTurno(Integer idUC, String idTurno, int capacidade) throws SQLException {
        return ucFacade.definirCapacidadeTurno(idUC, idTurno, capacidade);
    }

    public List<Turno> getTurnosUc(int ucId) throws SQLException {
        return ucFacade.getTurnosUc(ucId);
    }
}
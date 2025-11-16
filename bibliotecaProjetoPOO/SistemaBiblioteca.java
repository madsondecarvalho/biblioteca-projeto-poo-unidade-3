package bibliotecaProjetoPOO;

import java.util.List;
import java.util.Scanner;

public class SistemaBiblioteca {
    
    // Criamos instâncias dos DAOs que serão usadas pelos menus
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static LivroDAO livroDAO = new LivroDAO();
    private static EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    public static void main(String[] args) {
        
        // 1. Inicia o banco de dados e cria as tabelas
        ConexaoSQLite.criarTabelas();
        
        try (Scanner scanner = new Scanner(System.in)) {
            
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                           BIBLIOTECA VIRTUAL                                  |");
            System.out.println("|                        SISTEMA DE GERENCIAMENTO                               |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.println("Por favor, faça o login para continuar:");
            System.out.println();

            System.out.print("Usuario: ");
            String username = scanner.nextLine();

            System.out.print("Senha: ");
            String password = scanner.nextLine();
            
            // 2. Lógica de Login via DAO
            Usuario usuarioLogado = usuarioDAO.login(username, password);

            if (usuarioLogado != null) {
                limparTela();
                System.out.println(">> Login bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + ".");
                pausa(1500);
                
                // 3. Direciona para o menu correto
                if (usuarioLogado.getTipo().equals("Admin")) {
                    menuAdmin(scanner);
                } else {
                    menuUsuario(scanner, (Leitor) usuarioLogado);
                }
                
            } else {
                System.out.println();
                System.out.println(">> ERRO: Usuario ou senha invalidos. Encerrando.");
                pausa(2000);
            }

        } catch (Exception e) {
            System.out.println(">> ERRO: Ocorreu um erro inesperado: " + e.getMessage());
        }

        System.out.println();
        System.out.println(">> Obrigado por usar nosso sistema. Volte sempre!");
    }

    /**
     * Menu de Administrador COMPLETO
     */
    private static void menuAdmin(Scanner scanner) {
        int opcao = -1;

        while (opcao != 0) {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                            MENU ADMINISTRADOR                                 |");
            System.out.println("==================================================================================");
            System.out.println("|  1. Adicionar livro ao acervo                                                |");
            System.out.println("|  2. Remover livro do acervo                                                  |");
            System.out.println("|  3. Editar livro                                                             |");
            System.out.println("|  4. Listar todos os livros                                                   |");
            System.out.println("|  5. Cadastrar novo leitor                                                    |");
            System.out.println("|  6. Listar todos os leitores                                                 |");
            System.out.println("|  7. Remover leitor                                                           |");
            System.out.println("|  8. Listar todos os emprestimos                                              |");
            System.out.println("|  9. Pesquisar livro por genero                                               |");
            System.out.println("|  0. Sair (Logout)                                                            |");
            System.out.println("==================================================================================");
            System.out.println();
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        adicionarLivroPeloAdmin(scanner);
                        break;
                    case 2:
                        removerLivroPeloAdmin(scanner);
                        break;
                    case 3:
                        editarLivroPeloAdmin(scanner);
                        break;
                    case 4:
                        listarTodosLivros();
                        break;
                    case 5:
                        cadastrarNovoLeitor(scanner);
                        break;
                    case 6:
                        listarTodosLeitores();
                        break;
                    case 7:
                        removerLeitorPeloAdmin(scanner);
                        break;
                    case 8:
                        listarTodosEmprestimos();
                        break;
                    case 9:
                        pesquisarLivroPorGenero(scanner);
                        break;
                    case 0:
                        System.out.println();
                        System.out.println(">> Saindo do menu de administracao...");
                        pausa(1000);
                        break;
                    default:
                        System.out.println();
                        System.out.println(">> ERRO: Opcao invalida. Tente novamente.");
                        pausa(1500);
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(">> ERRO: Entrada invalida. Por favor, digite apenas numeros.");
                pausa(1500);
                opcao = -1; 
            }
        }
    }

    /**
     * Menu de Usuário (Leitor) - ATUALIZADO
     */
    private static void menuUsuario(Scanner scanner, Leitor leitor) {
        int opcao = -1;
    
        while (opcao != 0) {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                              MENU DO LEITOR                                   |");
            System.out.println("==================================================================================");
            System.out.println("|  Usuario: " + String.format("%-65s", leitor.getNome()) + "|");
            System.out.println("==================================================================================");
            System.out.println("|  1. Ver acervo completo                                                       |");
            System.out.println("|  2. Pesquisar livro por genero                                                |");
            System.out.println("|  3. Realizar emprestimo                                                       |");
            System.out.println("|  4. Devolver livro                                                            |");
            System.out.println("|  5. Ver meus emprestimos ativos                                               |");
            System.out.println("|  6. Ver meu historico completo                                                |");
            System.out.println("|  0. Sair (Logout)                                                             |");
            System.out.println("==================================================================================");
            System.out.println();
            System.out.print("Escolha uma opcao: ");
    
            try {
                opcao = Integer.parseInt(scanner.nextLine());
    
                switch (opcao) {
                    case 1:
                        listarTodosLivros();
                        break;
                    case 2:
                        pesquisarLivroPorGeneroUsuario(scanner);
                        break;
                    case 3:
                        realizarEmprestimo(scanner, leitor);
                        break;
                    case 4:
                        devolverLivro(scanner, leitor);
                        break;
                    case 5:
                        listarMeusEmprestimosAtivos(leitor);
                        break;
                    case 6:
                        listarMeuHistoricoEmprestimos(leitor);
                        break;
                    case 0:
                        System.out.println();
                        System.out.println(">> Saindo do menu de usuario...");
                        pausa(1000);
                        break;
                    default:
                        System.out.println();
                        System.out.println(">> ERRO: Opcao invalida. Tente novamente.");
                        pausa(1500);
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(">> ERRO: Entrada invalida. Por favor, digite apenas numeros.");
                pausa(1500);
                opcao = -1; 
            }
        }
    }
    
    // --- MÉTODOS DE AÇÃO DO ADMIN (Chamados pelos Menus) ---

    private static void adicionarLivroPeloAdmin(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                           ADICIONAR NOVO LIVRO                                |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("ID (ISBN, ex: B008): ");
            String id = scanner.nextLine();
            
            System.out.print("Titulo: ");
            String titulo = scanner.nextLine();
            
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            
            System.out.print("Genero: ");
            String genero = scanner.nextLine();
            
            Livro novoLivro = new Livro(id, titulo, autor, genero);
            livroDAO.cadastrarLivro(novoLivro);
            
            System.out.println();
            System.out.println(">> SUCCESS: Livro adicionado com sucesso!");
            pausa(1500);
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro ao ler dados: " + e.getMessage());
            pausa(2000);
        }
    }

    private static void removerLivroPeloAdmin(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                              REMOVER LIVRO                                    |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("ID do livro a ser removido (ex: B001): ");
            String id = scanner.nextLine();
            
            // Confirmar remoção
            System.out.println();
            System.out.print("Tem certeza que deseja remover este livro? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                livroDAO.removerLivro(id);
            } else {
                System.out.println();
                System.out.println(">> INFO: Remocao cancelada.");
            }
            pausa(1500);
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro ao remover livro: " + e.getMessage());
            pausa(2000);
        }
    }

    private static void editarLivroPeloAdmin(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                               EDITAR LIVRO                                    |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("ID do livro a ser editado (ex: B001): ");
            String id = scanner.nextLine();
            
            // Buscar livro atual para mostrar dados
            Livro livroAtual = livroDAO.buscarPorId(id);
            if (livroAtual == null) {
                System.out.println();
                System.out.println(">> ERRO: Livro nao encontrado!");
                pausa(1500);
                return;
            }
            
            System.out.println();
            System.out.println("Dados atuais do livro:");
            System.out.println("  Titulo: " + livroAtual.getTitulo());
            System.out.println("  Autor: " + livroAtual.getAutor());
            System.out.println("  Genero: " + livroAtual.getGenero());
            System.out.println();
            System.out.println("Digite os novos dados (deixe em branco para manter o atual):");
            System.out.println();
            
            System.out.print("Novo Titulo [" + livroAtual.getTitulo() + "]: ");
            String novoTitulo = scanner.nextLine();
            if (novoTitulo.isEmpty()) novoTitulo = livroAtual.getTitulo();
            
            System.out.print("Novo Autor [" + livroAtual.getAutor() + "]: ");
            String novoAutor = scanner.nextLine();
            if (novoAutor.isEmpty()) novoAutor = livroAtual.getAutor();
            
            System.out.print("Novo Genero [" + livroAtual.getGenero() + "]: ");
            String novoGenero = scanner.nextLine();
            if (novoGenero.isEmpty()) novoGenero = livroAtual.getGenero();
            
            livroDAO.editarLivro(id, novoTitulo, novoAutor, novoGenero);
            
            System.out.println();
            System.out.println(">> SUCCESS: Livro atualizado com sucesso!");
            pausa(1500);
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro ao editar livro: " + e.getMessage());
            pausa(2000);
        }
    }
    
    private static void listarTodosLeitores() {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                         LISTA DE TODOS OS LEITORES                             |");
        System.out.println("==================================================================================");
        System.out.println();
        
        List<Usuario> leitores = usuarioDAO.listarTodosLeitores();
        
        if (leitores.isEmpty()) {
            System.out.println(">> INFO: Nenhum leitor cadastrado.");
        } else {
            System.out.println("+----------+--------------------------------+---------------------+");
            System.out.println("|    ID    |             Nome              |      Username       |");
            System.out.println("+----------+--------------------------------+---------------------+");
            
            for (Usuario leitor : leitores) {
                System.out.printf("| %-8s | %-30s | %-19s |\n",
                    leitor.getId(), 
                    truncarString(leitor.getNome(), 30), 
                    truncarString(leitor.getUsername(), 19)
                );
            }
            System.out.println("+----------+--------------------------------+---------------------+");
        }
        
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }

    private static void removerLeitorPeloAdmin(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                             REMOVER LEITOR                                    |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("ID do leitor a ser removido (ex: L001): ");
            String id = scanner.nextLine();
            
            // Confirmar remoção
            System.out.println();
            System.out.print("Tem certeza que deseja remover este leitor? (s/n): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                usuarioDAO.removerUsuario(id);
            } else {
                System.out.println();
                System.out.println(">> INFO: Remocao cancelada.");
            }
            pausa(1500);
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro ao remover leitor: " + e.getMessage());
            pausa(2000);
        }
    }

    private static void listarTodosEmprestimos() {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                         TODOS OS EMPRESTIMOS                                   |");
        System.out.println("==================================================================================");
        System.out.println();
        
        List<String> emprestimos = emprestimoDAO.listarTodosEmprestimos();
        
        if (emprestimos.isEmpty()) {
            System.out.println(">> INFO: Nenhum emprestimo registrado.");
        } else {
            for (String info : emprestimos) {
                System.out.println("- " + info);
                System.out.println("----------------------------------------------------------------------------------");
            }
        }
        
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }

    private static void pesquisarLivroPorGenero(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                        PESQUISAR LIVRO POR GENERO                             |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("Digite o genero: ");
            String genero = scanner.nextLine();
            
            List<Livro> livros = livroDAO.listarPorGenero(genero);
            
            if (livros.isEmpty()) {
                System.out.println();
                System.out.println(">> ERRO: Nenhum livro encontrado para o genero: " + genero);
            } else {
                System.out.println();
                System.out.println("Livros do Genero: " + genero);
                System.out.println("+----------+---------------------------------------------+---------------------------+------------------+--------------+");
                System.out.println("|    ID    |                  Titulo                     |           Autor           |      Genero      | Disponivel?  |");
                System.out.println("+----------+---------------------------------------------+---------------------------+------------------+--------------+");
                
                for (Livro livro : livros) {
                    String disponivel = livro.isDisponivel() ? "Sim" : "Nao";
                    System.out.printf("| %-8s | %-43s | %-25s | %-16s | %-12s |\n",
                        truncarString(livro.getId(), 8),
                        truncarString(livro.getTitulo(), 43),
                        truncarString(livro.getAutor(), 25),
                        truncarString(livro.getGenero(), 16),
                        disponivel
                    );
                }
                System.out.println("+----------+---------------------------------------------+---------------------------+------------------+--------------+");
            }
            
            System.out.println();
            System.out.print("Pressione Enter para continuar...");
            new Scanner(System.in).nextLine();
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro na pesquisa: " + e.getMessage());
            pausa(2000);
        }
    }

    // --- MÉTODOS COMPARTILHADOS ---
    
    /** Método atualizado para mostrar Gênero */
    private static void listarTodosLivros() {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                            ACERVO COMPLETO                                    |");
        System.out.println("==================================================================================");
        System.out.println();
        
        List<Livro> livros = livroDAO.listarTodosLivros();
        
        if (livros.isEmpty()) {
            System.out.println(">> INFO: Nenhum livro cadastrado no acervo.");
        } else {
            System.out.println("+----------+---------------------------------------------+---------------------------+------------------+--------------+");
            System.out.println("|    ID    |                  Titulo                     |           Autor           |      Genero      | Disponivel?  |");
            System.out.println("+----------+---------------------------------------------+---------------------------+------------------+--------------+");
            
            for (Livro livro : livros) {
                String disponivel = livro.isDisponivel() ? "Sim" : "Nao";
                System.out.printf("| %-8s | %-43s | %-25s | %-16s | %-12s |\n",
                    truncarString(livro.getId(), 8),
                    truncarString(livro.getTitulo(), 43),
                    truncarString(livro.getAutor(), 25),
                    truncarString(livro.getGenero(), 16),
                    disponivel
                );
            }
            System.out.println("+----------+---------------------------------------------+---------------------------+------------------+--------------+");
        }
        
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }
    
    private static void cadastrarNovoLeitor(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                         CADASTRAR NOVO LEITOR                                 |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("ID (ex: L006): ");
            String id = scanner.nextLine();
            
            System.out.print("Nome Completo: ");
            String nome = scanner.nextLine();
            
            System.out.print("Username (login): ");
            String username = scanner.nextLine();
            
            System.out.print("Password (senha): ");
            String password = scanner.nextLine();
            
            usuarioDAO.cadastrarUsuario(id, nome, username, password);
            
            System.out.println();
            System.out.println(">> SUCCESS: Leitor cadastrado com sucesso!");
            pausa(1500);
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro ao ler dados: " + e.getMessage());
            pausa(2000);
        }
    }

    // --- MÉTODOS DO USUÁRIO ---
    
    private static void pesquisarLivroPorGeneroUsuario(Scanner scanner) {
        try {
            limparTela();
            System.out.println("==================================================================================");
            System.out.println("|                        PESQUISAR LIVRO POR GENERO                             |");
            System.out.println("==================================================================================");
            System.out.println();
            
            System.out.print("Digite o genero: ");
            String genero = scanner.nextLine();
            
            List<Livro> livros = livroDAO.listarPorGenero(genero);
            
            if (livros.isEmpty()) {
                System.out.println();
                System.out.println(">> ERRO: Nenhum livro encontrado para o genero: " + genero);
            } else {
                System.out.println();
                System.out.println("Livros Disponiveis do Genero: " + genero);
                System.out.println("+----------+---------------------------------------------+---------------------------+");
                System.out.println("|    ID    |                  Titulo                     |           Autor           |");
                System.out.println("+----------+---------------------------------------------+---------------------------+");
                
                boolean encontrouDisponivel = false;
                for (Livro livro : livros) {
                    if (livro.isDisponivel()) {
                        encontrouDisponivel = true;
                        System.out.printf("| %-8s | %-43s | %-25s |\n",
                            truncarString(livro.getId(), 8),
                            truncarString(livro.getTitulo(), 43),
                            truncarString(livro.getAutor(), 25)
                        );
                    }
                }
                System.out.println("+----------+---------------------------------------------+---------------------------+");
                
                if (!encontrouDisponivel) {
                    System.out.println();
                    System.out.println(">> INFO: Nenhum livro disponivel no momento para este genero.");
                }
            }
            
            System.out.println();
            System.out.print("Pressione Enter para continuar...");
            new Scanner(System.in).nextLine();
            
        } catch (Exception e) {
            System.err.println(">> ERRO: Erro na pesquisa: " + e.getMessage());
            pausa(2000);
        }
    }
    
    private static void realizarEmprestimo(Scanner scanner, Leitor leitor) {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                          REALIZAR EMPRESTIMO                                  |");
        System.out.println("==================================================================================");
        System.out.println();
        
        System.out.print("Digite o ID do livro que deseja emprestar (ex: B001): ");
        String livroId = scanner.nextLine();

        // 1. Verificar se o livro existe
        Livro livro = livroDAO.buscarPorId(livroId);
        if (livro == null) {
            System.out.println();
            System.out.println(">> ERRO: Livro com ID '" + livroId + "' nao encontrado.");
            pausa(2000);
            return;
        }

        // 2. Verificar se o livro está disponível
        if (!livro.isDisponivel()) {
            System.out.println();
            System.out.println(">> ERRO: O livro '" + livro.getTitulo() + "' ja esta emprestado.");
            pausa(2000);
            return;
        }

        // 3. Se estiver tudo OK, registrar o empréstimo
        boolean sucessoEmprestimo = emprestimoDAO.realizarEmprestimo(leitor.getId(), livro.getId());
        
        if (sucessoEmprestimo) {
            // 4. Atualizar a disponibilidade do livro
            livroDAO.atualizarDisponibilidade(livro.getId(), false);
            System.out.println();
            System.out.println(">> SUCCESS: Emprestimo do livro '" + livro.getTitulo() + "' realizado com sucesso!");
            System.out.println(">> Data de vencimento: " + java.time.LocalDate.now().plusDays(14));
        } else {
            System.out.println();
            System.out.println(">> ERRO: Nao foi possivel registrar o emprestimo no banco de dados.");
        }
        pausa(2000);
    }

    private static void devolverLivro(Scanner scanner, Leitor leitor) {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                             DEVOLVER LIVRO                                    |");
        System.out.println("==================================================================================");
        System.out.println();
        
        System.out.print("Digite o ID do livro que deseja devolver (ex: B001): ");
        String livroId = scanner.nextLine();

        // 1. Tentar registrar a devolução
        boolean sucessoDevolucao = emprestimoDAO.registrarDevolucao(leitor.getId(), livroId);

        if (sucessoDevolucao) {
            // 2. Atualizar a disponibilidade do livro
            livroDAO.atualizarDisponibilidade(livroId, true);
            System.out.println();
            System.out.println(">> SUCCESS: Devolucao registrada com sucesso!");
        } else {
            System.out.println();
            System.out.println(">> ERRO: Nao foi possivel registrar a devolucao.");
            System.out.println(">> DICA: Verifique se o ID do livro esta correto e se voce possui este emprestimo.");
        }
        pausa(2000);
    }
    
    private static void listarMeusEmprestimosAtivos(Leitor leitor) {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                         MEUS EMPRESTIMOS ATIVOS                               |");
        System.out.println("==================================================================================");
        System.out.println();
        
        List<String> emprestimos = emprestimoDAO.listarEmprestimosAtivosPorUsuario(leitor.getId());

        if (emprestimos.isEmpty()) {
            System.out.println(">> INFO: Voce nao possui nenhum livro emprestado no momento.");
        } else {
            for (String info : emprestimos) {
                System.out.println("- " + info);
                System.out.println("----------------------------------------------------------------------------------");
            }
        }
        
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }

    private static void listarMeuHistoricoEmprestimos(Leitor leitor) {
        limparTela();
        System.out.println("==================================================================================");
        System.out.println("|                        MEU HISTORICO DE EMPRESTIMOS                           |");
        System.out.println("==================================================================================");
        System.out.println();
        
        List<String> historico = emprestimoDAO.listarHistoricoEmprestimos(leitor.getId());

        if (historico.isEmpty()) {
            System.out.println(">> INFO: Voce nao possui historico de emprestimos.");
        } else {
            for (String info : historico) {
                System.out.println("- " + info);
                System.out.println("----------------------------------------------------------------------------------");
            }
        }
        
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }

    // --- MÉTODOS AUXILIARES ---
    
    /**
     * Limpa a tela do console (funciona na maioria dos terminais)
     */
    private static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    /**
     * Pausa a execução por um tempo determinado
     */
    private static void pausa(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Trunca uma string se for muito longa para caber na tabela
     */
    private static String truncarString(String texto, int comprimentoMaximo) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= comprimentoMaximo) {
            return texto;
        }
        return texto.substring(0, comprimentoMaximo - 3) + "...";
    }
}
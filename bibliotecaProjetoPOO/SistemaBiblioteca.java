package bibliotecaProjetoPOO;

import java.util.List;
import java.util.Scanner;

public class SistemaBiblioteca {
    
    // Criamos instâncias dos DAOs que serão usadas pelos menus
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static LivroDAO livroDAO = new LivroDAO();
    // Adicionamos o novo DAO
    private static EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    public static void main(String[] args) {
        
        // 1. Inicia o banco de dados e cria as tabelas
        ConexaoSQLite.criarTabelas();
        
        try (Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("=====================================");
            System.out.println("== BEM-VINDO À BIBLIOTECA VIRTUAL ==");
            System.out.println("=====================================");
            System.out.println("\nPor favor, faça o login:");

            System.out.print("Usuário: ");
            String username = scanner.nextLine();

            System.out.print("Senha: ");
            String password = scanner.nextLine();
            
            // 2. Lógica de Login via DAO
            Usuario usuarioLogado = usuarioDAO.login(username, password);

            if (usuarioLogado != null) {
                System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + ".");
                
                // 3. Direciona para o menu correto
                if (usuarioLogado.getTipo().equals("Admin")) {
                    menuAdmin(scanner);
                } else {
                    menuUsuario(scanner, (Leitor) usuarioLogado);
                }
                
            } else {
                System.out.println("\nUsuário ou senha inválidos. Encerrando.");
            }

        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }

        System.out.println("\nObrigado por usar nosso sistema. Volte sempre!");
    }

    /**
     * Menu de Administrador
     */
    private static void menuAdmin(Scanner scanner) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Adicionar livro ao acervo");
            System.out.println("2. Remover livro do acervo (TODO)");
            System.out.println("3. Listar todos os livros");
            System.out.println("4. Cadastrar novo leitor");
            // TODO: Adicionar "Listar todos empréstimos"
            System.out.println("0. Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        adicionarLivroPeloAdmin(scanner);
                        break;
                    case 2:
                        System.out.println("-> (ADMIN) Função 'Remover Livro' (TODO)");
                        // TODO: Chamar livroDAO.removerLivro(id)
                        break;
                    case 3:
                        listarTodosLivros();
                        break;
                    case 4:
                        cadastrarNovoLeitor(scanner);
                        break;
                    case 0:
                        System.out.println("Saindo do menu de administração...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite apenas números.");
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
	        System.out.println("\n--- MENU USUÁRIO (" + leitor.getNome() + ") ---");
	        System.out.println("1. Ver acervo completo");
	        System.out.println("2. Pesquisar livro por gênero (TODO)");
	        System.out.println("3. Realizar empréstimo");
	        System.out.println("4. Devolver livro");
	        System.out.println("5. Ver meus empréstimos ativos");
	        System.out.println("6. Ver meu histórico completo"); // NOVA OPÇÃO
	        System.out.println("0. Sair (Logout)");
	        System.out.print("Escolha uma opção: ");
	
	        try {
	            opcao = Integer.parseInt(scanner.nextLine());
	
	            switch (opcao) {
	                case 1:
	                    listarTodosLivros();
	                    break;
	                case 2:
	                    System.out.println("-> (USUÁRIO) Função 'Pesquisar por Gênero' (TODO)");
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
	                    System.out.println("Saindo do menu de usuário...");
	                    break;
	                default:
	                    System.out.println("Opção inválida. Tente novamente.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada inválida. Por favor, digite apenas números.");
	            opcao = -1; 
	        }
	    }
	}
    
    // --- MÉTODOS DE AÇÃO (Chamados pelos Menus) ---

    private static void adicionarLivroPeloAdmin(Scanner scanner) {
        try {
            System.out.println("\n--- Adicionar Novo Livro ---");
            System.out.print("ID (ISBN, ex: B008): ");
            String id = scanner.nextLine();
            
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            
            System.out.print("Gênero: ");
            String genero = scanner.nextLine();
            
            Livro novoLivro = new Livro(id, titulo, autor, genero);
            livroDAO.cadastrarLivro(novoLivro);
            
        } catch (Exception e) {
            System.err.println("Erro ao ler dados: " + e.getMessage());
        }
    }
    
    /** Método atualizado para mostrar Gênero */
    private static void listarTodosLivros() {
        System.out.println("\n--- Acervo Completo ---");
        List<Livro> livros = livroDAO.listarTodosLivros();
        
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado no acervo.");
        } else {
            for (Livro livro : livros) {
                System.out.println(
                    String.format("ID: %-6s | Título: %-30s | Gênero: %-15s | Disponível: %s",
                        livro.getId(), 
                        livro.getTitulo(), 
                        livro.getGenero(),
                        (livro.isDisponivel() ? "Sim" : "Não")
                    )
                );
            }
        }
    }
    
    private static void cadastrarNovoLeitor(Scanner scanner) {
        try {
            System.out.println("\n--- Cadastrar Novo Leitor ---");
            System.out.print("ID (ex: L006): ");
            String id = scanner.nextLine();
            
            System.out.print("Nome Completo: ");
            String nome = scanner.nextLine();
            
            System.out.print("Username (login): ");
            String username = scanner.nextLine();
            
            System.out.print("Password (senha): ");
            String password = scanner.nextLine();
            
            usuarioDAO.cadastrarUsuario(id, nome, username, password);
            
        } catch (Exception e) {
            System.err.println("Erro ao ler dados: " + e.getMessage());
        }
    }
    
    private static void realizarEmprestimo(Scanner scanner, Leitor leitor) {
        System.out.println("\n--- Realizar Empréstimo ---");
        System.out.println("Digite o ID do livro que deseja emprestar (ex: B001):");
        String livroId = scanner.nextLine();

        // 1. Verificar se o livro existe
        Livro livro = livroDAO.buscarPorId(livroId);
        if (livro == null) {
            System.out.println("Erro: Livro com ID '" + livroId + "' não encontrado.");
            return;
        }

        // 2. Verificar se o livro está disponível
        if (!livro.isDisponivel()) {
            System.out.println("Erro: O livro '" + livro.getTitulo() + "' já está emprestado.");
            return;
        }

        // 3. Se estiver tudo OK, registrar o empréstimo
        boolean sucessoEmprestimo = emprestimoDAO.realizarEmprestimo(leitor.getId(), livro.getId());
        
        if (sucessoEmprestimo) {
            // 4. Atualizar a disponibilidade do livro
            livroDAO.atualizarDisponibilidade(livro.getId(), false); // false = não disponível
            System.out.println("Empréstimo do livro '" + livro.getTitulo() + "' realizado com sucesso!");
        } else {
            System.out.println("Erro: Não foi possível registrar o empréstimo no banco de dados.");
        }
    }

    private static void devolverLivro(Scanner scanner, Leitor leitor) {
        System.out.println("\n--- Devolver Livro ---");
        System.out.println("Digite o ID do livro que deseja devolver (ex: B001):");
        String livroId = scanner.nextLine();

        // 1. Tentar registrar a devolução
        boolean sucessoDevolucao = emprestimoDAO.registrarDevolucao(leitor.getId(), livroId);

        if (sucessoDevolucao) {
            // 2. Atualizar a disponibilidade do livro
            livroDAO.atualizarDisponibilidade(livroId, true); // true = disponível
            System.out.println("Devolução registrada com sucesso!");
        } else {
            // Isso pode falhar se o livro ID estiver errado ou se
            // o usuário não tiver um empréstimo ativo para esse livro.
            System.out.println("Erro: Não foi possível registrar a devolução.");
            System.out.println("Verifique se o ID do livro está correto e se você possui este empréstimo.");
        }
    }
    
    private static void listarMeusEmprestimosAtivos(Leitor leitor) {
        System.out.println("\n--- Meus Empréstimos Ativos ---");
        List<String> emprestimos = emprestimoDAO.listarEmprestimosAtivosPorUsuario(leitor.getId());

        if (emprestimos.isEmpty()) {
            System.out.println("Você não possui nenhum livro emprestado no momento.");
        } else {
            for (String info : emprestimos) {
                System.out.println(info);
            }
        }
    }

    private static void listarMeuHistoricoEmprestimos(Leitor leitor) {
        System.out.println("\n--- Meu Histórico de Empréstimos ---");
        List<String> historico = emprestimoDAO.listarHistoricoEmprestimos(leitor.getId());

        if (historico.isEmpty()) {
            System.out.println("Você não possui histórico de empréstimos.");
        } else {
            for (String info : historico) {
                System.out.println(info);
            }
        }
    }
}
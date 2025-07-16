package com.locadora.locadoraapp;

import com.locadora.locadoraapp.model.Cliente;
import com.locadora.locadoraapp.model.Filme;
import com.locadora.locadoraapp.model.Locacao;
import com.locadora.locadoraapp.service.ClienteService;
import com.locadora.locadoraapp.service.FilmeService;
import com.locadora.locadoraapp.service.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LocadoraAppApplication {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private FilmeService filmeService;
    @Autowired
    private LocacaoService locacaoService;

    @Autowired
    private ApplicationContext appContext;

    public static void main(String[] args) {
        SpringApplication.run(LocadoraAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            int opcao;

            System.out.println("--- BEM-VINDO AO SISTEMA DE LOCADORA DE FILMES (TERMINAL) ---");

            do {
                exibirMenuPrincipal();
                opcao = lerInteiro(scanner, "Escolha uma opção: ");

                try {
                    switch (opcao) {
                        case 1:
                            menuClientes(scanner);
                            break;
                        case 2:
                            menuFilmes(scanner);
                            break;
                        case 3:
                            menuLocacoes(scanner);
                            break;
                        case 0:
                            System.out.println("Saindo do sistema. Até mais!");
                            break; 
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                } catch (Exception e) {
                    System.err.println("Ocorreu um erro: " + e.getMessage());
                }

                if (opcao != 0) {
                    System.out.println("\nPressione ENTER para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                }

            } while (opcao != 0);

            scanner.close();
            SpringApplication.exit(appContext, () -> 0);
        };
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Filmes");
        System.out.println("3. Gerenciar Locações");
        System.out.println("0. Sair");
        System.out.print("-----------------------\n");
    }

    private void menuClientes(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- GERENCIAR CLIENTES ---");
            System.out.println("1. Inserir Novo Cliente");
            System.out.println("2. Listar Todos os Clientes");
            System.out.println("3. Buscar Cliente por ID");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Deletar Cliente");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerInteiro(scanner, "Escolha uma opção para Clientes: ");

            switch (opcao) {
                case 1: inserirCliente(scanner); break;
                case 2: listarClientes(); break;
                case 3: buscarClientePorId(scanner); break;
                case 4: atualizarCliente(scanner); break;
                case 5: deletarCliente(scanner); break;
                case 0: System.out.println("Voltando ao menu principal..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void inserirCliente(Scanner scanner) {
        System.out.println("\n--- INSERIR NOVO CLIENTE ---");
        Cliente cliente = new Cliente();
        System.out.print("Nome: ");
        scanner.nextLine();
        cliente.setNome(scanner.nextLine());
        System.out.print("Sobrenome: ");
        cliente.setSobrenome(scanner.nextLine());
        System.out.print("Email: ");
        cliente.setEmail(scanner.nextLine());
        System.out.print("Telefone (opcional): ");
        cliente.setTelefone(scanner.nextLine());
        System.out.print("Endereço (opcional): ");
        cliente.setEndereco(scanner.nextLine());

        try {
            clienteService.salvarCliente(cliente);
            System.out.println("Cliente '" + cliente.getNome() + " " + cliente.getSobrenome() + "' inserido com sucesso! ID: " + cliente.getClienteId());
        } catch (Exception e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        List<Cliente> clientes = clienteService.buscarTodosClientes();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        clientes.forEach(System.out::println);
    }

    private void buscarClientePorId(Scanner scanner) {
        System.out.println("\n--- BUSCAR CLIENTE POR ID ---");
        Long id = lerLong(scanner, "Digite o ID do cliente: ");
        Optional<Cliente> cliente = clienteService.buscarClientePorId(id);
        cliente.ifPresentOrElse(
            System.out::println,
            () -> System.out.println("Cliente com ID " + id + " não encontrado.")
        );
    }

    private void atualizarCliente(Scanner scanner) {
        System.out.println("\n--- ATUALIZAR CLIENTE ---");
        Long id = lerLong(scanner, "Digite o ID do cliente a ser atualizado: ");

        Optional<Cliente> optionalCliente = clienteService.buscarClientePorId(id);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            System.out.println("Cliente atual: " + cliente);

            System.out.print("Novo Nome (" + cliente.getNome() + "): ");
            scanner.nextLine();
            String novoNome = scanner.nextLine();
            if (!novoNome.isEmpty()) cliente.setNome(novoNome);

            System.out.print("Novo Sobrenome (" + cliente.getSobrenome() + "): ");
            String novoSobrenome = scanner.nextLine();
            if (!novoSobrenome.isEmpty()) cliente.setSobrenome(novoSobrenome);

            System.out.print("Novo Email (" + cliente.getEmail() + "): ");
            String novoEmail = scanner.nextLine();
            if (!novoEmail.isEmpty()) cliente.setEmail(novoEmail);

            System.out.print("Novo Telefone (" + (cliente.getTelefone() != null ? cliente.getTelefone() : "") + "): ");
            String novoTelefone = scanner.nextLine();
            if (!novoTelefone.isEmpty()) cliente.setTelefone(novoTelefone);

            System.out.print("Novo Endereço (" + (cliente.getEndereco() != null ? cliente.getEndereco() : "") + "): ");
            String novoEndereco = scanner.nextLine();
            if (!novoEndereco.isEmpty()) cliente.setEndereco(novoEndereco);

            try {
                clienteService.salvarCliente(cliente);
                System.out.println("Cliente atualizado com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            }
        } else {
            System.out.println("Cliente com ID " + id + " não encontrado.");
        }
    }

    private void deletarCliente(Scanner scanner) {
        System.out.println("\n--- DELETAR CLIENTE ---");
        Long id = lerLong(scanner, "Digite o ID do cliente a ser deletado: ");

        try {
            if (clienteService.existeCliente(id)) {
                clienteService.deletarCliente(id);
                System.out.println("Cliente com ID " + id + " deletado com sucesso!");
            } else {
                System.out.println("Cliente com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao deletar cliente (pode haver locações associadas): " + e.getMessage());
        }
    }

    private void menuFilmes(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- GERENCIAR FILMES ---");
            System.out.println("1. Inserir Novo Filme");
            System.out.println("2. Listar Todos os Filmes");
            System.out.println("3. Buscar Filme por ID");
            System.out.println("4. Buscar Filmes por Gênero");
            System.out.println("5. Atualizar Filme");
            System.out.println("6. Deletar Filme");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerInteiro(scanner, "Escolha uma opção para Filmes: ");

            switch (opcao) {
                case 1: inserirFilme(scanner); break;
                case 2: listarFilmes(); break;
                case 3: buscarFilmePorId(scanner); break;
                case 4: buscarFilmesPorGenero(scanner); break;
                case 5: atualizarFilme(scanner); break;
                case 6: deletarFilme(scanner); break;
                case 0: System.out.println("Voltando ao menu principal..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void inserirFilme(Scanner scanner) {
        System.out.println("\n--- INSERIR NOVO FILME ---");
        Filme filme = new Filme();
        System.out.print("Título: ");
        scanner.nextLine();
        filme.setTitulo(scanner.nextLine());
        System.out.print("Gênero: ");
        filme.setGenero(scanner.nextLine());
        filme.setAnoLancamento(lerInteiro(scanner, "Ano de Lançamento: "));
        System.out.print("Diretor: ");
        scanner.nextLine();
        filme.setDiretor(scanner.nextLine());
        filme.setQuantidadeEstoque(lerInteiro(scanner, "Quantidade em Estoque: "));

        try {
            filmeService.salvarFilme(filme);
            System.out.println("Filme '" + filme.getTitulo() + "' inserido com sucesso! ID: " + filme.getFilmeId());
        } catch (Exception e) {
            System.err.println("Erro ao inserir filme: " + e.getMessage());
        }
    }

    private void listarFilmes() {
        System.out.println("\n--- LISTA DE FILMES ---");
        List<Filme> filmes = filmeService.buscarTodosFilmes();
        if (filmes.isEmpty()) {
            System.out.println("Nenhum filme cadastrado.");
            return;
        }
        filmes.forEach(System.out::println);
    }

    private void buscarFilmePorId(Scanner scanner) {
        System.out.println("\n--- BUSCAR FILME POR ID ---");
        Long id = lerLong(scanner, "Digite o ID do filme: ");
        Optional<Filme> filme = filmeService.buscarFilmePorId(id);
        filme.ifPresentOrElse(
            System.out::println,
            () -> System.out.println("Filme com ID " + id + " não encontrado.")
        );
    }

    private void buscarFilmesPorGenero(Scanner scanner) {
        System.out.println("\n--- BUSCAR FILMES POR GÊNERO ---");
        System.out.print("Digite o gênero: ");
        scanner.nextLine();
        String genero = scanner.nextLine();
        List<Filme> filmes = filmeService.buscarFilmesPorGenero(genero);
        if (filmes.isEmpty()) {
            System.out.println("Nenhum filme encontrado para o gênero '" + genero + "'.");
            return;
        }
        filmes.forEach(System.out::println);
    }

    private void atualizarFilme(Scanner scanner) {
        System.out.println("\n--- ATUALIZAR FILME ---");
        Long id = lerLong(scanner, "Digite o ID do filme a ser atualizado: ");

        Optional<Filme> optionalFilme = filmeService.buscarFilmePorId(id);
        if (optionalFilme.isPresent()) {
            Filme filme = optionalFilme.get();
            System.out.println("Filme atual: " + filme);

            System.out.print("Novo Título (" + filme.getTitulo() + "): ");
            scanner.nextLine();
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.isEmpty()) filme.setTitulo(novoTitulo);

            System.out.print("Novo Gênero (" + (filme.getGenero() != null ? filme.getGenero() : "") + "): ");
            String novoGenero = scanner.nextLine();
            if (!novoGenero.isEmpty()) filme.setGenero(novoGenero);

            System.out.print("Novo Ano de Lançamento (" + (filme.getAnoLancamento() != null ? filme.getAnoLancamento() : "") + ", 0 para ignorar): ");
            int novoAno = lerInteiro(scanner, "");
            if (novoAno != 0) filme.setAnoLancamento(novoAno);

            System.out.print("Novo Diretor (" + (filme.getDiretor() != null ? filme.getDiretor() : "") + "): ");
            scanner.nextLine();
            String novoDiretor = scanner.nextLine();
            if (!novoDiretor.isEmpty()) filme.setDiretor(novoDiretor);

            System.out.print("Nova Quantidade em Estoque (" + (filme.getQuantidadeEstoque() != null ? filme.getQuantidadeEstoque() : "") + ", -1 para ignorar): ");
            int novoEstoque = lerInteiro(scanner, "");
            if (novoEstoque != -1) filme.setQuantidadeEstoque(novoEstoque);

            try {
                filmeService.salvarFilme(filme);
                System.out.println("Filme atualizado com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao atualizar filme: " + e.getMessage());
            }
        } else {
            System.out.println("Filme com ID " + id + " não encontrado.");
        }
    }

    private void deletarFilme(Scanner scanner) {
        System.out.println("\n--- DELETAR FILME ---");
        Long id = lerLong(scanner, "Digite o ID do filme a ser deletado: ");

        try {
            if (filmeService.existeFilme(id)) {
                List<Locacao> locacoesDoFilme = locacaoService.findByFilme_FilmeId(id);
                if (!locacoesDoFilme.isEmpty()) {
                    System.out.println("ERRO: Não é possível deletar este filme. Existem " + locacoesDoFilme.size() + " locações associadas a ele.");
                    System.out.println("Por favor, delete as locações associadas primeiro ou marque-as como devolvidas.");
                    return;
                }

                filmeService.deletarFilme(id);
                System.out.println("Filme com ID " + id + " deletado com sucesso!");
            } else {
                System.out.println("Filme com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao deletar filme: " + e.getMessage());
        }
    }

    private void menuLocacoes(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n--- GERENCIAR LOCAÇÕES ---");
            System.out.println("1. Inserir Nova Locação");
            System.out.println("2. Listar Todas as Locações");
            System.out.println("3. Buscar Locação por ID");
            System.out.println("4. Listar Locações Não Devolvidas");
            System.out.println("5. Registrar Devolução de Locação");
            System.out.println("6. Deletar Locação");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerInteiro(scanner, "Escolha uma opção para Locações: ");

            switch (opcao) {
                case 1: inserirLocacao(scanner); break;
                case 2: listarLocacoes(); break;
                case 3: buscarLocacaoPorId(scanner); break;
                case 4: listarLocacoesNaoDevolvidas(); break;
                case 5: registrarDevolucaoLocacao(scanner); break;
                case 6: deletarLocacao(scanner); break;
                case 0: System.out.println("Voltando ao menu principal..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void inserirLocacao(Scanner scanner) {
        System.out.println("\n--- INSERIR NOVA LOCAÇÃO ---");
        Locacao locacao = new Locacao();

        Long clienteId = lerLong(scanner, "Digite o ID do Cliente: ");
        Optional<Cliente> cliente = clienteService.buscarClientePorId(clienteId);
        if (cliente.isEmpty()) {
            System.out.println("Cliente com ID " + clienteId + " não encontrado.");
            return;
        }
        locacao.setCliente(cliente.get());

        Long filmeId = lerLong(scanner, "Digite o ID do Filme: ");
        Optional<Filme> filme = filmeService.buscarFilmePorId(filmeId);
        if (filme.isEmpty()) {
            System.out.println("Filme com ID " + filmeId + " não encontrado.");
            return;
        }
        locacao.setFilme(filme.get());

        locacao.setValorTotal(lerBigDecimal(scanner, "Valor Total da Locação (ex: 12.50): "));

        try {
            locacaoService.salvarLocacao(locacao);
            System.out.println("Locação inserida com sucesso! ID: " + locacao.getLocacaoId());
        } catch (RuntimeException e) {
            System.err.println("Erro ao inserir locação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao inserir locação: " + e.getMessage());
        }
    }

    private void listarLocacoes() {
        System.out.println("\n--- LISTA DE LOCAÇÕES ---");
        List<Locacao> locacoes = locacaoService.buscarTodasLocacoes();
        if (locacoes.isEmpty()) {
            System.out.println("Nenhuma locação cadastrada.");
            return;
        }
        locacoes.forEach(locacao -> {
            String clienteNome = locacao.getCliente() != null ? locacao.getCliente().getNome() : "Desconhecido";
            String filmeTitulo = locacao.getFilme() != null ? locacao.getFilme().getTitulo() : "Desconhecido";
            System.out.println("ID: " + locacao.getLocacaoId() +
                               ", Cliente: " + clienteNome +
                               ", Filme: " + filmeTitulo +
                               ", Data Locação: " + locacao.getDataLocacao() +
                               ", Data Devolução: " + (locacao.getDataDevolucao() != null ? locacao.getDataDevolucao() : "Pendente") +
                               ", Valor: " + locacao.getValorTotal());
        });
    }

    private void buscarLocacaoPorId(Scanner scanner) {
        System.out.println("\n--- BUSCAR LOCAÇÃO POR ID ---");
        Long id = lerLong(scanner, "Digite o ID da locação: ");
        Optional<Locacao> locacao = locacaoService.buscarLocacaoPorId(id);
        locacao.ifPresentOrElse(
            l -> {
                String clienteNome = l.getCliente() != null ? l.getCliente().getNome() : "Desconhecido";
                String filmeTitulo = l.getFilme() != null ? l.getFilme().getTitulo() : "Desconhecido";
                System.out.println("ID: " + l.getLocacaoId() +
                                   ", Cliente: " + clienteNome +
                                   ", Filme: " + filmeTitulo +
                                   ", Data Locação: " + l.getDataLocacao() +
                                   ", Data Devolução: " + (l.getDataDevolucao() != null ? l.getDataDevolucao() : "Pendente") +
                                   ", Valor: " + l.getValorTotal());
            },
            () -> System.out.println("Locação com ID " + id + " não encontrada.")
        );
    }

    private void listarLocacoesNaoDevolvidas() {
        System.out.println("\n--- LOCAÇÕES NÃO DEVOLVIDAS ---");
        List<Locacao> locacoes = locacaoService.buscarLocacoesNaoDevolvidas();
        if (locacoes.isEmpty()) {
            System.out.println("Nenhuma locação pendente de devolução.");
            return;
        }
        locacoes.forEach(locacao -> {
            String clienteNome = locacao.getCliente() != null ? locacao.getCliente().getNome() : "Desconhecido";
            String filmeTitulo = locacao.getFilme() != null ? locacao.getFilme().getTitulo() : "Desconhecido";
            System.out.println("ID: " + locacao.getLocacaoId() +
                               ", Cliente: " + clienteNome +
                               ", Filme: " + filmeTitulo +
                               ", Data Locação: " + locacao.getDataLocacao() +
                               ", Valor: " + locacao.getValorTotal());
        });
    }

    private void registrarDevolucaoLocacao(Scanner scanner) {
        System.out.println("\n--- REGISTRAR DEVOLUÇÃO DE LOCAÇÃO ---");
        Long id = lerLong(scanner, "Digite o ID da locação a ser devolvida: ");

        try {
            Locacao locacaoAtualizada = locacaoService.registrarDevolucao(id);
            System.out.println("Devolução da locação " + id + " registrada com sucesso em: " + locacaoAtualizada.getDataDevolucao());
        } catch (RuntimeException e) {
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
        }
    }

    private void deletarLocacao(Scanner scanner) {
        System.out.println("\n--- DELETAR LOCAÇÃO ---");
        Long id = lerLong(scanner, "Digite o ID da locação a ser deletada: ");

        try {
            if (locacaoService.existeLocacao(id)) {
                locacaoService.deletarLocacao(id);
                System.out.println("Locação com ID " + id + " deletada com sucesso!");
            } else {
                System.out.println("Locação com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao deletar locação: " + e.getMessage());
        }
    }

    private int lerInteiro(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = scanner.nextInt();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                scanner.nextLine();
            }
        }
    }

    private Long lerLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                long valor = scanner.nextLong();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro longo.");
                scanner.nextLine();
            }
        }
    }

    private BigDecimal lerBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                scanner.nextLine();
                String valorStr = scanner.nextLine();
                return new BigDecimal(valorStr);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um valor numérico (ex: 12.50).");
            }
        }
    }
}
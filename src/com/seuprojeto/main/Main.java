package com.seuprojeto.main;

import com.seuprojeto.dao.ClienteDAO;
import com.seuprojeto.dao.EstoqueDAO;
import com.seuprojeto.dao.ProdutoDAO;
import com.seuprojeto.domain.Cliente;
import com.seuprojeto.domain.Estoque;
import com.seuprojeto.domain.Produto;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.TipoChaveNaoEncontradaException;
import com.seuprojeto.service.ClienteService;
import com.seuprojeto.service.EstoqueService;
import com.seuprojeto.service.IClienteService;
import com.seuprojeto.service.IEstoqueService;
import com.seuprojeto.service.IProdutoService;
import com.seuprojeto.service.ProdutoService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static IClienteService clienteService;
    private static IProdutoService produtoService;
    private static IEstoqueService estoqueService;
    private static Scanner scanner; // Scanner global para toda a aplicação

    public static void main(String[] args) {
        // Inicializa os Services com seus respectivos DAOs
        clienteService = new ClienteService(new ClienteDAO());
        produtoService = new ProdutoService(new ProdutoDAO());
        estoqueService = new EstoqueService(new EstoqueDAO());
        scanner = new Scanner(System.in); // Inicializa o scanner

        int opcao = -1;

        System.out.println("--- BEM-VINDO AO SISTEMA DE GERENCIAMENTO ---");

        while (opcao != 0) {
            exibirMenuPrincipal();
            try {
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha pendente

                switch (opcao) {
                    case 1:
                        menuClientes();
                        break;
                    case 2:
                        menuProdutos();
                        break;
                    case 3:
                        menuEstoque();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema... Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("ERRO: Entrada inválida. Por favor, insira um número para a opção.");
                scanner.nextLine(); // Limpar o buffer do scanner
                opcao = -1; // Resetar opção para continuar no loop
            } catch (DAOException | TipoChaveNaoEncontradaException e) {
                System.err.println("ERRO NO BANCO DE DADOS: " + e.getMessage());
                // e.printStackTrace(); // Descomente para debug detalhado
            } catch (Exception e) {
                System.err.println("ERRO INESPERADO: " + e.getMessage());
                // e.printStackTrace(); // Descomente para debug detalhado
            }
            if (opcao != 0) {
                pressioneEnterParaContinuar();
            }
        }
        scanner.close(); // Fecha o scanner ao sair do programa
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Produtos");
        System.out.println("3. Gerenciar Estoque");
        System.out.println("0. Sair do Sistema");
        System.out.println("----------------------");
    }

    // --- MÉTODOS PARA GERENCIAR CLIENTES ---
    private static void menuClientes() throws DAOException, TipoChaveNaoEncontradaException {
        int opcaoCliente = -1;
        while (opcaoCliente != 0) {
            System.out.println("\n--- GERENCIAR CLIENTES ---");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Consultar Cliente por CPF");
            System.out.println("3. Alterar Cliente");
            System.out.println("4. Excluir Cliente");
            System.out.println("5. Listar Todos os Clientes");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.println("---------------------------");
            System.out.print("Escolha uma opção para Clientes: ");

            try {
                opcaoCliente = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha

                switch (opcaoCliente) {
                    case 1:
                        cadastrarClienteInterativo();
                        break;
                    case 2:
                        consultarClienteInterativo();
                        break;
                    case 3:
                        alterarClienteInterativo();
                        break;
                    case 4:
                        excluirClienteInterativo();
                        break;
                    case 5:
                        listarTodosClientes();
                        break;
                    case 0:
                        System.out.println("Retornando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida para Clientes. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("ERRO: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
                opcaoCliente = -1; // Resetar
            }
            if (opcaoCliente != 0) {
                pressioneEnterParaContinuar();
            }
        }
    }

    private static void cadastrarClienteInterativo() throws DAOException, TipoChaveNaoEncontradaException {
        System.out.println("\n--- CADASTRAR NOVO CLIENTE ---");
        Cliente cliente = new Cliente();

        try {
            System.out.print("Nome completo: ");
            cliente.setNome(scanner.nextLine());

            System.out.print("CPF (somente números): ");
            cliente.setCpf(scanner.nextLong());
            scanner.nextLine(); // Consumir nova linha

            System.out.print("Telefone (somente números): ");
            cliente.setTel(scanner.nextLong());
            scanner.nextLine();

            System.out.print("Endereço (Rua, Av., etc.): ");
            cliente.setEnd(scanner.nextLine());

            System.out.print("Número do endereço: ");
            cliente.setNumero(scanner.nextInt());
            scanner.nextLine();

            System.out.print("Cidade: ");
            cliente.setCidade(scanner.nextLine());

            System.out.print("Estado (Sigla, ex: SP): ");
            cliente.setEstado(scanner.nextLine());

            System.out.print("Email: ");
            cliente.setEmail(scanner.nextLine());

            Boolean cadastrou = clienteService.cadastrar(cliente);
            if (cadastrou) {
                System.out.println("SUCESSO: Cliente '" + cliente.getNome() + "' cadastrado!");
                if (cliente.getId() != null) {
                    System.out.println("ID gerado para o cliente: " + cliente.getId());
                }
            } else {
                System.out.println("FALHA: Não foi possível cadastrar o cliente. Verifique se o CPF/Email já existe.");
            }
        } catch (InputMismatchException e) {
            System.err.println("ERRO: Entrada inválida. Certifique-se de que CPF, Telefone e Número são números válidos.");
            scanner.nextLine(); // Limpar buffer em caso de erro de tipo
        }
    }

    private static void consultarClienteInterativo() throws DAOException {
        System.out.println("\n--- CONSULTAR CLIENTE POR CPF ---");
        System.out.print("Digite o CPF do cliente a ser consultado (somente números): ");
        try {
            Long cpfConsulta = scanner.nextLong();
            scanner.nextLine(); // Consumir nova linha

            Cliente cliente = clienteService.consultar(cpfConsulta);
            if (cliente != null) {
                System.out.println("\n--- DETALHES DO CLIENTE ---");
                System.out.println("ID: " + cliente.getId());
                System.out.println("Nome: " + cliente.getNome());
                System.out.println("CPF: " + cliente.getCpf());
                System.out.println("Telefone: " + cliente.getTel());
                System.out.println("Endereço: " + cliente.getEnd() + ", " + cliente.getNumero());
                System.out.println("Cidade: " + cliente.getCidade() + " - " + cliente.getEstado());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("---------------------------");
            } else {
                System.out.println("Cliente com CPF " + cpfConsulta + " não encontrado.");
            }
        } catch (InputMismatchException e) {
            System.err.println("ERRO: CPF inválido. Por favor, digite somente números.");
            scanner.nextLine(); // Limpar buffer
        }
    }

    private static void alterarClienteInterativo() throws DAOException, TipoChaveNaoEncontradaException {
        System.out.println("\n--- ALTERAR CLIENTE ---");
        System.out.print("Digite o CPF do cliente a ser alterado (somente números): ");
        try {
            Long cpfAltera = scanner.nextLong();
            scanner.nextLine();

            Cliente clienteExistente = clienteService.consultar(cpfAltera);
            if (clienteExistente == null) {
                System.out.println("Cliente com CPF " + cpfAltera + " não encontrado. Não é possível alterar.");
                return;
            }

            System.out.println("Cliente encontrado: " + clienteExistente.getNome() + ". Deixe o campo em branco para não alterar.");

            System.out.print("Novo Nome (" + clienteExistente.getNome() + "): ");
            String novoNome = scanner.nextLine();
            if (!novoNome.trim().isEmpty()) clienteExistente.setNome(novoNome);

            System.out.print("Novo Telefone (" + clienteExistente.getTel() + "): ");
            String novoTelStr = scanner.nextLine();
            if (!novoTelStr.trim().isEmpty()) clienteExistente.setTel(Long.parseLong(novoTelStr));

            System.out.print("Novo Endereço (" + clienteExistente.getEnd() + "): ");
            String novoEnd = scanner.nextLine();
            if(!novoEnd.trim().isEmpty()) clienteExistente.setEnd(novoEnd);

            System.out.print("Novo Número (" + clienteExistente.getNumero() + "): ");
            String novoNumStr = scanner.nextLine();
            if(!novoNumStr.trim().isEmpty()) clienteExistente.setNumero(Integer.parseInt(novoNumStr));

            System.out.print("Nova Cidade (" + clienteExistente.getCidade() + "): ");
            String novaCidade = scanner.nextLine();
            if(!novaCidade.trim().isEmpty()) clienteExistente.setCidade(novaCidade);

            System.out.print("Novo Estado (" + clienteExistente.getEstado() + "): ");
            String novoEstado = scanner.nextLine();
            if(!novoEstado.trim().isEmpty()) clienteExistente.setEstado(novoEstado);

            System.out.print("Novo Email (" + clienteExistente.getEmail() + "): ");
            String novoEmail = scanner.nextLine();
            if (!novoEmail.trim().isEmpty()) clienteExistente.setEmail(novoEmail);

            Boolean alterou = clienteService.alterar(clienteExistente);
            if (alterou) {
                System.out.println("SUCESSO: Cliente alterado!");
            } else {
                System.out.println("FALHA: Não foi possível alterar o cliente.");
            }

        } catch (InputMismatchException e) {
            System.err.println("ERRO: CPF inválido ou tipo de dado incorreto para algum campo. Por favor, digite somente números onde esperado.");
            scanner.nextLine();
        } catch (NumberFormatException e) {
            System.err.println("ERRO: Erro ao converter número para Telefone ou Número do endereço. Verifique os valores digitados.");
        }
    }

    private static void excluirClienteInterativo() throws DAOException {
        System.out.println("\n--- EXCLUIR CLIENTE POR CPF ---");
        System.out.print("Digite o CPF do cliente a ser excluído (somente números): ");
        try {
            Long cpfExcluir = scanner.nextLong();
            scanner.nextLine();

            Cliente cliente = clienteService.consultar(cpfExcluir);
            if (cliente == null) {
                System.out.println("Cliente com CPF " + cpfExcluir + " não encontrado.");
                return;
            }

            System.out.println("Tem certeza que deseja excluir o cliente: " + cliente.getNome() + " (CPF: " + cliente.getCpf() + ")? (S/N)");
            String confirmacao = scanner.nextLine().trim().toUpperCase();

            if (confirmacao.equals("S")) {
                clienteService.excluir(cpfExcluir);
                System.out.println("SUCESSO: Cliente excluído (se não houver dependências, como vendas).");
            } else {
                System.out.println("Exclusão cancelada.");
            }

        } catch (InputMismatchException e) {
            System.err.println("ERRO: CPF inválido. Por favor, digite somente números.");
            scanner.nextLine();
        }
    }

    private static void listarTodosClientes() throws DAOException {
        System.out.println("\n--- LISTA DE TODOS OS CLIENTES ---");
        Collection<Cliente> clientes = clienteService.buscarTodos();
        if (clientes == null || clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.printf("%-15s | %-30s | %-15s | %-30s\n", "CPF", "Nome", "Telefone", "Email");
        System.out.println(String.join("", java.util.Collections.nCopies(95, "-")));
        for (Cliente cliente : clientes) {
            System.out.printf("%-15d | %-30s | %-15d | %-30s\n",
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getTel(),
                cliente.getEmail() != null ? cliente.getEmail() : "N/A"
            );
        }
        System.out.println(String.join("", java.util.Collections.nCopies(95, "-")));
    }

    // --- MÉTODOS PARA GERENCIAR PRODUTOS ---
    private static void menuProdutos() throws DAOException, TipoChaveNaoEncontradaException {
        int opcaoProduto = -1;
        while (opcaoProduto != 0) {
            System.out.println("\n--- GERENCIAR PRODUTOS ---");
            System.out.println("1. Cadastrar Novo Produto");
            System.out.println("2. Consultar Produto por Código");
            System.out.println("3. Alterar Produto");
            System.out.println("4. Excluir Produto");
            System.out.println("5. Listar Todos os Produtos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.println("---------------------------");
            System.out.print("Escolha uma opção para Produtos: ");

            try {
                opcaoProduto = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha

                switch (opcaoProduto) {
                    case 1:
                        cadastrarProdutoInterativo();
                        break;
                    case 2:
                        consultarProdutoInterativo();
                        break;
                    case 3:
                        alterarProdutoInterativo();
                        break;
                    case 4:
                        excluirProdutoInterativo();
                        break;
                    case 5:
                        listarTodosProdutos();
                        break;
                    case 0:
                        System.out.println("Retornando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida para Produtos. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("ERRO: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
                opcaoProduto = -1; // Resetar
            }
            if (opcaoProduto != 0) {
                pressioneEnterParaContinuar();
            }
        }
    }

    private static void cadastrarProdutoInterativo() throws DAOException, TipoChaveNaoEncontradaException {
        System.out.println("\n--- CADASTRAR NOVO PRODUTO ---");
        Produto produto = new Produto();

        try {
            System.out.print("Código do Produto: ");
            produto.setCodigo(scanner.nextLine());

            System.out.print("Nome do Produto: ");
            produto.setNome(scanner.nextLine());

            System.out.print("Descrição do Produto: ");
            produto.setDescricao(scanner.nextLine());

            System.out.print("Valor (ex: 123.45): ");
            produto.setValor(new BigDecimal(scanner.nextLine().replace(',', '.'))); // Aceita vírgula ou ponto

            System.out.print("Categoria: ");
            produto.setCategoria(scanner.nextLine());

            Boolean cadastrou = produtoService.cadastrar(produto);
            if (cadastrou) {
                System.out.println("SUCESSO: Produto '" + produto.getNome() + "' cadastrado!");
                if (produto.getId() != null) {
                    System.out.println("ID gerado para o produto: " + produto.getId());
                }
            } else {
                System.out.println("FALHA: Não foi possível cadastrar o produto. Verifique se o Código já existe.");
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.err.println("ERRO: Entrada inválida. Verifique o formato do Valor.");
            // scanner.nextLine(); // Já consumido pelo nextLine()
        }
    }

    private static void consultarProdutoInterativo() throws DAOException {
        System.out.println("\n--- CONSULTAR PRODUTO POR CÓDIGO ---");
        System.out.print("Digite o Código do produto a ser consultado: ");
        String codigoConsulta = scanner.nextLine();

        Produto produto = produtoService.consultar(codigoConsulta);
        if (produto != null) {
            System.out.println("\n--- DETALHES DO PRODUTO ---");
            System.out.println("ID: " + produto.getId());
            System.out.println("Código: " + produto.getCodigo());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Descrição: " + produto.getDescricao());
            System.out.println("Valor: " + produto.getValor());
            System.out.println("Categoria: " + produto.getCategoria());
            System.out.println("---------------------------");
        } else {
            System.out.println("Produto com código " + codigoConsulta + " não encontrado.");
        }
    }

    private static void alterarProdutoInterativo() throws DAOException, TipoChaveNaoEncontradaException {
        System.out.println("\n--- ALTERAR PRODUTO ---");
        System.out.print("Digite o Código do produto a ser alterado: ");
        String codigoAltera = scanner.nextLine();

        Produto produtoExistente = produtoService.consultar(codigoAltera);
        if (produtoExistente == null) {
            System.out.println("Produto com código " + codigoAltera + " não encontrado. Não é possível alterar.");
            return;
        }

        System.out.println("Produto encontrado: " + produtoExistente.getNome() + ". Deixe o campo em branco para não alterar.");

        System.out.print("Novo Nome (" + produtoExistente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) produtoExistente.setNome(novoNome);

        System.out.print("Nova Descrição (" + produtoExistente.getDescricao() + "): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.trim().isEmpty()) produtoExistente.setDescricao(novaDescricao);

        System.out.print("Novo Valor (" + produtoExistente.getValor() + "): ");
        String novoValorStr = scanner.nextLine();
        if (!novoValorStr.trim().isEmpty()) {
            try {
                produtoExistente.setValor(new BigDecimal(novoValorStr.replace(',', '.')));
            } catch (NumberFormatException e) {
                System.err.println("ERRO: Valor inválido. O valor não será alterado.");
            }
        }

        System.out.print("Nova Categoria (" + produtoExistente.getCategoria() + "): ");
        String novaCategoria = scanner.nextLine();
        if (!novaCategoria.trim().isEmpty()) produtoExistente.setCategoria(novaCategoria);

        Boolean alterou = produtoService.alterar(produtoExistente);
        if (alterou) {
            System.out.println("SUCESSO: Produto alterado!");
        } else {
            System.out.println("FALHA: Não foi possível alterar o produto.");
        }
    }

    private static void excluirProdutoInterativo() throws DAOException {
        System.out.println("\n--- EXCLUIR PRODUTO POR CÓDIGO ---");
        System.out.print("Digite o Código do produto a ser excluído: ");
        String codigoExcluir = scanner.nextLine();

        Produto produto = produtoService.consultar(codigoExcluir);
        if (produto == null) {
            System.out.println("Produto com código " + codigoExcluir + " não encontrado.");
            return;
        }

        System.out.println("Tem certeza que deseja excluir o produto: " + produto.getNome() + " (Código: " + produto.getCodigo() + ")? (S/N)");
        String confirmacao = scanner.nextLine().trim().toUpperCase();

        if (confirmacao.equals("S")) {
            produtoService.excluir(codigoExcluir);
            System.out.println("SUCESSO: Produto excluído (se não houver dependências, como estoque ou vendas).");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void listarTodosProdutos() throws DAOException {
        System.out.println("\n--- LISTA DE TODOS OS PRODUTOS ---");
        Collection<Produto> produtos = produtoService.buscarTodos();
        if (produtos == null || produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.printf("%-15s | %-30s | %-15s | %-20s\n", "Código", "Nome", "Valor", "Categoria");
        System.out.println(String.join("", java.util.Collections.nCopies(85, "-")));
        for (Produto produto : produtos) {
            System.out.printf("%-15s | %-30s | %-15.2f | %-20s\n",
                produto.getCodigo(),
                produto.getNome(),
                produto.getValor(),
                produto.getCategoria() != null ? produto.getCategoria() : "N/A"
            );
        }
        System.out.println(String.join("", java.util.Collections.nCopies(85, "-")));
    }

    // --- MÉTODOS PARA GERENCIAR ESTOQUE ---
    private static void menuEstoque() throws DAOException, TipoChaveNaoEncontradaException {
        int opcaoEstoque = -1;
        while (opcaoEstoque != 0) {
            System.out.println("\n--- GERENCIAR ESTOQUE ---");
            System.out.println("1. Cadastrar Nova Entrada de Estoque");
            System.out.println("2. Consultar Estoque por ID");
            System.out.println("3. Alterar Estoque (Quantidade/Localização)");
            System.out.println("4. Excluir Entrada de Estoque");
            System.out.println("5. Listar Todas as Entradas de Estoque");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.println("---------------------------");
            System.out.print("Escolha uma opção para Estoque: ");

            try {
                opcaoEstoque = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha

                switch (opcaoEstoque) {
                    case 1:
                        cadastrarEstoqueInterativo();
                        break;
                    case 2:
                        consultarEstoqueInterativo();
                        break;
                    case 3:
                        alterarEstoqueInterativo();
                        break;
                    case 4:
                        excluirEstoqueInterativo();
                        break;
                    case 5:
                        listarTodosEstoques();
                        break;
                    case 0:
                        System.out.println("Retornando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida para Estoque. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("ERRO: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
                opcaoEstoque = -1; // Resetar
            }
            if (opcaoEstoque != 0) {
                pressioneEnterParaContinuar();
            }
        }
    }

    private static void cadastrarEstoqueInterativo() throws DAOException, TipoChaveNaoEncontradaException {
        System.out.println("\n--- CADASTRAR NOVA ENTRADA DE ESTOQUE ---");
        System.out.print("Digite o Código do Produto para esta entrada de estoque: ");
        String codigoProduto = scanner.nextLine();

        Produto produto = produtoService.consultar(codigoProduto);
        if (produto == null) {
            System.out.println("Produto com código " + codigoProduto + " não encontrado. Cadastre o produto primeiro.");
            return;
        }

        Estoque estoque = new Estoque();
        estoque.setProduto(produto); // Associa o produto encontrado

        try {
            System.out.print("Quantidade em estoque: ");
            estoque.setQuantidade(scanner.nextInt());
            scanner.nextLine();

            System.out.print("Localização (ex: Armazém A, Corredor 3): ");
            estoque.setLocalizacao(scanner.nextLine());

            estoque.setDataUltimaAtualizacao(Instant.now()); // Define a data/hora atual

            Boolean cadastrou = estoqueService.cadastrar(estoque);
            if (cadastrou) {
                System.out.println("SUCESSO: Entrada de estoque para '" + produto.getNome() + "' cadastrada com ID: " + estoque.getId());
            } else {
                System.out.println("FALHA: Não foi possível cadastrar a entrada de estoque.");
            }
        } catch (InputMismatchException e) {
            System.err.println("ERRO: Entrada inválida. Certifique-se de que a Quantidade é um número válido.");
            scanner.nextLine(); // Limpar buffer
        }
    }

    private static void consultarEstoqueInterativo() throws DAOException {
        System.out.println("\n--- CONSULTAR ESTOQUE POR ID ---");
        System.out.print("Digite o ID da entrada de estoque a ser consultada: ");
        try {
            Long idEstoque = scanner.nextLong();
            scanner.nextLine();

            Estoque estoque = estoqueService.consultar(idEstoque);
            if (estoque != null) {
                System.out.println("\n--- DETALHES DA ENTRADA DE ESTOQUE ---");
                System.out.println("ID da Entrada: " + estoque.getId());
                System.out.println("Produto Associado: " + estoque.getProduto().getNome() + " (Código: " + estoque.getProduto().getCodigo() + ")");
                System.out.println("Quantidade: " + estoque.getQuantidade());
                System.out.println("Localização: " + estoque.getLocalizacao());
                System.out.println("Última Atualização: " + estoque.getDataUltimaAtualizacao());
                System.out.println("---------------------------------------");
            } else {
                System.out.println("Entrada de estoque com ID " + idEstoque + " não encontrada.");
            }
        } catch (InputMismatchException e) {
            System.err.println("ERRO: ID inválido. Por favor, digite somente números.");
            scanner.nextLine();
        }
    }

    private static void alterarEstoqueInterativo() throws DAOException, TipoChaveNaoEncontradaException {
        System.out.println("\n--- ALTERAR ENTRADA DE ESTOQUE ---");
        System.out.print("Digite o ID da entrada de estoque a ser alterada: ");
        try {
            Long idEstoqueAltera = scanner.nextLong();
            scanner.nextLine();

            Estoque estoqueExistente = estoqueService.consultar(idEstoqueAltera);
            if (estoqueExistente == null) {
                System.out.println("Entrada de estoque com ID " + idEstoqueAltera + " não encontrada. Não é possível alterar.");
                return;
            }

            System.out.println("Entrada de estoque encontrada para o produto: " + estoqueExistente.getProduto().getNome());
            System.out.println("Deixe o campo em branco para não alterar.");

            System.out.print("Nova Quantidade (" + estoqueExistente.getQuantidade() + "): ");
            String novaQtdStr = scanner.nextLine();
            if (!novaQtdStr.trim().isEmpty()) {
                try {
                    estoqueExistente.setQuantidade(Integer.parseInt(novaQtdStr));
                } catch (NumberFormatException e) {
                    System.err.println("ERRO: Quantidade inválida. A quantidade não será alterada.");
                }
            }

            System.out.print("Nova Localização (" + estoqueExistente.getLocalizacao() + "): ");
            String novaLocalizacao = scanner.nextLine();
            if (!novaLocalizacao.trim().isEmpty()) estoqueExistente.setLocalizacao(novaLocalizacao);

            estoqueExistente.setDataUltimaAtualizacao(Instant.now()); // Atualiza a data de modificação

            Boolean alterou = estoqueService.alterar(estoqueExistente);
            if (alterou) {
                System.out.println("SUCESSO: Entrada de estoque alterada!");
            } else {
                System.out.println("FALHA: Não foi possível alterar a entrada de estoque.");
            }

        } catch (InputMismatchException e) {
            System.err.println("ERRO: ID inválido ou tipo de dado incorreto para algum campo. Por favor, digite somente números onde esperado.");
            scanner.nextLine();
        }
    }

    private static void excluirEstoqueInterativo() throws DAOException {
        System.out.println("\n--- EXCLUIR ENTRADA DE ESTOQUE POR ID ---");
        System.out.print("Digite o ID da entrada de estoque a ser excluída: ");
        try {
            Long idEstoqueExcluir = scanner.nextLong();
            scanner.nextLine();

            Estoque estoque = estoqueService.consultar(idEstoqueExcluir);
            if (estoque == null) {
                System.out.println("Entrada de estoque com ID " + idEstoqueExcluir + " não encontrada.");
                return;
            }

            System.out.println("Tem certeza que deseja excluir a entrada de estoque para o produto: " + estoque.getProduto().getNome() + " (ID: " + estoque.getId() + ")? (S/N)");
            String confirmacao = scanner.nextLine().trim().toUpperCase();

            if (confirmacao.equals("S")) {
                estoqueService.excluir(idEstoqueExcluir);
                System.out.println("SUCESSO: Entrada de estoque excluída.");
            } else {
                System.out.println("Exclusão cancelada.");
            }

        } catch (InputMismatchException e) {
            System.err.println("ERRO: ID inválido. Por favor, digite somente números.");
            scanner.nextLine();
        }
    }

    private static void listarTodosEstoques() throws DAOException {
        System.out.println("\n--- LISTA DE TODAS AS ENTRADAS DE ESTOQUE ---");
        Collection<Estoque> estoques = estoqueService.buscarTodos();
        if (estoques == null || estoques.isEmpty()) {
            System.out.println("Nenhuma entrada de estoque cadastrada.");
            return;
        }
        System.out.printf("%-10s | %-30s | %-15s | %-30s\n", "ID", "Produto", "Quantidade", "Localização");
        System.out.println(String.join("", java.util.Collections.nCopies(90, "-")));
        for (Estoque estoque : estoques) {
            System.out.printf("%-10d | %-30s | %-15d | %-30s\n",
                estoque.getId(),
                estoque.getProduto().getNome(),
                estoque.getQuantidade(),
                estoque.getLocalizacao() != null ? estoque.getLocalizacao() : "N/A"
            );
        }
        System.out.println(String.join("", java.util.Collections.nCopies(90, "-")));
    }

    // --- MÉTODO AUXILIAR ---
    private static void pressioneEnterParaContinuar() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine(); // Espera que o usuário pressione Enter
    }
}
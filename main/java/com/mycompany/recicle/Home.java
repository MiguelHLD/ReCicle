
package com.mycompany.recicle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Home extends JFrame {

    // CORES DO RECICLE
    private final Color green = new Color(35, 110, 75);
    private final Color lightGreen = new Color(239, 247, 241);
    private final Color textGray = new Color(100, 110, 104);
    private final Color borderGreen = new Color(215, 228, 218);

    public Home() {

        // CONFIGURAÇÕES DA JANELA
        setTitle("ReCicle");

        // ÍCONE DA JANELA
        Image icone = carregarIcone();

        if (icone != null) {
            setIconImage(icone);
        }

        setSize(900, 650);
        setMinimumSize(new Dimension(750, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        // =========================================
        // CABEÇALHO
        // =========================================

        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(green);
        header.setBorder(
                BorderFactory.createEmptyBorder(10, 35, 10, 35)
        );
        header.setPreferredSize(new Dimension(900, 115));

        // LOGOTIPO
        JLabel logo = criarLogo();

        // NOME RECICLE AO LADO DA LOGOTIPO
        JLabel nomeRecicle = new JLabel("ReCicle");
        nomeRecicle.setFont(
                new Font("Arial", Font.BOLD, 38)
        );
        nomeRecicle.setForeground(Color.WHITE);

        // AGRUPAR LOGOTIPO E NOME
        JPanel marca = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 18, 0)
        );
        marca.setOpaque(false);
        marca.add(logo);
        marca.add(nomeRecicle);

        // TEXTO À DIREITA
        JLabel headerText = new JLabel(
                "MARKETPLACE SUSTENTÁVEL"
        );
        headerText.setFont(
                new Font("Arial", Font.BOLD, 11)
        );
        headerText.setForeground(new Color(220, 239, 226));

        header.add(marca, BorderLayout.WEST);
        header.add(headerText, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // =========================================
        // CONTEÚDO PRINCIPAL
        // =========================================

        JPanel content = new JPanel();
        content.setBackground(lightGreen);
        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS)
        );
        content.setBorder(
                BorderFactory.createEmptyBorder(35, 45, 25, 45)
        );

        JLabel welcome = new JLabel(
                "Tecnologia ganha uma nova vida."
        );
        welcome.setFont(
                new Font("Arial", Font.BOLD, 27)
        );
        welcome.setForeground(new Color(35, 75, 52));
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Encontre, anuncie e reutilize celulares.<br>"
                + "Faça parte de um consumo mais consciente.</html>"
        );
        description.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );
        description.setForeground(textGray);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(welcome);
        content.add(Box.createVerticalStrut(10));
        content.add(description);
        content.add(Box.createVerticalStrut(30));

        // =========================================
        // TÍTULO DA SEÇÃO
        // =========================================

        JLabel sectionTitle = new JLabel(
                "O que você deseja fazer?"
        );
        sectionTitle.setFont(
                new Font("Arial", Font.BOLD, 17)
        );
        sectionTitle.setForeground(new Color(45, 65, 52));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(15));

        // =========================================
        // CARTÕES
        // =========================================

        JPanel cards = new JPanel(
                new GridLayout(1, 2, 20, 0)
        );
        cards.setOpaque(false);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 220)
        );

        // CARTÃO: EXPLORAR CELULARES
        JPanel listingsCard = createCard(
                "",
                "Explorar celulares",
                "<html>Confira aparelhos anunciados<br>"
                + "para venda, doação ou troca.</html>",
                "Ver celulares anunciados"
        );
        
        adicionarImagemCelular(listingsCard);
        
        // CARTÃO: ANUNCIAR CELULAR
        JPanel registerCard = createCard(
                "+",
                "Anunciar celular",
                "<html>Cadastre um aparelho e ajude<br>"
                + "a dar uma nova vida à tecnologia.</html>",
                "Cadastrar celular"
        );

        JButton listingsButton =
                (JButton) listingsCard.getClientProperty(
                        "actionButton"
                );

        JButton registerButton =
                (JButton) registerCard.getClientProperty(
                        "actionButton"
                );

        // ABRIR ANÚNCIOS
        listingsButton.addActionListener(e -> {
            setVisible(false);
            new AnnouncedPhones(this).setVisible(true);
        });

        // ABRIR CADASTRO
        registerButton.addActionListener(e -> {
            setVisible(false);
            new RegisterPhone(this).setVisible(true);
        });

        cards.add(listingsCard);
        cards.add(registerCard);

        content.add(cards);
        content.add(Box.createVerticalGlue());

        // =========================================
        // RODAPÉ
        // =========================================

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1, 0, 0, 0,
                                new Color(225, 232, 226)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 35, 15, 35
                        )
                )
        );

        JLabel footerText = new JLabel(
                "♻  Reutilizar hoje para preservar o amanhã."
        );
        footerText.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );
        footerText.setForeground(textGray);

        JButton exitButton = new JButton("Sair");
        styleButton(exitButton);
        exitButton.setPreferredSize(
                new Dimension(100, 36)
        );

        exitButton.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair do ReCicle?",
                    "Confirmar saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (resposta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        footer.add(footerText, BorderLayout.WEST);
        footer.add(exitButton, BorderLayout.EAST);

        // MONTAGEM DA TELA
        JPanel center = new JPanel(new BorderLayout());
        center.add(content, BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);

        setContentPane(main);
    }

    // =========================================
    // CARREGAR ÍCONE DA JANELA
    // =========================================

    private Image carregarIcone() {
    java.net.URL recurso = getClass().getResource("/images/icon.png");

    if (recurso != null) {
        return new ImageIcon(recurso).getImage();
    }

    System.err.println("Ícone não encontrado: /images/icon.png");
    return null;
}

    // =========================================
    // CARREGAR LOGOTIPO DO CABEÇALHO
    // =========================================

    
private JLabel criarLogo() {
    java.net.URL recurso = getClass().getResource("/images/logotipo.png");

    if (recurso != null) {
        ImageIcon originalLogo = new ImageIcon(recurso);

        Image imagem = originalLogo.getImage().getScaledInstance(
                100, 100, Image.SCALE_SMOOTH
        );

        return new JLabel(new ImageIcon(imagem));
    }

    System.err.println("Logotipo não encontrado: /images/logotipo.png");

    JLabel logoTexto = new JLabel("♻");
    logoTexto.setFont(new Font("Arial", Font.BOLD, 38));
    logoTexto.setForeground(Color.WHITE);

    return logoTexto;
}

    // =========================================
    // CRIAR CARTÕES
    // =========================================

    private JPanel createCard(
            String symbol,
            String titleText,
            String descriptionText,
            String buttonText) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                borderGreen, 1, true
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        JLabel icon = new JLabel(symbol);
        icon.setFont(
                new Font("Arial", Font.BOLD, 30)
        );
        icon.setForeground(green);
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(titleText);
        title.setFont(
                new Font("Arial", Font.BOLD, 18)
        );
        title.setForeground(green);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(descriptionText);
        description.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );
        description.setForeground(textGray);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton button = new JButton(buttonText);
        styleButton(button);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(
                new Dimension(280, 42)
        );

        card.add(icon);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(description);
        card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(12));
        card.add(button);

        card.putClientProperty("actionButton", button);

        return card;
    }

    // =========================================
    // ESTILIZAR BOTÕES
    // =========================================

    private void styleButton(JButton button) {

        button.setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        button.setPreferredSize(
                new Dimension(250, 42)
        );

        button.setBackground(Color.WHITE);
        button.setForeground(green);

        button.setFocusPainted(false);
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(green, 1, true),
                        BorderFactory.createEmptyBorder(
                                8, 14, 8, 14
                        )
                )
        );

        // EFEITO AO PASSAR O MOUSE
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(green);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(green);
            }
        });
    }

private void adicionarImagemCelular(JPanel card) {
    java.net.URL recurso = getClass().getResource("/images/celular.png");

    if (recurso != null) {
        ImageIcon original = new ImageIcon(recurso);

        Image imagem = original.getImage().getScaledInstance(
                55, 55, Image.SCALE_SMOOTH
        );

        JLabel imagemCelular = new JLabel(new ImageIcon(imagem));
        imagemCelular.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(imagemCelular, 0);
    } else {
        System.err.println(
                "Imagem do celular não encontrada: /images/celular.png"
        );
    }
}

    // =========================================
    // INICIAR PROGRAMA
    // =========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Database.initialize();
            new Home().setVisible(true);
        });
    }
}

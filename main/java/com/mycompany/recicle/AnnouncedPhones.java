
package com.mycompany.recicle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.File;

public class AnnouncedPhones extends JFrame {

    private final JFrame home;

    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JPanel cardsPanel;
    private JLabel countLabel;

    private final Color green = new Color(35, 110, 75);
    private final Color background = new Color(35, 110, 75);
    private final Color lightBackground = new Color(245, 248, 246);

    public AnnouncedPhones() {
        this(null);
    }

    public AnnouncedPhones(JFrame home) {
        this.home = home;

        setTitle("ReCicle - Celulares anunciados");

        Image icone = carregarIcone();
        if (icone != null) {
            setIconImage(icone);
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setMinimumSize(new Dimension(750, 600));
        setLocationRelativeTo(home);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(lightBackground);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(22, 25, 22, 25)
        );

        // Cabeçalho
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Celulares anunciados");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(green);

        JLabel subtitle = new JLabel(
                "Encontre celulares para comprar, receber ou trocar."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 110, 104));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(subtitle);

        JButton backButton = new JButton("Voltar");
        styleButton(backButton, false);

        backButton.addActionListener(e -> {
            dispose();

            if (home != null) {
                home.setVisible(true);
                home.toFront();
            } else {
                new Home().setVisible(true);
            }
        });

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(backButton, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // Pesquisa e filtros
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 228, 222), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 38));
        searchField.setToolTipText("Pesquise por marca ou modelo");

        JButton searchButton = new JButton("Pesquisar");
        styleButton(searchButton, true);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JPanel filterPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 0)
        );
        filterPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Modalidade:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 13));

        filterComboBox = new JComboBox<>(new String[]{
            "Todas", "Venda", "Doação", "Troca"
        });
        filterComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterComboBox.setPreferredSize(new Dimension(150, 36));

        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        // Cartões de anúncios
        cardsPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        cardsPanel.setBackground(lightBackground);
        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 2, 15, 2)
        );

        JScrollPane scrollPane = new JScrollPane(
                cardsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(lightBackground);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        countLabel = new JLabel("Carregando anúncios...");
        countLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        countLabel.setForeground(new Color(100, 110, 104));
        centerPanel.add(countLabel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        searchButton.addActionListener(e -> loadPhones());
        searchField.addActionListener(e -> loadPhones());
        filterComboBox.addActionListener(e -> loadPhones());

        loadPhones();
    }

    // Carrega os anúncios do banco
    private void loadPhones() {
        cardsPanel.removeAll();
        cardsPanel.setLayout(new GridLayout(0, 2, 15, 15));

        String search = searchField.getText().trim();
        String filter = (String) filterComboBox.getSelectedItem();

        String sql = """
            SELECT id, brand, model, description, price,
                   condition, listing_type
            FROM phones
            WHERE (brand LIKE ? OR model LIKE ?)
            AND (? = 'Todas' OR listing_type = ?)
            ORDER BY id DESC
            """;

        int total = 0;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            statement.setString(3, filter);
            statement.setString(4, filter);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    total++;

                    int id = rs.getInt("id");

                    JPanel card = createPhoneCard(
                            id,
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("condition"),
                            rs.getString("listing_type")
                    );

                    cardsPanel.add(card);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar os anúncios:\n"
                            + e.getMessage(),
                    "Erro no banco de dados",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        if (total == 0) {
            cardsPanel.setLayout(new BorderLayout());

            JLabel emptyLabel = new JLabel(
                    "Nenhum celular encontrado.",
                    SwingConstants.CENTER
            );
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(100, 110, 104));

            cardsPanel.add(emptyLabel, BorderLayout.CENTER);
        }

        countLabel.setText(
                total + (total == 1
                        ? " anúncio encontrado"
                        : " anúncios encontrados")
        );

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // Cria o cartão de cada celular
    private JPanel createPhoneCard(
            int id,
            String brand,
            String model,
            String description,
            double price,
            String condition,
            String listingType) {

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 228, 222), 1, true),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        // Cabeçalho com nome e botão X
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setOpaque(false);

        JLabel phoneName = new JLabel(
                safe(brand) + " " + safe(model)
        );
        phoneName.setFont(new Font("Arial", Font.BOLD, 18));
        phoneName.setForeground(green);

        JPanel headerRight = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 6, 0)
        );
        headerRight.setOpaque(false);

        JLabel typeLabel = new JLabel(safe(listingType));
        typeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        typeLabel.setForeground(green);
        typeLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(190, 220, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton deleteButton = new JButton("X");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 13));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(190, 45, 45));
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setToolTipText("Excluir este anúncio");
        deleteButton.setPreferredSize(new Dimension(45, 40));

        deleteButton.addActionListener(e -> deletePhone(id));

        headerRight.add(typeLabel);
        headerRight.add(deleteButton);

        header.add(phoneName, BorderLayout.CENTER);
        header.add(headerRight, BorderLayout.EAST);

        // Detalhes
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        JLabel descriptionTitle = new JLabel("Descrição");
        descriptionTitle.setFont(new Font("Arial", Font.BOLD, 12));

        JTextArea descriptionArea = new JTextArea(
                description == null || description.isBlank()
                        ? "Nenhuma descrição informada."
                        : description
        );
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setRows(3);

        JLabel conditionLabel = new JLabel(
                "<html><b>Defeito:</b> "
                        + escapeHtml(safe(condition))
                        + "</html>"
        );
        conditionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        details.add(descriptionTitle);
        details.add(Box.createVerticalStrut(6));
        details.add(descriptionArea);
        details.add(Box.createVerticalStrut(10));
        details.add(conditionLabel);

        JLabel priceLabel;

        if ("Doação".equals(listingType)) {
            priceLabel = new JLabel("Gratuito");
        } else if ("Troca".equals(listingType)) {
            priceLabel = new JLabel("Aceita troca");
        } else {
            NumberFormat currency = NumberFormat.getCurrencyInstance(
                    new Locale("pt", "BR")
            );
            priceLabel = new JLabel(currency.format(price));
        }

        priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priceLabel.setForeground(green);

        card.add(header, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(priceLabel, BorderLayout.SOUTH);

        return card;
    }

    // Confirma e exclui somente o anúncio selecionado
    private void deletePhone(int id) {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza de que deseja excluir este anúncio?\n\n"
                        + "Essa ação não poderá ser desfeita.",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM phones WHERE id = ?";

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int excluidos = statement.executeUpdate();

            if (excluidos > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Anúncio excluído com sucesso!",
                        "Exclusão concluída",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadPhones();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Esse anúncio não foi encontrado no banco de dados.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );

                loadPhones();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível excluir o anúncio:\n"
                            + e.getMessage(),
                    "Erro no banco de dados",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private void styleButton(JButton button, boolean primary) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(120, 38));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (primary) {
            button.setBackground(green);
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(green);
            button.setBorder(new LineBorder(green, 1));
        }
    }

    private Image carregarIcone() {
    java.net.URL recurso = getClass().getResource("/images/icon.png");

    if (recurso != null) {
        return new ImageIcon(recurso).getImage();
    }

    System.err.println("Ícone não encontrado: /images/icon.png");
    return null;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new AnnouncedPhones().setVisible(true)
        );
    }
}


package com.mycompany.recicle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.Image;
import java.io.File;

public class RegisterPhone extends JFrame {
    
    private JComboBox<String> brandComboBox;
    private JComboBox<String> modelComboBox;
    private JTextField otherBrandField;
    private JTextField otherModelField;
    private JTextField priceField;
    private JTextArea descriptionArea;
    private JComboBox<String> conditionComboBox;
    
    // Modalidade do anúncio
private JComboBox<String> listingTypeComboBox;

// Campo para explicar outro defeito
private JTextArea otherDefectArea;
private JScrollPane otherDefectScroll;

    private JPanel formPanel;
    private JScrollPane descriptionScroll;

    private final JFrame home;

    private final Color green = new Color(35, 110, 75);
    private final Color background = new Color(35, 110, 75);

    public RegisterPhone() {
        this(null);
    }

    public RegisterPhone(JFrame home) {
        this.home = home;

        setTitle("ReCicle - Cadastrar Celular");
        
        Image icone = carregarIcone();

         if (icone != null) {
         setIconImage(icone);
}
         
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 750);
        setMinimumSize(new Dimension(700, 650));
        setLocationRelativeTo(home);

        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(background);
        backgroundPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JPanel card = new JPanel(new BorderLayout(0, 20));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(680, 620));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(215, 223, 218), 1, true),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        // Cabeçalho
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Cadastrar celular");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(green);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Anuncie seu celular no ReCicle"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 110, 104));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // Marca e modelo
        brandComboBox = new JComboBox<>(new String[]{
            "Apple", "Samsung", "Motorola", "Xiaomi",
            "Asus", "Sony", "Outro"
        });

        modelComboBox = new JComboBox<>();
        otherBrandField = new JTextField();
        otherModelField = new JTextField();

        // Descrição
descriptionArea = new JTextArea(3, 20);
descriptionArea.setLineWrap(true);
descriptionArea.setWrapStyleWord(true);
descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));

descriptionScroll = new JScrollPane(
        descriptionArea,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
);
descriptionScroll.setPreferredSize(new Dimension(250, 85));

        // Campo de preço vazio, com exemplo visual 0,00.
        priceField = new JTextField();
        priceField.setHorizontalAlignment(JTextField.RIGHT);
        priceField.setToolTipText("Digite o preço");

        configurePriceField();

        // Defeito principal
        conditionComboBox = new JComboBox<>(new String[]{
            "Não liga",
            "Tela quebrada",
            "Problema na bateria",
            "Dano por água",
            "Outro defeito"
        });

        
        // Modalidade do anúncio
        listingTypeComboBox = new JComboBox<>(new String[]{
            "Venda", "Doação", "Troca"
        });

        // Campo para explicar outro defeito
otherDefectArea = new JTextArea(3, 20);
otherDefectArea.setLineWrap(true);
otherDefectArea.setWrapStyleWord(true);
otherDefectArea.setFont(new Font("Arial", Font.PLAIN, 14));
otherDefectArea.setToolTipText("Descreva o defeito do celular");

otherDefectScroll = new JScrollPane(
        otherDefectArea,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
);
otherDefectScroll.setPreferredSize(new Dimension(250, 85));
        
        // Estilo dos campos
        styleField(brandComboBox);
        styleField(modelComboBox);
        styleField(otherBrandField);
        styleField(otherModelField);
        styleField(priceField);
        styleField(conditionComboBox);
        styleField(listingTypeComboBox);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        updateModelOptions();
        rebuildForm();

        brandComboBox.addActionListener(e -> {
            updateModelOptions();
            rebuildForm();
        });

        modelComboBox.addActionListener(e -> rebuildForm());

        
listingTypeComboBox.addActionListener(e -> rebuildForm());

conditionComboBox.addActionListener(e -> rebuildForm());

        
        
JScrollPane formScroll = new JScrollPane(
        formPanel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
);

formScroll.setBorder(null);
formScroll.getVerticalScrollBar().setUnitIncrement(16);

card.add(formScroll, BorderLayout.CENTER);


        // Botões
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        buttonPanel.setOpaque(false);

        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Salvar celular");

        styleButton(cancelButton, false);
        styleButton(saveButton, true);

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(card, new GridBagConstraints());
        add(backgroundPanel);

        cancelButton.addActionListener(e -> {
            dispose();

            if (home != null) {
                home.setVisible(true);
                home.toFront();
            } else {
                new Home().setVisible(true);
            }
        });

        saveButton.addActionListener(e -> savePhone());
    }

    // Campo monetário: os dois últimos dígitos são os centavos.
    private void configurePriceField() {
        ((AbstractDocument) priceField.getDocument())
                .setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String text,
                    AttributeSet attrs)
                    throws BadLocationException {

                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void replace(
                    FilterBypass fb,
                    int offset,
                    int length,
                    String text,
                    AttributeSet attrs)
                    throws BadLocationException {

                String current = fb.getDocument().getText(
                        0, fb.getDocument().getLength());

                String inserted = text == null ? "" : text;

                // Aceita somente números.
                if (!inserted.matches("[0-9]*")) {
                    return;
                }

                // Trabalha com a sequência de dígitos, sem pontuação.
                String digits = current.replaceAll("[^0-9]", "");

                int safeOffset = Math.min(offset, current.length());
                int safeEnd = Math.min(offset + length, current.length());

                int start = current.substring(0, safeOffset)
                        .replaceAll("[^0-9]", "").length();

                int end = current.substring(0, safeEnd)
                        .replaceAll("[^0-9]", "").length();

                // Reconstrói os dígitos antes e depois da edição.
                String updated = digits.substring(0, start)
                        + inserted
                        + digits.substring(end);

                // Limite de 12 dígitos.
                if (updated.length() > 12) {
                    return;
                }

                // Permite apagar tudo.
                if (updated.isEmpty()) {
                    fb.replace(
                            0,
                            fb.getDocument().getLength(),
                            "",
                            attrs
                    );
                    priceField.repaint();
                    return;
                }

                // Remove zeros desnecessários à esquerda.
                updated = updated.replaceFirst("^0+(?!$)", "");

                // Garante dois dígitos para os centavos.
                while (updated.length() < 3) {
                    updated = "0" + updated;
                }

                String reais = updated.substring(
                        0, updated.length() - 2);

                String centavos = updated.substring(
                        updated.length() - 2);

                // Adiciona separador de milhar.
                reais = reais.replaceAll(
                        "\\B(?=(\\d{3})+(?!\\d))", ".");

                String formatted = reais + "," + centavos;

                // Atualiza o conteúdo completo.
                fb.replace(
                        0,
                        fb.getDocument().getLength(),
                        formatted,
                        attrs
                );

                priceField.repaint();
            }
        });

        // Exibe 0,00 em cinza apenas como exemplo.
        // O valor real do campo continua vazio.
        priceField.setUI(
                new javax.swing.plaf.basic.BasicTextFieldUI() {

            @Override
            protected void paintSafely(Graphics graphics) {
                super.paintSafely(graphics);

                if (priceField.getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) graphics.create();

                    g2.setColor(Color.GRAY);
                    g2.setFont(priceField.getFont());

                    Insets insets = priceField.getInsets();
                    FontMetrics metrics = g2.getFontMetrics();

                    int x = priceField.getWidth()
                            - insets.right
                            - metrics.stringWidth("0,00") - 5;

                    int y = (priceField.getHeight()
                            - metrics.getHeight()) / 2
                            + metrics.getAscent();

                    g2.drawString("0,00", x, y);
                    g2.dispose();
                }
            }
        });

        priceField.setText("");
    }

    private void updateModelOptions() {
        String brand = (String) brandComboBox.getSelectedItem();

        String[] models;

        if (brand == null || "Outro".equals(brand)) {
            models = new String[]{"Outro"};
        } else {
            switch (brand) {
                case "Apple":
                    models = new String[]{
                        "iPhone 18 Pro Max", "iPhone 18 Pro", "iPhone 18",
                        "iPhone 17 Pro Max", "iPhone 17 Pro", "iPhone 17",
                        "iPhone 16 Pro Max", "iPhone 16 Pro", "iPhone 16",
                        "iPhone 15 Pro Max", "iPhone 15 Pro", "iPhone 15",
                        "iPhone 14", "iPhone 13", "iPhone 12",
                        "iPhone 11", "Outro"
                    };
                    break;

                case "Samsung":
                    models = new String[]{
                        "Galaxy S26 Ultra", "Galaxy S26+", "Galaxy S26",
                        "Galaxy S25 Ultra", "Galaxy S25+", "Galaxy S25",
                        "Galaxy S24 Ultra", "Galaxy S24", "Galaxy S23",
                        "Galaxy A56", "Galaxy A55", "Galaxy A36",
                        "Galaxy A35", "Galaxy A16", "Galaxy Z Fold",
                        "Galaxy Z Flip", "Outro"
                    };
                    break;

                case "Motorola":
                    models = new String[]{
                        "Motorola Edge 60", "Motorola Edge 50",
                        "Moto G86", "Moto G75", "Moto G55",
                        "Moto G35", "Moto G24", "Motorola Razr",
                        "Outro"
                    };
                    break;

                case "Xiaomi":
                    models = new String[]{
                        "Xiaomi 17", "Xiaomi 15", "Xiaomi 14",
                        "Redmi Note 15", "Redmi Note 14",
                        "Redmi Note 13", "Redmi 14C",
                        "POCO X7", "POCO F7", "Outro"
                    };
                    break;

                case "Asus":
                    models = new String[]{
                        "ROG Phone 9", "ROG Phone 8",
                        "Zenfone 12 Ultra", "Zenfone 11 Ultra",
                        "Zenfone 10", "Outro"
                    };
                    break;

                case "Sony":
                    models = new String[]{
                        "Xperia 1 VII", "Xperia 1 VI",
                        "Xperia 5 V", "Xperia 10 VI",
                        "Xperia 10 V", "Outro"
                    };
                    break;

                default:
                    models = new String[]{"Outro"};
                    break;
            }
        }

        modelComboBox.setModel(new DefaultComboBoxModel<>(models));
        otherBrandField.setText("");
        otherModelField.setText("");
    }

    private void rebuildForm() {
        
    formPanel.removeAll();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(7, 5, 7, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    int row = 0;

    addRow("Marca:", brandComboBox, row++, gbc);

    if ("Outro".equals(brandComboBox.getSelectedItem())) {
        addRow("Digite a marca:", otherBrandField, row++, gbc);
    }

    addRow("Modelo:", modelComboBox, row++, gbc);

    if ("Outro".equals(modelComboBox.getSelectedItem())) {
        addRow("Digite o modelo:", otherModelField, row++, gbc);
    }

    addRow("Descrição:", descriptionScroll, row++, gbc);

    // Modalidade sempre aparece antes do preço.
    addRow("Modalidade:", listingTypeComboBox, row++, gbc);

    String tipo = (String) listingTypeComboBox.getSelectedItem();

    // O preço aparece somente para venda.
    if ("Venda".equals(tipo)) {
        JPanel pricePanel = new JPanel(new BorderLayout(8, 0));
        pricePanel.setOpaque(false);
        pricePanel.add(new JLabel("R$"), BorderLayout.WEST);
        pricePanel.add(priceField, BorderLayout.CENTER);

        addRow("Preço desejado:", pricePanel, row++, gbc);
    }

    addRow("Defeito principal:", conditionComboBox, row++, gbc);

    // Campo adicional aparece somente para "Outro defeito".
    if ("Outro defeito".equals(conditionComboBox.getSelectedItem())) {
        addRow("Explique o defeito:", otherDefectScroll, row++, gbc);
    }

    formPanel.revalidate();
formPanel.repaint();

SwingUtilities.invokeLater(() -> {
    Container parent = formPanel.getParent();

    if (parent instanceof JViewport) {
        parent.revalidate();
        parent.repaint();
    }
});
}

    
private void addRow(
        String label,
        JComponent field,
        int row,
        GridBagConstraints gbc) {

    gbc.gridy = row;
    gbc.gridx = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.CENTER;

    JLabel textLabel = new JLabel(label);
    textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    formPanel.add(textLabel, gbc);

    gbc.gridx = 1;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Mantém o tamanho dos campos com área de texto.
    if (field instanceof JScrollPane) {
        field.setPreferredSize(new Dimension(250, 85));
        field.setMinimumSize(new Dimension(250, 85));
    } else {
        field.setPreferredSize(new Dimension(250, 36));
    }


    formPanel.add(field, gbc);
}


    
private void styleField(JComponent field) {
    field.setFont(new Font("Arial", Font.PLAIN, 14));

    if (field instanceof JTextArea) {
        field.setPreferredSize(new Dimension(250, 85));
    } else {
        field.setPreferredSize(new Dimension(250, 36));
    }
}


    private void styleButton(JButton button, boolean primary) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(145, 40));
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

    
private void savePhone() {
    String brand = "Outro".equals(brandComboBox.getSelectedItem())
            ? otherBrandField.getText().trim()
            : (String) brandComboBox.getSelectedItem();

    String model = "Outro".equals(modelComboBox.getSelectedItem())
            ? otherModelField.getText().trim()
            : (String) modelComboBox.getSelectedItem();

    String description = descriptionArea.getText().trim();
    String priceText = priceField.getText().trim();
    String tipo = (String) listingTypeComboBox.getSelectedItem();
    String condition = (String) conditionComboBox.getSelectedItem();

    // Verifica os campos obrigatórios.
    if (brand == null || brand.isBlank()
            || model == null || model.isBlank()
            || tipo == null || tipo.isBlank()
            || condition == null || condition.isBlank()) {

        JOptionPane.showMessageDialog(
                this,
                "Preencha a marca, o modelo, a modalidade e o defeito.",
                "Campos obrigatórios",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // Se selecionou "Outro defeito", exige uma explicação.
    if ("Outro defeito".equals(condition)) {
        String detalheDefeito = otherDefectArea.getText().trim();

        if (detalheDefeito.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Explique qual é o outro defeito do celular.",
                    "Defeito não informado",
                    JOptionPane.WARNING_MESSAGE
            );
            otherDefectArea.requestFocusInWindow();
            return;
        }

        // Guarda o detalhe junto ao nome do defeito.
        condition = "Outro defeito: " + detalheDefeito;
    }

    // Só a modalidade Venda exige um preço.
    double price = 0.0;

    if ("Venda".equals(tipo)) {
        if (priceText.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o preço desejado para a venda.",
                    "Preço obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            priceField.requestFocusInWindow();
            return;
        }

        try {
            String normalizedPrice = priceText
                    .replace(".", "")
                    .replace(",", ".");

            price = Double.parseDouble(normalizedPrice);

            if (!Double.isFinite(price) || price <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "O preço deve ser maior que R$ 0,00.",
                        "Preço inválido",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite um preço válido.",
                    "Preço inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }

    String sql = """
        INSERT INTO phones
            (brand, model, description, price, condition, listing_type)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (Connection connection = Database.connect();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, brand);
        statement.setString(2, model);
        statement.setString(3, description);
        statement.setDouble(4, price);
        statement.setString(5, condition);
        statement.setString(6, tipo);

        statement.executeUpdate();

        JOptionPane.showMessageDialog(
                this,
                "Celular cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Limpa os campos após salvar.
        brandComboBox.setSelectedIndex(0);
        updateModelOptions();
        otherBrandField.setText("");
        otherModelField.setText("");
        descriptionArea.setText("");
        priceField.setText("");
        listingTypeComboBox.setSelectedItem("Venda");
        conditionComboBox.setSelectedIndex(0);
        otherDefectArea.setText("");

        rebuildForm();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Não foi possível salvar o celular:\n" + e.getMessage(),
                "Erro no banco de dados",
                JOptionPane.ERROR_MESSAGE
        );
    }
}


    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new RegisterPhone().setVisible(true)
        );
    }

    private Image carregarIcone() {
    java.net.URL recurso = getClass().getResource("/images/icon.png");

    if (recurso != null) {
        return new ImageIcon(recurso).getImage();
    }

    System.err.println("Ícone não encontrado: /images/icon.png");
    return null;
}

}


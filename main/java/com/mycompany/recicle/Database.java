
package com.mycompany.recicle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    private static final String URL = "jdbc:sqlite:recicle.db";

    // Abre a conexão com o banco de dados
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Cria as tabelas e atualiza a estrutura do banco
    public static void initialize() {
        String createPhonesTable = """
            CREATE TABLE IF NOT EXISTS phones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                brand TEXT NOT NULL,
                model TEXT NOT NULL,
                description TEXT,
                price REAL NOT NULL DEFAULT 0,
                condition TEXT NOT NULL,
                listing_type TEXT NOT NULL DEFAULT 'Preço',
                trade_description TEXT
            )
            """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            // Cria a tabela caso ela ainda não exista
            statement.execute(createPhonesTable);

            // Atualiza bancos antigos sem apagar os registros
            addColumnIfNotExists(
                connection,
                "phones",
                "listing_type",
                "TEXT NOT NULL DEFAULT 'Preço'"
            );

            addColumnIfNotExists(
                connection,
                "phones",
                "trade_description",
                "TEXT"
            );

            System.out.println(
                "Banco de dados inicializado com sucesso!"
            );

        } catch (SQLException e) {
            System.err.println(
                "Erro ao inicializar o banco de dados: "
                + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // Adiciona uma coluna somente se ela ainda não existir
    private static void addColumnIfNotExists(
            Connection connection,
            String tableName,
            String columnName,
            String columnDefinition) throws SQLException {

        boolean columnExists = false;

        String sql = "PRAGMA table_info(" + tableName + ")";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String existingColumn = resultSet.getString("name");

                if (columnName.equalsIgnoreCase(existingColumn)) {
                    columnExists = true;
                    break;
                }
            }
        }

        if (!columnExists) {
            String alterSql = "ALTER TABLE " + tableName
                    + " ADD COLUMN " + columnName
                    + " " + columnDefinition;

            try (Statement statement = connection.createStatement()) {
                statement.execute(alterSql);
            }

            System.out.println(
                "Coluna adicionada: " + columnName
            );
        }
    }
}


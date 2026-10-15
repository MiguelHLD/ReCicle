
package com.mycompany.recicle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    // Pasta permanente do banco de dados do ReCicle
    private static final Path DATABASE_FOLDER = Paths.get(
            System.getProperty("user.home"),
            "ReCicle"
    );

    // Caminho completo do arquivo do banco de dados
    private static final Path DATABASE_FILE =
            DATABASE_FOLDER.resolve("recicle.db");

    // Abre a conexão com o banco de dados
    public static Connection connect() throws SQLException {

        try {
            // Cria a pasta automaticamente, caso não exista
            Files.createDirectories(DATABASE_FOLDER);

        } catch (java.io.IOException e) {
            throw new SQLException(
                    "Não foi possível criar a pasta do banco de dados.",
                    e
            );
        }

        // Conecta ao banco usando um caminho absoluto
        String url = "jdbc:sqlite:" + DATABASE_FILE.toAbsolutePath();

        return DriverManager.getConnection(url);
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

            System.out.println(
                    "Local do banco: " + DATABASE_FILE.toAbsolutePath()
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

package com.support.compracarros.config;

import com.support.compracarros.controllers.AuthController;
import com.support.compracarros.dto.req.CreateUserRequest;
import com.support.compracarros.entities.UserPermission;
import com.support.compracarros.models.UserPermissionEnum;
import com.support.compracarros.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final PermissionRepository repository;

    private final AuthController authController;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<UserPermissionEnum> notInsertedPermissions;
        if (repository.count() == 0) {
            notInsertedPermissions = List.of(UserPermissionEnum.values());
            notInsertedPermissions.forEach(p -> repository.save(UserPermission.builder().permission(p).build()));
        }
        if (repository.count() < 3) {
            List<UserPermissionEnum> userPermissions = repository.findAll().stream().map(UserPermission::getPermission).toList();
            List<UserPermissionEnum> localUserPermissions = List.of(UserPermissionEnum.values());

            notInsertedPermissions = localUserPermissions.stream().filter(p -> !userPermissions.contains(p)).toList();
            notInsertedPermissions.forEach(p -> repository.save(UserPermission.builder().permission(p).build()));
        }

        try {
            // Check if marcas table is empty
            if (isTableEmpty("marcas")) {
                logger.info("Loading initial data into marcas table...");
                loadSqlFile("tabelafipemarcamodelo.sql");
                logger.info("Data loaded successfully!");
            } else {
                logger.info("Tables already contain data. Skipping initialization.");
            }
        } catch (Exception e) {
            logger.error("Error initializing data: ", e);
        }

    }

    /**
     * Check if a table is empty
     */
    private boolean isTableEmpty(String tableName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + tableName,
                    Integer.class
            );
            return count == null || count == 0;
        } catch (Exception e) {
            logger.warn("Table " + tableName + " might not exist: " + e.getMessage());
            return true;
        }
    }

    /**
     * Load and execute SQL file from resources
     */
    private void loadSqlFile(String resourcePath) throws Exception {
        ClassPathResource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            throw new Exception("SQL file not found: " + resourcePath);
        }

        String sqlContent = new String(Files.readAllBytes(
                Paths.get(resource.getFile().getAbsolutePath())
        ));

        // Split by semicolon to separate individual statements
        String[] statements = sqlContent.split(";");

        for (String statement : statements) {
            String trimmed = statement.trim();

            // Skip empty statements and comments
            if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                try {
                    jdbcTemplate.execute(trimmed + ";");
                    logger.debug("Executed: " + trimmed.substring(0, Math.min(50, trimmed.length())) + "...");
                } catch (Exception e) {
                    logger.error("Error executing statement: " + trimmed, e);
                    // Continue with next statement instead of failing
                }
            }
        }
    }

    /**
     * Alternative method: Load SQL file using Scanner (handles large files better)
     */
    public void loadSqlFileWithScanner(String resourcePath) throws Exception {
        ClassPathResource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            throw new Exception("SQL file not found: " + resourcePath);
        }

        Scanner scanner = new Scanner(resource.getFile());
        scanner.useDelimiter(";");

        while (scanner.hasNext()) {
            String statement = scanner.next().trim();

            if (!statement.isEmpty() && !statement.startsWith("--")) {
                try {
                    jdbcTemplate.execute(statement + ";");
                    logger.debug("Executed: " + statement.substring(0, Math.min(50, statement.length())) + "...");
                } catch (Exception e) {
                    logger.error("Error executing statement: " + statement, e);
                }
            }
        }

        scanner.close();
    }
}

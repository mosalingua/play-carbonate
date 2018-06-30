package play.modules.carbonate;

import java.io.File;

import play.Logger;
import play.Play;
import play.db.DBPlugin;

public class ApplyMigrationsMain {

    public static void main(String[] args) {
        String root = System.getProperty("application.path", ".");
        String id = System.getProperty("play.id", "");
        Play.init(new File(root), id);

        String migrationsPath = MigrationUtils.getPath();
        if (migrationsPath == null) {
            System.err.println("Missing configuration 'carbonate.path', database migrations will be ignored!");
            System.exit(-1);
        }

        Logger.info("Running migrations from path %s", migrationsPath);
        DBPlugin dbPlugin = Play.plugin(DBPlugin.class);
        dbPlugin.onApplicationStart();

        MigrationUtils.runMigrations(migrationsPath);
    }

}

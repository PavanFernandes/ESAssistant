package org.example.starters;

import org.example.entities.Player;
import org.example.entities.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.example.Main.*;


public class StarterCode {

    public static void loadData(){
        Path path = Paths.get(fileName);
        Path path2 = Paths.get("PlayersData.txt");
        boolean exits = Files.exists(path);
        if (exits) {
            try {
                Scanner scanner = new Scanner(Files.newBufferedReader(path));
                scanner.useDelimiter(",");
                while (scanner.hasNextLine()) {
                    String title = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String id = scanner.nextLine();
                    Report report = new Report(title, id);
                    reports.add(report);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean exits2 = Files.exists(path2);
        if (exits2) {
            try {
                Scanner scanner = new Scanner(Files.newBufferedReader(path2));
                scanner.useDelimiter(",");
                while (scanner.hasNextLine()) {
                    String id = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String name = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String raid = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String cr = scanner.next();
                    scanner.skip(scanner.delimiter());
                    String rank = scanner.nextLine();
                    Player p = new Player(id, name, raid, cr, rank);
                    ES.add(p);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

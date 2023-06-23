package JavaCore.Chapter3.Hometask;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MainHT23 {
    public static void main(String[] args) {
        GameProgress gp1 = new GameProgress(100, 2, 5, 100);
        GameProgress gp2 = new GameProgress(98, 3, 7, 250);
        GameProgress gp3 = new GameProgress(56, 10, 17, 1000);
        File forZip = new File("D:/Programming/Netology/Games/savegames/forZip");
        forZip.mkdir();
        saveGames("D:/Programming/Netology/Games/savegames/forZip/save3.dat", gp3);
        zipFiles("D:/Programming/Netology/Games/savegames/zip.zip", "D:/Programming/Netology/Games/savegames/forZip");
        forZip.delete();
        openZip("D:/Programming/Netology/Games/savegames/zip.zip", "D:/Programming/Netology/Games/savegames/");
        openProgress("D:/Programming/Netology/Games/savegames/!save3.dat");

    }

    public static void saveGames(String path, GameProgress gp) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(gp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipFiles(String zipPath, String listOfObjects) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            File directory = new File(listOfObjects);
            for (File file : directory.listFiles()) {
                System.out.println(file);
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                fis.close();

                ZipEntry entry = new ZipEntry("!" + file.getName());
                zout.putNextEntry(entry);
                zout.write(bytes);
                zout.closeEntry();
                file.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openZip(String pathZip, String pathUnpack) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String fileName;
            while ((entry = zis.getNextEntry()) != null) {
                fileName = entry.getName();

                FileOutputStream fout = new FileOutputStream(pathUnpack + fileName);
                for (int s = zis.read(); s != -1; s = zis.read()) {
                    fout.write(s);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openProgress(String pathSave) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(pathSave);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(gameProgress);
    }
}

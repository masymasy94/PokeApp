package com.masy.pokeapp.service;

import com.masy.pokeapp.data.Pokemon;


import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExcellService {

    final int NAME = 0;
    final int FORZA_ATTACCO = 1;
    final int TIPO1 = 2;
    final int TIPO2 = 3;
    final int EVOLVE = 4;
    final int EVOLVE_DUE_VOLTE = 5;

    List<Pokemon> allPkm = new ArrayList<>();

    public void readFile() {

        populatePokemonListReadingExcell();
        writeTxt();

    }

    private void writeTxt() {
        File txt = new File("C:\\Users\\Andrea\\Desktop\\pokemon app resources\\new.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txt))) {

            for (Pokemon pokemon : allPkm) {
                String row = MessageFormat.format("INSERT INTO pokemon (name, strenght, type, second_type, is_evolved, is_two_times_evolved) VALUES ({0},{1},{2},{3},{4},{5})",
                        "'"+StringUtils.trimToEmpty(pokemon.getName().replace("'", ""))+"'",
                        pokemon.getStrenght(),
                        "'"+StringUtils.trimToEmpty(pokemon.getType()).toUpperCase()+"'",
                        "'"+StringUtils.trimToEmpty(pokemon.getType2()).toUpperCase()+"'",
                        pokemon.isEvolved() ? "'S'" : "''",
                        pokemon.isEvolvedTwoTimes() ? "'S'" : "''");
                writer.write(row);
                writer.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatePokemonListReadingExcell() {
        File file = new File("C:\\Users\\Andrea\\Desktop\\pokemon app resources\\lista pokemon 1 gen.xlsx");
        try {
            XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0);

            IntStream.range(1, sheet.getPhysicalNumberOfRows()).forEach(row -> readRow(sheet, row));

            wb.close();
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

    private void readRow(XSSFSheet sheet, int r) {

        XSSFRow row = sheet.getRow(r);

        String name = row.getCell(NAME).toString();
        int forzaAttacco = (int) Math.round(Double.parseDouble(row.getCell(FORZA_ATTACCO).toString()));
        String tipo1 = row.getCell(TIPO1).toString();
        String tipo2 = row.getCell(TIPO2) == null? null : row.getCell(TIPO2).toString();
        boolean evolve = row.getCell(EVOLVE) != null;
        boolean evolveDueVolte =  row.getCell(EVOLVE_DUE_VOLTE) != null;

        allPkm.add(
                Pokemon.builder()
                        .name(name)
                        .isEvolved(evolve)
                        .isEvolvedTwoTimes(evolveDueVolte)
                        .type(tipo1)
                        .type2(tipo2)
                        .strenght(forzaAttacco)
                .build());


    }
}

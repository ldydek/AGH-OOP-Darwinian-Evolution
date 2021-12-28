package agh.ics.project1;

import java.util.Arrays;

public class Genes {
    private final int[] genes = new int[32];

    public Genes() {
        for (int i=0; i<32; i++) {
            genes[i] = (int) Math.round(Math.random() * 7);
        }
        Arrays.sort(genes);
//        można napisać countsorta, bo liczby są od 0 do 7 (sortować będzie wtedy w czasie liniowym)
    }

    public int[] getGenes() {
        return this.genes;
    }

    public int getRandom() {
        int rnd = (int) Math.round(Math.random() * 31);
        return genes[rnd];
    }

    public void setGene(int i, int j) {
        genes[i] = j;
    }

    public String toString() {
        String genes1 = "";
        for (int i=0; i<32; i++) {
            genes1 += genes[i];
        }
        return genes1;
    }
}

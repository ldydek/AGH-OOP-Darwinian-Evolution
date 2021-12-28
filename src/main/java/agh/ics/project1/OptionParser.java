package agh.ics.project1;

public class OptionParser {
    protected static int[] data = new int[7];
//  klasa zamieniająca dane wejściowe na liczby całkowite reprezentujące np. energię rośliny
//  0) wysokość mapy,
//  1) szerokość mapy,
//  2) ilość energii początkowej zwierząt
//  3) ilość energii traconej w każdym dniu
//  4) ilość energii rośliny
//  5) proporcje dżungli do sawanny
//  6) ilość początkowych zwierząt na mapie
    public int[] parse(String[] args) {
        int length = args.length;
        for (int i=0; i<length; i++) {
            OptionParser.data[i] = Integer.parseInt(args[i]);
        }
        return data;
    }

    public int[] getData() {
        return data;
    }
}

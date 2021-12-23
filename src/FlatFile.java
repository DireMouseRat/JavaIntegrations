import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FlatFile {
    private List<String> header = new ArrayList<>();
    private final List<List<String>> records = new ArrayList<>();

    public void read(String filePath, String delimiter, boolean hasHeader) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filePath));
        if(hasHeader) {
            header = Arrays.asList(sc.nextLine().split(delimiter));
        }
        while (sc.hasNextLine()) {
            records.add(Arrays.asList(sc.nextLine().split(delimiter)));
        }
        sc.close();
    }

    public HashMap<String, String> getDictionary(String id, String value) {
        HashMap<String, String> dictionary = new HashMap<>();
        if(header.isEmpty()) {
            return null;
        }
        else {
            int keyIndex = header.indexOf(id);
            int valueIndex = header.indexOf(value);
            if(keyIndex == -1 || valueIndex == -1)
            {
                return null;
            }
            else {
                for(List<String> record : records) {
                    dictionary.put(record.get(keyIndex), record.get(valueIndex));
                }
                return dictionary;
            }
        }
    }

    public List<List<String>> Records(){
        return new ArrayList<>(records);
    }
}
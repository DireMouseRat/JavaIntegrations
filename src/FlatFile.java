import java.io.*;
import java.util.*;

public class FlatFile {
    private List<String> header = new ArrayList<>();
    private final List<List<String>> records = new ArrayList<>();

    public FlatFile(String filePath, String delimiter, boolean hasHeader) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filePath));
        ScanFile(sc, delimiter, hasHeader);
        sc.close();
    }

    public FlatFile(InputStream inputStream, String delimiter, boolean hasHeader) throws IOException {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        Scanner sc = new Scanner(br);
        ScanFile(sc, delimiter, hasHeader);
        sc.close();
        br.close();
        isr.close();
    }

    private void ScanFile(Scanner scanner, String delimiter, boolean hasHeader){
        if(hasHeader) {
            header = Arrays.asList(scanner.nextLine().split(delimiter));
        }
        while (scanner.hasNextLine()) {
            records.add(Arrays.asList(scanner.nextLine().split(delimiter)));
        }
    }

    // Using 2 fields as the key-value pair, return a dictionary (HashMap) from the FlatFile
    // Duplicate keys are overwritten
    public HashMap<String, String> getDictionary(String key, String value) throws IndexOutOfBoundsException{
        int keyIndex = getFieldIndex(key);
        int valueIndex = getFieldIndex(value);
        HashMap<String, String> dictionary = new HashMap<>();
        for(List<String> record : records) {
            dictionary.put(record.get(keyIndex), record.get(valueIndex));
        }
        return dictionary;
    }

    public List<String> firstRecord() throws IndexOutOfBoundsException{
        return records.get(0);
    }

    //Requires header
    public String firstRecordValueAt(String fieldName) throws IndexOutOfBoundsException{
        int fieldIndex = getFieldIndex(fieldName);
        return firstRecord().get(fieldIndex);
    }

    public boolean hasNextRecord(){
        return records.iterator().hasNext();
    }

    public List<String> nextRecord() throws NoSuchElementException {
        return records.iterator().next();
    }

    public int recordCount(){
        return records.size();
    }

    public int fieldCount(){
        if(hasHeader()){
            return header.size();
        }
        return firstRecord().size();
    }

    public boolean hasHeader(){return !header.isEmpty();}
    public boolean noHeader(){return header.isEmpty();}

    //Requires header
    public boolean hasDuplicates(String fieldName) throws IndexOutOfBoundsException {
        return hasDuplicates(getFieldIndex(fieldName));
    }

    public boolean hasDuplicates(int fieldIndex) throws IndexOutOfBoundsException {
        HashSet<String> set = new HashSet<>();
        return records.stream().anyMatch(record -> !set.add(record.get(fieldIndex)));
    }

    //Requires header
    public List<String> getFieldNames() throws IndexOutOfBoundsException{
        assertHeader();
        return header;
    }

    private void assertHeader() throws IndexOutOfBoundsException{
        if(noHeader()) throw new IndexOutOfBoundsException("Header not found");
    }

    //Requires header
    public int getFieldIndex(String fieldName) throws IndexOutOfBoundsException {
        assertHeader();
        int fieldIndex = header.indexOf(fieldName);
        if(fieldIndex == -1) throw new IndexOutOfBoundsException("Field not found");
        return fieldIndex;
    }

    //Requires header
    public String getFieldName(int fieldIndex) throws IndexOutOfBoundsException{
        assertHeader();
        return header.get(fieldIndex);
    }

    public List<List <String>> getRecords() {
        return records;
    }
}
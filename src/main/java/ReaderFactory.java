import java.io.IOException;

public class ReaderFactory {

    public FormatReader concreteReader(String path) throws IOException {
        if(path.contains(".csv")) return new CsvReader(path);
        else if(path.contains(".json")) return new JsonReader(path);
        else if(path.contains(".xml")) return new XmlReader(path);
        else throw new IOException("This format is not supported yet");
    }
}
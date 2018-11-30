import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class ReaderFactoryTest {
    @Test
    public void CsvTest() throws IOException {
        ReaderFactory readerFactory = new ReaderFactory();
        FormatReader csvReader = readerFactory.concreteReader("file.csv");
        assertThat(csvReader, instanceOf(CsvReader.class));
    }
    @Test
    public void JsonTest() throws IOException {
        ReaderFactory readerFactory = new ReaderFactory();
        FormatReader reader = readerFactory.concreteReader("file.json");
        assertThat(reader, instanceOf(JsonReader.class));
    }
    @Test
    public void XmlTest() throws IOException {
        ReaderFactory readerFactory = new ReaderFactory();
        FormatReader reader = readerFactory.concreteReader("file.xml");
        assertThat(reader, instanceOf(XmlReader.class));
    }
    @Test(expected = IOException.class)
    public void wrongFormatTest() throws IOException {
        ReaderFactory readerFactory = new ReaderFactory();
        FormatReader reader = readerFactory.concreteReader("file.java");
        assertThat(reader, instanceOf(XmlReader.class));
    }
}
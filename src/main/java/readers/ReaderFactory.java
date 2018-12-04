package readers;

import java.io.IOException;

public class ReaderFactory {

    public FormatReader concreteReader(String path, WebSites.SupportedWebSites webSite) throws IOException {
        if(path.contains(".csv") && webSite == WebSites.SupportedWebSites.YAHOOFINANCE) return new YahooFinanceCsvReader(path);
        else if(path.contains(".json") && webSite == WebSites.SupportedWebSites.NBP) return new NbpJsonReader(path);
        else if(path.contains(".xml") && webSite == WebSites.SupportedWebSites.NBP) return new NbpXmlReader(path);
        else throw new IOException("This format is not supported");
    }
}
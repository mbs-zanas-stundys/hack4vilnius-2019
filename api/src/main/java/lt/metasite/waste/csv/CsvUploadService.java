package lt.metasite.waste.csv;

import java.nio.file.Path;

public interface CsvUploadService {

    void parseFile(Path pathToFile);

    String getFilePattern();

}
